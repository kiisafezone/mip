package com.kiisafezone.mip.contracts

import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MatchExportContractTest {
    @Test fun exportRoundTripPreservesSessionEventsAndCorrectionHistory() {
        val session = MatchSession(
            id = MatchSessionId("match-1"),
            startedAtEpochMillis = 10,
            heroName = "Miya",
            role = "Gold Lane",
        )
        val event = MatchEvent(
            id = MatchEventId("event-1"),
            matchId = session.id,
            type = "HERO_DIED",
            gameTimeMs = 30_000,
            recordedAtEpochMs = 20,
            source = MatchEventSource.MANUAL,
            confidence = 1.0,
        )
        val revision = MatchEventRevision(
            id = "revision-1",
            eventId = event.id,
            matchId = session.id,
            beforeSnapshotJson = """{"gameTimeMs":30000}""",
            afterSnapshotJson = """{"gameTimeMs":25000}""",
            reason = "Corrected from match notes",
            changedAtEpochMs = 30,
        )
        val export = MatchExportContract(
            exportedAtEpochMs = 40,
            session = session,
            events = listOf(event),
            revisions = listOf(revision),
        )
        val json = Json { ignoreUnknownKeys = true }
        assertEquals(export, json.decodeFromString<MatchExportContract>(json.encodeToString(export)))
    }
}
