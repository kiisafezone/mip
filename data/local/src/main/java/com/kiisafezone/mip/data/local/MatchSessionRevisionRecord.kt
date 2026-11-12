package com.kiisafezone.mip.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionRevision

@Entity(
    tableName = "match_session_revisions",
    foreignKeys = [
        ForeignKey(
            entity = MatchSessionRecord::class,
            parentColumns = ["id"],
            childColumns = ["matchId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["matchId", "changedAtEpochMs"])],
)
internal data class MatchSessionRevisionRecord(
    @PrimaryKey val id: String,
    val matchId: String,
    val beforeSnapshotJson: String,
    val afterSnapshotJson: String,
    val reason: String,
    val changedAtEpochMs: Long,
    val schemaVersion: Int,
)

internal fun MatchSessionRevisionRecord.toDomain() = MatchSessionRevision(
    id = id,
    matchId = MatchSessionId(matchId),
    beforeSnapshotJson = beforeSnapshotJson,
    afterSnapshotJson = afterSnapshotJson,
    reason = reason,
    changedAtEpochMs = changedAtEpochMs,
    schemaVersion = schemaVersion,
)

internal fun MatchSessionRevision.toRecord() = MatchSessionRevisionRecord(
    id = id,
    matchId = matchId.value,
    beforeSnapshotJson = beforeSnapshotJson,
    afterSnapshotJson = afterSnapshotJson,
    reason = reason,
    changedAtEpochMs = changedAtEpochMs,
    schemaVersion = schemaVersion,
)
