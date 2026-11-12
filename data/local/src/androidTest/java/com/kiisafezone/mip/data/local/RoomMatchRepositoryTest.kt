package com.kiisafezone.mip.data.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionRevision
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomMatchRepositoryTest {
    private lateinit var database: MipDatabase
    private lateinit var repository: RoomMatchRepository
    private val migrationHelper by lazy {
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MipDatabase::class.java,
            emptyList(),
            FrameworkSQLiteOpenHelperFactory(),
        )
    }

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

    @Test fun metadataRevisionHistoryIsPersistedAndImmutable() = runBlocking {
        val match = MatchSession(MatchSessionId("metadata-match"), 1_000, heroName = "Miya", role = "Gold Lane")
        repository.save(match)
        val updated = match.copy(notes = "Updated after review")
        val revision = MatchSessionRevision(
            id = "session-revision-1",
            matchId = match.id,
            beforeSnapshotJson = """{"notes":null}""",
            afterSnapshotJson = """{"notes":"Updated after review"}""",
            reason = "Corrected match notes",
            changedAtEpochMs = 50,
        )

        repository.revise(updated, revision)

        assertEquals(updated, repository.get(match.id))
        assertEquals(listOf(revision), repository.getSessionRevisionsForMatch(match.id))
        try {
            repository.revise(updated.copy(notes = "Tampered"), revision.copy(reason = "Rewrite history"))
            fail("Duplicate immutable revision IDs must be rejected.")
        } catch (_: SQLiteConstraintException) {
            // INSERT ABORT protects the original audit record; the transaction rolls back.
        }
        assertEquals(updated, repository.get(match.id))
        assertEquals(listOf(revision), repository.getSessionRevisionsForMatch(match.id))
    }

    @Test fun migratesVersionOneDatabaseWithoutLosingMatchRows() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "mip-migration-1-2.db"
        context.deleteDatabase(databaseName)
        val versionOne = migrationHelper.createDatabase(databaseName, 1)
        versionOne.execSQL(
            "INSERT INTO match_sessions (id, startedAtEpochMillis, endedAtEpochMillis, status, " +
                "gameVersionId, notes, schemaVersion, heroName, role, result, mode, durationMs) " +
                "VALUES ('migration-match', 1000, NULL, 'ACTIVE', NULL, NULL, 1, 'Miya', 'Gold Lane', NULL, NULL, NULL)",
        )
        versionOne.close()

        val migratedDatabase = Room.databaseBuilder(context, MipDatabase::class.java, databaseName)
            .addMigrations(MIGRATION_1_2)
            .build()
        val migratedRepository = RoomMatchRepository(migratedDatabase)
        runBlocking {
            assertEquals("Miya", migratedRepository.get(MatchSessionId("migration-match"))?.heroName)
            assertEquals(emptyList<MatchSessionRevision>(), migratedRepository.getSessionRevisionsForMatch(MatchSessionId("migration-match")))
        }
        migratedDatabase.close()
        context.deleteDatabase(databaseName)
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
        repository.revise(
            match.copy(notes = "Updated"),
            MatchSessionRevision("session-revision-1", match.id, "{}", """{"notes":"Updated"}""", "Correction", 40),
        )
        repository.delete(match.id)

        assertEquals(null, repository.get(match.id))
        assertEquals(emptyList<MatchEvent>(), repository.getForMatch(match.id))
        assertEquals(emptyList<MatchEventRevision>(), repository.getRevisionsForMatch(match.id))
        assertEquals(emptyList<MatchSessionRevision>(), repository.getSessionRevisionsForMatch(match.id))
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
