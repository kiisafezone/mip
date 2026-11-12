package com.kiisafezone.mip.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchSessionId

@Entity(
    tableName = "match_event_revisions",
    foreignKeys = [
        ForeignKey(
            entity = MatchSessionRecord::class,
            parentColumns = ["id"],
            childColumns = ["matchId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["eventId", "changedAtEpochMs"]),
        Index(value = ["matchId", "changedAtEpochMs"]),
    ],
)
internal data class MatchEventRevisionRecord(
    @PrimaryKey val id: String,
    val eventId: String,
    val matchId: String,
    val beforeSnapshotJson: String,
    val afterSnapshotJson: String,
    val reason: String,
    val changedAtEpochMs: Long,
    val schemaVersion: Int,
)

internal fun MatchEventRevisionRecord.toDomain() = MatchEventRevision(
    id = id,
    eventId = MatchEventId(eventId),
    matchId = MatchSessionId(matchId),
    beforeSnapshotJson = beforeSnapshotJson,
    afterSnapshotJson = afterSnapshotJson,
    reason = reason,
    changedAtEpochMs = changedAtEpochMs,
    schemaVersion = schemaVersion,
)

internal fun MatchEventRevision.toRecord() = MatchEventRevisionRecord(
    id = id,
    eventId = eventId.value,
    matchId = matchId.value,
    beforeSnapshotJson = beforeSnapshotJson,
    afterSnapshotJson = afterSnapshotJson,
    reason = reason,
    changedAtEpochMs = changedAtEpochMs,
    schemaVersion = schemaVersion,
)
