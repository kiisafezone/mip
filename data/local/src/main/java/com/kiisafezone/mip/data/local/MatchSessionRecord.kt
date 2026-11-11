package com.kiisafezone.mip.data.local

import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId

/** Persistence-shaped record kept outside shared domain. Room wiring is deferred to persistence work. */
internal data class MatchSessionRecord(
    val id: String,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long?,
)
internal fun MatchSessionRecord.toDomain() = MatchSession(
    id = MatchSessionId(id),
    startedAtEpochMillis = startedAtEpochMillis,
    endedAtEpochMillis = endedAtEpochMillis,
)
internal fun MatchSession.toRecord() = MatchSessionRecord(
    id = id.value,
    startedAtEpochMillis = startedAtEpochMillis,
    endedAtEpochMillis = endedAtEpochMillis,
)
