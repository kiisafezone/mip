package com.kiisafezone.mip.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kiisafezone.mip.domain.MatchResult
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionStatus

@Entity(
    tableName = "match_sessions",
    indices = [Index(value = ["startedAtEpochMillis"])],
)
internal data class MatchSessionRecord(
    @PrimaryKey val id: String,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long?,
    val status: String,
    val gameVersionId: String?,
    val notes: String?,
    val schemaVersion: Int,
    val heroName: String?,
    val role: String?,
    val result: String?,
    val mode: String?,
    val durationMs: Long?,
)

internal fun MatchSessionRecord.toDomain() = MatchSession(
    id = MatchSessionId(id),
    startedAtEpochMillis = startedAtEpochMillis,
    endedAtEpochMillis = endedAtEpochMillis,
    status = MatchSessionStatus.valueOf(status),
    gameVersionId = gameVersionId,
    notes = notes,
    schemaVersion = schemaVersion,
    heroName = heroName,
    role = role,
    result = result?.let { MatchResult.valueOf(it) },
    mode = mode,
    durationMs = durationMs,
)

internal fun MatchSession.toRecord() = MatchSessionRecord(
    id = id.value,
    startedAtEpochMillis = startedAtEpochMillis,
    endedAtEpochMillis = endedAtEpochMillis,
    status = status.name,
    gameVersionId = gameVersionId,
    notes = notes,
    schemaVersion = schemaVersion,
    heroName = heroName,
    role = role,
    result = result?.name,
    mode = mode,
    durationMs = durationMs,
)
