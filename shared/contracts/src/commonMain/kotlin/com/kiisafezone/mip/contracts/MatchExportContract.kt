package com.kiisafezone.mip.contracts

import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchSession
import kotlinx.serialization.Serializable

/** Versioned, portable export envelope for one manually reviewed match. */
@Serializable
data class MatchExportContract(
    override val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val exportedAtEpochMs: Long,
    val session: MatchSession,
    val events: List<MatchEvent>,
    val revisions: List<MatchEventRevision>,
) : VersionedContract {
    init {
        require(schemaVersion > 0)
        require(exportedAtEpochMs >= 0)
    }

    companion object { const val CURRENT_SCHEMA_VERSION: Int = 1 }
}
