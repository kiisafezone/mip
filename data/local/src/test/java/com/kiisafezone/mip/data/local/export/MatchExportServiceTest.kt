package com.kiisafezone.mip.data.local.export

import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRepository
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchEventValidation
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionRepository
import com.kiisafezone.mip.domain.MatchSessionRevision
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class MatchExportServiceTest {
    @Test fun exportIncludesSessionEventsAndAllCorrectionRevisions() = runBlocking {
        val session = MatchSession(
            id = MatchSessionId("match-1"),
            startedAtEpochMillis = 100,
            heroName = "Miya",
            role = "Gold Lane",
        )
        val event = MatchEvent(
            id = MatchEventId("event-1"),
            matchId = session.id,
            type = "HERO_DIED",
            gameTimeMs = 30_000,
            recordedAtEpochMs = 200,
            source = MatchEventSource.MANUAL,
            confidence = 1.0,
            validation = MatchEventValidation.CORRECTED,
        )
        val revision = MatchEventRevision(
            id = "revision-1",
            eventId = event.id,
            matchId = session.id,
            beforeSnapshotJson = """{"gameTimeMs":35000}""",
            afterSnapshotJson = """{"gameTimeMs":30000}""",
            reason = "Verified from notes",
            changedAtEpochMs = 300,
        )
        val sessionRevision = MatchSessionRevision(
            id = "session-revision-1",
            matchId = session.id,
            beforeSnapshotJson = """{"notes":null}""",
            afterSnapshotJson = """{"notes":"corrected"}""",
            reason = "Corrected match metadata",
            changedAtEpochMs = 350,
        )
        val service = MatchExportService(
            sessions = FakeSessions(session, sessionRevision),
            events = FakeEvents(session.id, event, revision),
        )

        val decoded = Json.decodeFromString<com.kiisafezone.mip.contracts.MatchExportContract>(
            service.exportMatch(session.id, exportedAtEpochMs = 400),
        )

        assertEquals(session, decoded.session)
        assertEquals(listOf(event), decoded.events)
        assertEquals(listOf(revision), decoded.revisions)
        assertEquals(listOf(sessionRevision), decoded.sessionRevisions)
        assertEquals(400L, decoded.exportedAtEpochMs)
    }

    private class FakeSessions(
        private val session: MatchSession,
        private val revision: MatchSessionRevision,
    ) : MatchSessionRepository {
        override suspend fun get(id: MatchSessionId) = session.takeIf { it.id == id }
        override suspend fun getAll() = listOf(session)
        override suspend fun save(session: MatchSession) = Unit
        override suspend fun revise(session: MatchSession, revision: MatchSessionRevision) = Unit
        override suspend fun getSessionRevisionsForMatch(matchId: MatchSessionId) =
            if (matchId == session.id) listOf(revision) else emptyList()
        override suspend fun delete(id: MatchSessionId) = Unit
    }

    private class FakeEvents(
        private val matchId: MatchSessionId,
        private val event: MatchEvent,
        private val revision: MatchEventRevision,
    ) : MatchEventRepository {
        override suspend fun getForMatch(matchId: MatchSessionId) =
            if (matchId == this.matchId) listOf(event) else emptyList()
        override suspend fun save(event: MatchEvent) = Unit
        override suspend fun correct(event: MatchEvent, revision: MatchEventRevision) = Unit
        override suspend fun getRevisions(eventId: MatchEventId) =
            if (eventId == event.id) listOf(revision) else emptyList()
        override suspend fun getRevisionsForMatch(matchId: MatchSessionId) =
            if (matchId == this.matchId) listOf(revision) else emptyList()
        override suspend fun delete(eventId: MatchEventId) = Unit
    }
}
