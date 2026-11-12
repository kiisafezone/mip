package com.kiisafezone.mip.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MatchSessionTest {
    @Test fun rejectsNegativeStartAndEndBeforeStart() {
        assertFailsWith<IllegalArgumentException> {
            MatchSession(MatchSessionId("m1"), -1)
        }
        assertFailsWith<IllegalArgumentException> {
            MatchSession(MatchSessionId("m1"), 100, endedAtEpochMillis = 99)
        }
    }

    @Test fun completedSessionRequiresEndTimestamp() {
        assertFailsWith<IllegalArgumentException> {
            MatchSession(MatchSessionId("m1"), 100, status = MatchSessionStatus.COMPLETED)
        }
    }

    @Test fun sessionJsonRoundTripPreservesVersionedFields() {
        val session = MatchSession(
            id = MatchSessionId("m1"),
            startedAtEpochMillis = 100,
            endedAtEpochMillis = 200,
            status = MatchSessionStatus.COMPLETED,
            gameVersionId = "patch-1",
            notes = "manual review",
            heroName = "Miya",
            role = "Gold Lane",
            result = MatchResult.WIN,
            mode = "Ranked",
            durationMs = 720_000,
        )
        val json = Json { ignoreUnknownKeys = true }
        assertEquals(session, json.decodeFromString<MatchSession>(json.encodeToString(session)))
    }

    @Test fun rejectsNegativeMatchDuration() {
        assertFailsWith<IllegalArgumentException> {
            MatchSession(MatchSessionId("m1"), 100, durationMs = -1)
        }
    }

    @Test fun matchMetadataMustNotBeBlankWhenPresent() {
        assertFailsWith<IllegalArgumentException> {
            MatchSession(MatchSessionId("m1"), 100, heroName = " ")
        }
    }
}
