package com.kiisafezone.mip.data.local.export

import com.kiisafezone.mip.contracts.MatchExportContract
import com.kiisafezone.mip.domain.MatchEventRepository
import com.kiisafezone.mip.domain.MatchExportRepository
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Creates an explicit, versioned JSON export without accessing unrelated device data. */
class MatchExportService(
    private val sessions: MatchSessionRepository,
    private val events: MatchEventRepository,
    private val json: Json = Json { prettyPrint = true; encodeDefaults = true },
) : MatchExportRepository {
    override suspend fun exportMatch(matchId: MatchSessionId, exportedAtEpochMs: Long): String {
        require(exportedAtEpochMs >= 0)
        val session = requireNotNull(sessions.get(matchId)) { "Match does not exist." }
        val matchEvents = events.getForMatch(matchId)
        val revisions = events.getRevisionsForMatch(matchId)
        return json.encodeToString(
            MatchExportContract(
                exportedAtEpochMs = exportedAtEpochMs,
                session = session,
                events = matchEvents,
                revisions = revisions,
            ),
        )
    }
}
