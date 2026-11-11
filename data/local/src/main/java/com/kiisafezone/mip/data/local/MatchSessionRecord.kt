package com.kiisafezone.mip.data.local

import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionStatus

/** Room-shaped record; Room annotations/DAO wiring follow schema review. */
internal data class MatchSessionRecord(
    val id: String,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long?,
    val status: String,
    val gameVersionId: String?,
    val notes: String?,
    val schemaVersion: Int,
)

internal fun MatchSessionRecord.toDomain() = MatchSession(
    id = MatchSessionId(id),
    startedAtEpochMillis = startedAtEpochMillis,
    endedAtEpochMillis = endedAtEpochMillis,
    status = MatchSessionStatus.valueOf(status),
    gameVersionId = gameVersionId,
    notes = notes,
    schemaVersion = schemaVersion,
)

internal fun MatchSession.toRecord() = MatchSessionRecord(
    id = id.value,
    startedAtEpochMillis = startedAtEpochMillis,
    endedAtEpochMillis = endedAtEpochMillis,
    status = status.name,
    gameVersionId = gameVersionId,
    notes = notes,
    schemaVersion = schemaVersion,
)
