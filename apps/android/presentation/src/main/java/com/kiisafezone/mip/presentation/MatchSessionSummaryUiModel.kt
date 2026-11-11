package com.kiisafezone.mip.presentation

import com.kiisafezone.mip.domain.MatchSession

/** Presentation-facing projection; Compose screens can consume it without owning domain rules. */
data class MatchSessionSummaryUiModel(
    val sessionId: String,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long?,
) {
    companion object {
        fun from(session: MatchSession) = MatchSessionSummaryUiModel(
            sessionId = session.id.value,
            startedAtEpochMillis = session.startedAtEpochMillis,
            endedAtEpochMillis = session.endedAtEpochMillis,
        )
    }
}
