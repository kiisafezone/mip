package com.kiisafezone.mip.data.local

import android.content.Context
import androidx.room.Room

/** Creates the on-device database; no network or account dependency is involved. */
object MatchDatabaseFactory {
    fun create(context: Context): RoomMatchRepository {
        val database = Room.databaseBuilder(
            context.applicationContext,
            MipDatabase::class.java,
            "mip-matches.db",
        ).build()
        return RoomMatchRepository(database)
    }
}
