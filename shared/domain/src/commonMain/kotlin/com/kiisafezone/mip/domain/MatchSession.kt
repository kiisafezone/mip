package com.kiisafezone.mip.domain

@JvmInline
value class MatchSessionId(val value: String) {
    init { require(value.isNotBlank()) { "Match session ID must not be blank." } }
}

/** Platform-independent identity and timing metadata for one manually recorded match. */
data class MatchSession(
    val id: MatchSessionId,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long? = null,
) {
    init {
        require(startedAtEpochMillis >= 0) { "Start time must not be negative." }
        require(endedAtEpochMillis == null || endedAtEpochMillis >= startedAtEpochMillis) {
            "End time cannot precede start time."
        }
    }
}
