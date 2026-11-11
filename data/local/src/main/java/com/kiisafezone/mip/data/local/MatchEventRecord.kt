package com.kiisafezone.mip.data.local

import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchEventValidation
import com.kiisafezone.mip.domain.MatchSessionId
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.encodeToString

/** Room-shaped row; payload is stored as JSON text in SQLite. */
internal data class MatchEventRecord(
    val id: String,
    val matchId: String,
    val type: String,
    val gameTimeMs: Long,
    val recordedAtEpochMs: Long,
    val source: String,
    val confidence: Double,
    val validation: String,
    val actorId: String?,
    val payloadJson: String,
    val schemaVersion: Int,
)

private val recordJson = Json { encodeDefaults = true }

internal fun MatchEventRecord.toDomain() = MatchEvent(
    id = MatchEventId(id),
    matchId = MatchSessionId(matchId),
    type = type,
    gameTimeMs = gameTimeMs,
    recordedAtEpochMs = recordedAtEpochMs,
    source = MatchEventSource.valueOf(source),
    confidence = confidence,
    validation = MatchEventValidation.valueOf(validation),
    actorId = actorId,
    payload = recordJson.parseToJsonElement(payloadJson).jsonObject,
    schemaVersion = schemaVersion,
)

internal fun MatchEvent.toRecord() = MatchEventRecord(
    id = id.value,
    matchId = matchId.value,
    type = type,
    gameTimeMs = gameTimeMs,
    recordedAtEpochMs = recordedAtEpochMs,
    source = source.name,
    confidence = confidence,
    validation = validation.name,
    actorId = actorId,
    payloadJson = recordJson.encodeToString(JsonObject.serializer(), payload),
    schemaVersion = schemaVersion,
)
