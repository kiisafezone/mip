package com.kiisafezone.mip.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomMatchRepositoryTest {
    private lateinit var database: MipDatabase
    private lateinit var repository: RoomMatchRepository

    @Before fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MipDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RoomMatchRepository(database)
    }

    @After fun tearDown() {
        database.close()
    }

    @Test fun persistsMatchAndReturnsEventsInDeterministicOrder() = runBlocking {
        val match = MatchSession(MatchSessionId("match-1"), 1_000, heroName = "Miya", role = "Gold Lane", gameVersionId = "patch-1", durationMs = 720_000)
        repository.save(match)
        repository.save(event("event-z", match.id, 20_000, 20))
        repository.save(event("event-b", match.id, 10_000, 20))
        repository.save(event("event-a", match.id, 10_000, 10))

        assertEquals(match, repository.get(match.id))
        assertEquals(listOf("event-a", "event-b", "event-z"), repository.getForMatch(match.id).map { it.id.value })
    }

    @Test fun updatingMatchMetadataDoesNotCascadeDeleteItsEvents() = runBlocking {
        val match = MatchSession(MatchSessionId("match-update"), 1_000, heroName = "Miya", role = "Gold Lane")
        val event = event("event-1", match.id, 20_000, 20)
        repository.save(match)
        repository.save(event)
        repository.save(match.copy(notes = "updated notes"))

        assertEquals("updated notes", repository.get(match.id)?.notes)
        assertEquals(listOf(event), repository.getForMatch(match.id))
    }

    @Test fun dataSurvivesClosingAndReopeningTheDatabase() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "mip-persistence-test.db"
        context.deleteDatabase(databaseName)
        val firstDatabase = Room.databaseBuilder(context, MipDatabase::class.java, databaseName).build()
        val firstRepository = RoomMatchRepository(firstDatabase)
        val match = MatchSession(MatchSessionId("restart-match"), 1_000, heroName = "Miya", role = "Gold Lane")
        firstRepository.save(match)
        firstRepository.save(event("restart-event", match.id, 15_000, 20))
        firstDatabase.close()

        val secondDatabase = Room.databaseBuilder(context, MipDatabase::class.java, databaseName).build()
        val restored = RoomMatchRepository(secondDatabase)
        assertEquals(match, restored.get(match.id))
        assertEquals("restart-event", restored.getForMatch(match.id).single().id.value)
        secondDatabase.close()
        context.deleteDatabase(databaseName)
        Unit
    }

    @Test fun correctionAndRevisionAreCommittedTogether() = runBlocking {
        val match = MatchSession(MatchSessionId("match-2"), 1_000)
        val original = event("event-1", match.id, 30_000, 20)
        repository.save(match)
        repository.save(original)
        val corrected = original.copy(gameTimeMs = 25_000)
        val revision = MatchEventRevision(
            id = "revision-1",
            eventId = original.id,
            matchId = match.id,
            beforeSnapshotJson = """{"gameTimeMs":30000}""",
            afterSnapshotJson = """{"gameTimeMs":25000}""",
            reason = "Timestamp checked against notes",
            changedAtEpochMs = 30,
        )

        repository.correct(corrected, revision)

        assertEquals(25_000L, repository.getForMatch(match.id).single().gameTimeMs)
        assertEquals(listOf(revision), repository.getRevisions(original.id))
    }

    @Test fun deletingAnEventDoesNotEraseItsCorrectionHistory() = runBlocking {
        val match = MatchSession(MatchSessionId("match-3"), 1_000)
        val original = event("event-1", match.id, 30_000, 20)
        repository.save(match)
        repository.save(original)
        val revision = MatchEventRevision(
            id = "revision-1",
            eventId = original.id,
            matchId = match.id,
            beforeSnapshotJson = "{}",
            afterSnapshotJson = "{}",
            reason = "Correction",
            changedAtEpochMs = 30,
        )
        repository.correct(original, revision)
        repository.delete(original.id)

        assertEquals(emptyList<MatchEvent>(), repository.getForMatch(match.id))
        assertEquals(listOf(revision), repository.getRevisionsForMatch(match.id))
    }

    @Test fun deletingMatchCascadesToEventsAndRevisions() = runBlocking {
        val match = MatchSession(MatchSessionId("match-4"), 1_000)
        val original = event("event-1", match.id, 30_000, 20)
        repository.save(match)
        repository.save(original)
        repository.correct(
            original,
            MatchEventRevision("revision-1", original.id, match.id, "{}", "{}", "Correction", 30),
        )
        repository.delete(match.id)

        assertEquals(null, repository.get(match.id))
        assertEquals(emptyList<MatchEvent>(), repository.getForMatch(match.id))
        assertEquals(emptyList<MatchEventRevision>(), repository.getRevisionsForMatch(match.id))
    }

    private fun event(id: String, matchId: MatchSessionId, gameTimeMs: Long, recordedAt: Long) = MatchEvent(
        id = MatchEventId(id),
        matchId = matchId,
        type = "HERO_DIED",
        gameTimeMs = gameTimeMs,
        recordedAtEpochMs = recordedAt,
        source = MatchEventSource.MANUAL,
        confidence = 1.0,
        payload = buildJsonObject {},
    )
}
