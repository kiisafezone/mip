-- Proposed Room v1 schema. Design artifact; Room database wiring is not enabled yet.
CREATE TABLE IF NOT EXISTS match_sessions (
    id TEXT NOT NULL PRIMARY KEY,
    startedAtEpochMillis INTEGER NOT NULL CHECK (startedAtEpochMillis >= 0),
    endedAtEpochMillis INTEGER,
    status TEXT NOT NULL CHECK (status IN ('ACTIVE', 'PAUSED', 'COMPLETED')),
    gameVersionId TEXT,
    notes TEXT,
    schemaVersion INTEGER NOT NULL CHECK (schemaVersion >= 1),
    CHECK (endedAtEpochMillis IS NULL OR endedAtEpochMillis >= startedAtEpochMillis),
    CHECK ((status = 'COMPLETED' AND endedAtEpochMillis IS NOT NULL)
        OR (status <> 'COMPLETED' AND endedAtEpochMillis IS NULL))
);

CREATE INDEX IF NOT EXISTS index_match_sessions_startedAtEpochMillis
    ON match_sessions(startedAtEpochMillis DESC);

CREATE TABLE IF NOT EXISTS match_events (
    id TEXT NOT NULL PRIMARY KEY,
    matchId TEXT NOT NULL,
    type TEXT NOT NULL,
    gameTimeMs INTEGER NOT NULL CHECK (gameTimeMs >= 0),
    recordedAtEpochMs INTEGER NOT NULL CHECK (recordedAtEpochMs >= 0),
    source TEXT NOT NULL,
    confidence REAL NOT NULL CHECK (confidence >= 0.0 AND confidence <= 1.0),
    validation TEXT NOT NULL,
    actorId TEXT,
    payloadJson TEXT NOT NULL,
    schemaVersion INTEGER NOT NULL CHECK (schemaVersion >= 1),
    FOREIGN KEY(matchId) REFERENCES match_sessions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS index_match_events_matchId_gameTimeMs_recordedAtEpochMs_id
    ON match_events(matchId, gameTimeMs, recordedAtEpochMs, id);
