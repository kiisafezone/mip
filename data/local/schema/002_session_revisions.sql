-- Room v2 migration: immutable match metadata revision history.
CREATE TABLE IF NOT EXISTS match_session_revisions (
    id TEXT NOT NULL PRIMARY KEY,
    matchId TEXT NOT NULL,
    beforeSnapshotJson TEXT NOT NULL,
    afterSnapshotJson TEXT NOT NULL,
    reason TEXT NOT NULL,
    changedAtEpochMs INTEGER NOT NULL,
    schemaVersion INTEGER NOT NULL,
    FOREIGN KEY(matchId) REFERENCES match_sessions(id) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS index_match_session_revisions_matchId_changedAtEpochMs
    ON match_session_revisions(matchId, changedAtEpochMs);
