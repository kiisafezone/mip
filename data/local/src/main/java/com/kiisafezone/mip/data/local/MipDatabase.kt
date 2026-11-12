package com.kiisafezone.mip.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MatchSessionRecord::class,
        MatchEventRecord::class,
        MatchEventRevisionRecord::class,
        MatchSessionRevisionRecord::class,
    ],
    version = 2,
    exportSchema = true,
)
internal abstract class MipDatabase : RoomDatabase() {
    abstract fun matchSessionDao(): MatchSessionDao
    abstract fun matchEventDao(): MatchEventDao
    abstract fun matchEventRevisionDao(): MatchEventRevisionDao
    abstract fun matchSessionRevisionDao(): MatchSessionRevisionDao
}
