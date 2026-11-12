-- Room v1 schema. Kept as a readable companion to the generated Room schema.
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS match_sessions (
    id TEXT NOT NULL PRIMARY KEY,
    startedAtEpochMillis INTEGER NOT NULL,
    endedAtEpochMillis INTEGER,
    status TEXT NOT NULL,
    gameVersionId TEXT,
    notes TEXT,
    schemaVersion INTEGER NOT NULL,
    heroName TEXT,
    role TEXT,
    result TEXT,
    mode TEXT
);

CREATE INDEX IF NOT EXISTS index_match_sessions_startedAtEpochMillis
    ON match_sessions(startedAtEpochMillis);

CREATE TABLE IF NOT EXISTS match_events (
    id TEXT NOT NULL PRIMARY KEY,
    matchId TEXT NOT NULL,
    type TEXT NOT NULL,
    gameTimeMs INTEGER NOT NULL,
    recordedAtEpochMs INTEGER NOT NULL,
    source TEXT NOT NULL,
    confidence REAL NOT NULL,
    validation TEXT NOT NULL,
    actorId TEXT,
    payloadJson TEXT NOT NULL,
    schemaVersion INTEGER NOT NULL,
    FOREIGN KEY(matchId) REFERENCES match_sessions(id) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS index_match_events_matchId_gameTimeMs_recordedAtEpochMs_id
    ON match_events(matchId, gameTimeMs, recordedAtEpochMs, id);

CREATE TABLE IF NOT EXISTS match_event_revisions (
    id TEXT NOT NULL PRIMARY KEY,
    eventId TEXT NOT NULL,
    matchId TEXT NOT NULL,
    beforeSnapshotJson TEXT NOT NULL,
    afterSnapshotJson TEXT NOT NULL,
    reason TEXT NOT NULL,
    changedAtEpochMs INTEGER NOT NULL,
    schemaVersion INTEGER NOT NULL,
    FOREIGN KEY(matchId) REFERENCES match_sessions(id) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS index_match_event_revisions_eventId_changedAtEpochMs
    ON match_event_revisions(eventId, changedAtEpochMs);

CREATE INDEX IF NOT EXISTS index_match_event_revisions_matchId_changedAtEpochMs
    ON match_event_revisions(matchId, changedAtEpochMs);
