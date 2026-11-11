# Local-First Session Persistence Design

**Status:** schema and lifecycle design; Room wiring is a subsequent implementation.

## 1. Requirements and boundaries

- Persist sessions and manually recorded events on-device; work offline.
- No account, cloud sync, telemetry, or network dependency.
- Keep domain models in shared:domain and Android/Room concerns in data:local.
- Persist event source, confidence, validation, payload, and schema versions.
- Export is user-initiated. Deletion is explicit and local.

## 2. Room schema

### match_sessions

| Column | SQLite type | Rules |
|---|---|---|
| id | TEXT | PRIMARY KEY, NOT NULL |
| startedAtEpochMillis | INTEGER | NOT NULL, >= 0 |
| endedAtEpochMillis | INTEGER | NULL or >= startedAtEpochMillis |
| status | TEXT | NOT NULL; ACTIVE, PAUSED, COMPLETED |
| gameVersionId | TEXT | NULL or non-empty |
| notes | TEXT | NULL |
| schemaVersion | INTEGER | NOT NULL, >= 1 |

Index: index_match_sessions_startedAtEpochMillis on startedAtEpochMillis DESC
for recent-session lookup. Primary-key lookup uses id.

### match_events

| Column | SQLite type | Rules |
|---|---|---|
| id | TEXT | PRIMARY KEY, NOT NULL |
| matchId | TEXT | NOT NULL, FK to match_sessions(id) |
| type | TEXT | NOT NULL |
| gameTimeMs | INTEGER | NOT NULL, >= 0 |
| recordedAtEpochMs | INTEGER | NOT NULL, >= 0 |
| source | TEXT | NOT NULL |
| confidence | REAL | NOT NULL, finite and within [0,1] |
| validation | TEXT | NOT NULL |
| actorId | TEXT | NULL |
| payloadJson | TEXT | NOT NULL JSON object |
| schemaVersion | INTEGER | NOT NULL, >= 1 |

Required composite index:
index_match_events_matchId_gameTimeMs_recordedAtEpochMs_id on
(matchId, gameTimeMs, recordedAtEpochMs, id). This matches deterministic
timeline ordering and scopes reads to one session.

Foreign key: match_events.matchId REFERENCES match_sessions(id) ON DELETE CASCADE.
Deleting a session therefore deletes its events in the same database transaction.
The UI must warn that this deletion is permanent and local.

## 3. Migration policy

- Room database version starts at 1 when Room is introduced.
- Every schema change increments the version and ships an explicit, tested
  Migration; destructive fallback is disabled for user data.
- Migration SQL and a schema export are committed with the change.
- Test migrations from every supported prior schema, including populated rows.
- Do not deploy a production database until the first Room migration and schema
  export are reviewed.

Proposed initial DDL is in data/local/schema/001_initial.sql. It is a design
artifact, not yet wired to a Room database.

## 4. Repository and mapping

The domain MatchSessionRepository remains the port. The Android adapter owns
Room DAO/database classes and implements repository operations. Mapping occurs
at the boundary: domain models are converted to persistence rows before writes
and rows are converted back on reads. Room types never leak into domain.

The current persistence records map all MatchSession fields and all MatchEvent
fields. Event payload is stored as JSON text so unknown event types and unknown
payload keys survive round trips. Enum values are stored by stable name; changing
or removing a persisted enum value requires a migration.

Writes that create a session and its first events, or delete a session, must use
database transactions. Event reads must explicitly order by
gameTimeMs ASC, recordedAtEpochMs ASC, id ASC.

## 5. Active-session recovery after process death

- Persist session status and each event immediately (or in a short bounded
  transaction); do not rely on in-memory state as the source of truth.
- On startup, query ACTIVE and PAUSED sessions.
- If an ACTIVE session was interrupted, present a recovery choice: resume it or
  complete it using an explicit user action. Never silently mark it completed.
- Reconstruct the timeline from persisted events and derive the latest game
  clock position from the latest ordered event; do not invent elapsed game time
  while the app was stopped.
- A session remains recoverable until the user resumes, completes, or deletes it.

## 6. Export and deletion

- Export is initiated by the user and produces a portable versioned document
  containing sessions and events, including provenance/confidence/validation.
- Export must not include unrelated device data or hidden game state.
- Import (if added later) must validate schema versions and preserve unknown
  event types; it must never overwrite local data without explicit confirmation.
- Deletion is local, explicit, and cascades from session to events. No remote
  deletion is required because no cloud copy exists.
- Explain export destination and deletion consequences in the UI before action.

## 7. Implementation gate

Before enabling persistence in a release: add Room entities/DAOs/database,
commit generated schema JSON, implement the repository adapter, add migration
and DAO tests, verify process-death recovery, and run Android build/lint/tests.
