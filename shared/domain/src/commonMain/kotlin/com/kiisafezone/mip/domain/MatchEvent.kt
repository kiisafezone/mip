package com.kiisafezone.mip.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

@JvmInline
@Serializable
value class MatchEventId(val value: String) {
    init { require(value.isNotBlank()) { "Match event ID must not be blank." } }
}

@Serializable
enum class MatchEventSource { MANUAL, AUTHORIZED_IMPORT, SYNTHETIC }

@Serializable
enum class MatchEventValidation { UNREVIEWED, CONFIRMED, CORRECTED, REJECTED }

/** String constants preserve forward compatibility with event types added by newer clients. */
object MatchEventTypes {
    const val SPELL_USED = "SPELL_USED"
    const val HERO_DIED = "HERO_DIED"
    const val OBJECTIVE_OBSERVED = "OBJECTIVE_OBSERVED"
    const val TACTICAL_NOTE = "TACTICAL_NOTE"

    val initial: Set<String> = setOf(SPELL_USED, HERO_DIED, OBJECTIVE_OBSERVED, TACTICAL_NOTE)
}

@Serializable
data class MatchEvent(
    val id: MatchEventId,
    val matchId: MatchSessionId,
    val type: String,
    val gameTimeMs: Long,
    val recordedAtEpochMs: Long,
    val source: MatchEventSource,
    val confidence: Double,
    val validation: MatchEventValidation = MatchEventValidation.UNREVIEWED,
    val actorId: String? = null,
    val payload: JsonObject = buildJsonObject {},
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
) {
    init {
        require(type.isNotBlank()) { "Event type must not be blank." }
        require(gameTimeMs >= 0) { "Game time must not be negative." }
        require(recordedAtEpochMs >= 0) { "Recorded timestamp must not be negative." }
        require(confidence.isFinite() && confidence in 0.0..1.0) {
            "Confidence must be finite and within [0, 1]."
        }
        require(actorId == null || actorId.isNotBlank()) { "Actor ID must not be blank." }
        require(schemaVersion > 0) { "Schema version must be positive." }
    }

    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}

/** Stable ordering: match time, wall-clock time, then stable event ID. */
val matchEventTimelineComparator: Comparator<MatchEvent> =
    compareBy<MatchEvent> { it.gameTimeMs }
        .thenBy { it.recordedAtEpochMs }
        .thenBy { it.id.value }
