package com.kiisafezone.mip.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith

class MatchSessionRevisionTest {
    @Test fun rejectsBlankRevisionId() {
        assertFailsWith<IllegalArgumentException> {
            MatchSessionRevision("", MatchSessionId("match-1"), "{}", "{}", "Reason", 1)
        }
    }

    @Test fun rejectsBlankSnapshotsAndReason() {
        assertFailsWith<IllegalArgumentException> {
            MatchSessionRevision("revision-1", MatchSessionId("match-1"), " ", "{}", "Reason", 1)
        }
        assertFailsWith<IllegalArgumentException> {
            MatchSessionRevision("revision-2", MatchSessionId("match-1"), "{}", " ", "Reason", 1)
        }
        assertFailsWith<IllegalArgumentException> {
            MatchSessionRevision("revision-3", MatchSessionId("match-1"), "{}", "{}", " ", 1)
        }
    }

    @Test fun rejectsNegativeRevisionTimeAndInvalidSchemaVersion() {
        assertFailsWith<IllegalArgumentException> {
            MatchSessionRevision("revision-1", MatchSessionId("match-1"), "{}", "{}", "Reason", -1)
        }
        assertFailsWith<IllegalArgumentException> {
            MatchSessionRevision("revision-2", MatchSessionId("match-1"), "{}", "{}", "Reason", 1, 0)
        }
    }
}
