package com.kiisafezone.mip.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Adds immutable match-metadata revision history without discarding existing records. */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `match_session_revisions` (
                `id` TEXT NOT NULL,
                `matchId` TEXT NOT NULL,
                `beforeSnapshotJson` TEXT NOT NULL,
                `afterSnapshotJson` TEXT NOT NULL,
                `reason` TEXT NOT NULL,
                `changedAtEpochMs` INTEGER NOT NULL,
                `schemaVersion` INTEGER NOT NULL,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`matchId`) REFERENCES `match_sessions`(`id`)
                    ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent(),
        )
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_match_session_revisions_matchId_changedAtEpochMs` " +
                "ON `match_session_revisions` (`matchId`, `changedAtEpochMs`)",
        )
    }
}
