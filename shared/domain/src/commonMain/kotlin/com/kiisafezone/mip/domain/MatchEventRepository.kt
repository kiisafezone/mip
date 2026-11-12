package com.kiisafezone.mip.domain

/** Persistence port for events and their correction audit trail. */
interface MatchEventRepository {
    suspend fun getForMatch(matchId: MatchSessionId): List<MatchEvent>
    suspend fun save(event: MatchEvent)
    suspend fun correct(event: MatchEvent, revision: MatchEventRevision)
    suspend fun getRevisions(eventId: MatchEventId): List<MatchEventRevision>
    suspend fun getRevisionsForMatch(matchId: MatchSessionId): List<MatchEventRevision>
    suspend fun delete(eventId: MatchEventId)
}
