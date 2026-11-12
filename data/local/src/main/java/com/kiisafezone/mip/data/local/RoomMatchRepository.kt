package com.kiisafezone.mip.data.local

import androidx.room.withTransaction
import com.kiisafezone.mip.domain.MatchEvent
import com.kiisafezone.mip.domain.MatchEventId
import com.kiisafezone.mip.domain.MatchEventRepository
import com.kiisafezone.mip.domain.MatchEventRevision
import com.kiisafezone.mip.domain.MatchSession
import com.kiisafezone.mip.domain.MatchSessionId
import com.kiisafezone.mip.domain.MatchSessionRepository
import com.kiisafezone.mip.domain.MatchSessionRevision

/** Android Room adapter implementing the shared domain persistence ports. */
class RoomMatchRepository internal constructor(
    private val database: MipDatabase,
) : MatchSessionRepository, MatchEventRepository {
    private val sessions get() = database.matchSessionDao()
    private val events get() = database.matchEventDao()
    private val revisions get() = database.matchEventRevisionDao()
    private val sessionRevisions get() = database.matchSessionRevisionDao()

    override suspend fun get(id: MatchSessionId): MatchSession? =
        sessions.get(id.value)?.toDomain()

    override suspend fun getAll(): List<MatchSession> =
        sessions.getAll().map(MatchSessionRecord::toDomain)

    override suspend fun save(session: MatchSession) {
        sessions.upsert(session.toRecord())
    }

    override suspend fun revise(session: MatchSession, revision: MatchSessionRevision) {
        require(session.id == revision.matchId) {
            "Session revision must refer to the revised match."
        }
        database.withTransaction {
            require(sessions.get(session.id.value) != null) { "Match does not exist." }
            sessions.upsert(session.toRecord())
            sessionRevisions.insert(revision.toRecord())
        }
    }

    override suspend fun getSessionRevisionsForMatch(matchId: MatchSessionId): List<MatchSessionRevision> =
        sessionRevisions.getForMatch(matchId.value).map(MatchSessionRevisionRecord::toDomain)

    override suspend fun delete(id: MatchSessionId) {
        sessions.delete(id.value)
    }

    override suspend fun getForMatch(matchId: MatchSessionId): List<MatchEvent> =
        events.getForMatch(matchId.value).map(MatchEventRecord::toDomain)

    override suspend fun save(event: MatchEvent) {
        events.upsert(event.toRecord())
    }

    override suspend fun correct(event: MatchEvent, revision: MatchEventRevision) {
        require(event.id == revision.eventId && event.matchId == revision.matchId) {
            "Correction revision must refer to the corrected event and match."
        }
        database.withTransaction {
            events.upsert(event.toRecord())
            revisions.insert(revision.toRecord())
        }
    }

    override suspend fun getRevisions(eventId: MatchEventId): List<MatchEventRevision> =
        revisions.getForEvent(eventId.value).map(MatchEventRevisionRecord::toDomain)

    override suspend fun getRevisionsForMatch(matchId: MatchSessionId): List<MatchEventRevision> =
        revisions.getForMatch(matchId.value).map(MatchEventRevisionRecord::toDomain)

    override suspend fun delete(eventId: MatchEventId) {
        events.delete(eventId.value)
    }
}
