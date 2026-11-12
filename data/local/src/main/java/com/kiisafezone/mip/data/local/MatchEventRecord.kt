package com.kiisafezone.mip.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventSource
import com.kiisafezone.mip.domain.MatchEventValidation
import com.kiisafezone.mip.domain.MatchSessionId
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@Entity(
    tableName = "match_events",
    foreignKeys = [
        ForeignKey(
            entity = MatchSessionRecord::class,
            parentColumns = ["id"],
            childColumns = ["matchId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["matchId", "gameTimeMs", "recordedAtEpochMs", "id"])],
)
internal data class MatchEventRecord(
    @PrimaryKey val id: String,
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

private val recordJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }

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
