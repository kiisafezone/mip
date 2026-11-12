package com.kiisafezone.mip.domain

/** Domain-facing port for explicit user-requested match exports. */
interface MatchExportRepository {
    suspend fun exportMatch(matchId: MatchSessionId, exportedAtEpochMs: Long): String
}
