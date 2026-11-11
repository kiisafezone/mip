package com.kiisafezone.mip.domain

/** Persistence port owned by the domain boundary. Platform-specific adapters implement it. */
interface MatchSessionRepository {
    suspend fun get(id: MatchSessionId): MatchSession?
    suspend fun getAll(): List<MatchSession>
    suspend fun save(session: MatchSession)
    suspend fun delete(id: MatchSessionId)
}
