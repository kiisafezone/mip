package com.kiisafezone.mip.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
internal interface MatchSessionDao {
    @Query("SELECT * FROM match_sessions WHERE id = :id LIMIT 1")
    suspend fun get(id: String): MatchSessionRecord?

    @Query("SELECT * FROM match_sessions ORDER BY startedAtEpochMillis DESC, id ASC")
    suspend fun getAll(): List<MatchSessionRecord>

    // @Upsert updates the parent row without REPLACE's delete-and-reinsert cascade.
    @Upsert
    suspend fun upsert(record: MatchSessionRecord)

    @Query("DELETE FROM match_sessions WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
internal interface MatchEventDao {
    @Query(
        "SELECT * FROM match_events WHERE matchId = :matchId " +
            "ORDER BY gameTimeMs ASC, recordedAtEpochMs ASC, id ASC",
    )
    suspend fun getForMatch(matchId: String): List<MatchEventRecord>

    @Query("SELECT * FROM match_events WHERE id = :id LIMIT 1")
    suspend fun get(id: String): MatchEventRecord?

    @Upsert
    suspend fun upsert(record: MatchEventRecord)

    @Query("DELETE FROM match_events WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
internal interface MatchEventRevisionDao {
    @Query(
        "SELECT * FROM match_event_revisions WHERE eventId = :eventId " +
            "ORDER BY changedAtEpochMs ASC, id ASC",
    )
    suspend fun getForEvent(eventId: String): List<MatchEventRevisionRecord>

    @Query(
        "SELECT * FROM match_event_revisions WHERE matchId = :matchId " +
            "ORDER BY changedAtEpochMs ASC, id ASC",
    )
    suspend fun getForMatch(matchId: String): List<MatchEventRevisionRecord>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: MatchEventRevisionRecord)
}
