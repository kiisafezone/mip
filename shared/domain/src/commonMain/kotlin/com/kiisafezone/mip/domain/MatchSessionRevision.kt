package com.kiisafezone.mip.domain

import kotlinx.serialization.Serializable

/** Immutable audit record for a change to manually entered match metadata. */
@Serializable
data class MatchSessionRevision(
    val id: String,
    val matchId: MatchSessionId,
    val beforeSnapshotJson: String,
    val afterSnapshotJson: String,
    val reason: String,
    val changedAtEpochMs: Long,
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
) {
    init {
        require(id.isNotBlank()) { "Revision ID must not be blank." }
        require(beforeSnapshotJson.isNotBlank()) { "Before snapshot must not be blank." }
        require(afterSnapshotJson.isNotBlank()) { "After snapshot must not be blank." }
        require(reason.isNotBlank()) { "Revision reason must not be blank." }
        require(changedAtEpochMs >= 0) { "Revision time must not be negative." }
        require(schemaVersion > 0) { "Revision schema version must be positive." }
    }

    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}
