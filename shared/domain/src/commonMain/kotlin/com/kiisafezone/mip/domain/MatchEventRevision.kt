package com.kiisafezone.mip.domain

import kotlinx.serialization.Serializable

/**
 * Immutable audit record for a correction. Snapshots are versioned JSON strings
 * so future payload keys are not discarded when a correction is recorded.
 */
@Serializable
data class MatchEventRevision(
    val id: String,
    val eventId: MatchEventId,
    val matchId: MatchSessionId,
    val beforeSnapshotJson: String,
    val afterSnapshotJson: String,
    val reason: String,
    val changedAtEpochMs: Long,
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
) {
    init {
        require(id.isNotBlank())
        require(beforeSnapshotJson.isNotBlank())
        require(afterSnapshotJson.isNotBlank())
        require(reason.isNotBlank())
        require(changedAtEpochMs >= 0)
        require(schemaVersion > 0)
    }

    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}
