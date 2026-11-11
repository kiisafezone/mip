package com.kiisafezone.mip.contracts

import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchEventValidation
import com.kiisafezone.mip.domain.MatchSessionId
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Stable serialized interchange shape. A JSON object payload preserves unknown
 * optional keys, and the string event type preserves types added by newer clients.
 */
@Serializable
data class MatchEventContract(
    val id: String,
    val matchId: String,
    val type: String,
    val gameTimeMs: Long,
    val recordedAtEpochMs: Long,
    val source: MatchEventSource,
    val confidence: Double,
    val validation: MatchEventValidation = MatchEventValidation.UNREVIEWED,
    val actorId: String? = null,
    val payload: JsonObject = JsonObject(emptyMap()),
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
) {
    init {
        require(id.isNotBlank()) { "Event ID must not be blank." }
        require(matchId.isNotBlank()) { "Match ID must not be blank." }
        require(type.isNotBlank()) { "Event type must not be blank." }
        require(gameTimeMs >= 0) { "Game time must not be negative." }
        require(recordedAtEpochMs >= 0) { "Recorded timestamp must not be negative." }
        require(confidence.isFinite() && confidence in 0.0..1.0) {
            "Confidence must be finite and within [0, 1]."
        }
        require(schemaVersion > 0) { "Schema version must be positive." }
    }

    fun toDomain(): MatchEvent = MatchEvent(
        id = MatchEventId(id),
        matchId = MatchSessionId(matchId),
        type = type,
        gameTimeMs = gameTimeMs,
        recordedAtEpochMs = recordedAtEpochMs,
        source = source,
        confidence = confidence,
        validation = validation,
        actorId = actorId,
        payload = payload,
        schemaVersion = schemaVersion,
    )

    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}

fun MatchEvent.toContract(): MatchEventContract = MatchEventContract(
    id = id.value,
    matchId = matchId.value,
    type = type,
    gameTimeMs = gameTimeMs,
    recordedAtEpochMs = recordedAtEpochMs,
    source = source,
    confidence = confidence,
    validation = validation,
    actorId = actorId,
    payload = payload,
    schemaVersion = schemaVersion,
)
