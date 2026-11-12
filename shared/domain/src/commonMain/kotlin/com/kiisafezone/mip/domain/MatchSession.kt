package com.kiisafezone.mip.domain

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class MatchSessionId(val value: String) {
    init { require(value.isNotBlank()) { "Match session ID must not be blank." } }
}

@Serializable
enum class MatchSessionStatus { ACTIVE, PAUSED, COMPLETED }

@Serializable
enum class MatchResult { WIN, LOSS, DRAW, UNKNOWN }

/** Platform-independent metadata for one manually reviewed match. */
@Serializable
data class MatchSession(
    val id: MatchSessionId,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long? = null,
    val status: MatchSessionStatus = MatchSessionStatus.ACTIVE,
    val gameVersionId: String? = null,
    val notes: String? = null,
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val heroName: String? = null,
    val role: String? = null,
    val result: MatchResult? = null,
    val mode: String? = null,
) {
    init {
        require(startedAtEpochMillis >= 0) { "Start time must not be negative." }
        require(endedAtEpochMillis == null || endedAtEpochMillis >= startedAtEpochMillis) {
            "End time cannot precede start time."
        }
        require(schemaVersion > 0) { "Schema version must be positive." }
        require(gameVersionId == null || gameVersionId.isNotBlank()) { "Game version ID must not be blank." }
        require(heroName == null || heroName.isNotBlank()) { "Hero name must not be blank." }
        require(role == null || role.isNotBlank()) { "Role must not be blank." }
        require(mode == null || mode.isNotBlank()) { "Mode must not be blank." }
        require(status != MatchSessionStatus.COMPLETED || endedAtEpochMillis != null) {
            "A completed session must have an end timestamp."
        }
        require(status == MatchSessionStatus.COMPLETED || endedAtEpochMillis == null) {
            "Only a completed session may have an end timestamp."
        }
    }

    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}
