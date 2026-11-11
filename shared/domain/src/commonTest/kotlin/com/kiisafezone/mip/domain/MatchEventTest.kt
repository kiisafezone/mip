package com.kiisafezone.mip.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class MatchEventTest {
    private val sample = MatchEvent(
        id = MatchEventId("event-1"),
        matchId = MatchSessionId("match-1"),
        type = MatchEventTypes.SPELL_USED,
        gameTimeMs = 12_000,
        recordedAtEpochMs = 1_800_000_000_000,
        source = MatchEventSource.MANUAL,
        confidence = 1.0,
        payload = buildJsonObject { put("abilityId", "flicker"); put("futureField", "preserved") },
    )

    @Test fun rejectsNegativeGameTime() {
        assertFailsWith<IllegalArgumentException> { sample.copy(gameTimeMs = -1) }
    }

    @Test fun rejectsNegativeRecordedTimestamp() {
        assertFailsWith<IllegalArgumentException> { sample.copy(recordedAtEpochMs = -1) }
    }

    @Test fun rejectsConfidenceOutsideRangeAndNonFiniteValues() {
        assertFailsWith<IllegalArgumentException> { sample.copy(confidence = -0.01) }
        assertFailsWith<IllegalArgumentException> { sample.copy(confidence = 1.01) }
        assertFailsWith<IllegalArgumentException> { sample.copy(confidence = Double.NaN) }
    }

    @Test fun sortsEqualGameTimesDeterministically() {
        val laterId = sample.copy(id = MatchEventId("z"), recordedAtEpochMs = 20)
        val earlierClock = sample.copy(id = MatchEventId("b"), recordedAtEpochMs = 10)
        val tieBreak = sample.copy(id = MatchEventId("a"), recordedAtEpochMs = 10)
        assertEquals(
            listOf("a", "b", "z"),
            listOf(laterId, earlierClock, tieBreak).sortedWith(matchEventTimelineComparator).map { it.id.value },
        )
    }

    @Test fun jsonRoundTripPreservesUnknownEventTypesAndPayloadFields() {
        val json = Json { ignoreUnknownKeys = true }
        val future = sample.copy(
            type = "FUTURE_EVENT_TYPE",
            payload = buildJsonObject { put("newOptionalField", "keep-me") },
        )
        val decoded = json.decodeFromString<MatchEvent>(json.encodeToString(future))
        assertEquals("FUTURE_EVENT_TYPE", decoded.type)
        assertEquals("keep-me", decoded.payload["newOptionalField"]?.toString()?.trim('"'))
    }
}
