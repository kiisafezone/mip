# Local-First Session Persistence Design

> **Current authority notice (2026-11-13):** This document is a current Android Room persistence reference retained to describe the existing repository and its historical H1 work. It is not an independent product-scope, milestone, or implementation authority. The approved source of truth is [PRODUCT_PLAN v1.1.1](PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md). Current status remains **M0 unverified; Sprint 0 complete; M1 not started**. Where this document's older H1 framing conflicts with v1.1.1, v1.1.1 controls. This notice preserves the document's useful technical/history content; it does not resolve open decisions 11.35–11.40 or authorize work.

**Status:** H1 Room implementation baseline; verification and generated-schema review remain release checks.

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
| heroName | TEXT | NULL |
| role | TEXT | NULL |
| result | TEXT | NULL |
| mode | TEXT | NULL |
| durationMs | INTEGER | NULL or >= 0 |
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

The readable SQL companion is in `data/local/schema/001_initial.sql`.
Room entities, DAOs, the database, and `RoomMatchRepository` are implemented
in `data:local`. The generated Room v1 schema is committed under
`data/local/schemas/com.kiisafezone.mip.data.local.MipDatabase/1.json`.
The Room instrumentation/schema evidence is recorded in the WP2/WP8 entries
of `REVIEW.md`; the earlier statement that the SQL was not wired to Room is
superseded and factually stale.

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

The behavior below is a persistence/recovery design requirement, not evidence
that the current application implements the full lifecycle or recovery flow.
M0 lifecycle acceptance is governed by v1.1.1 and remains subject to the
unresolved specifications and measurable checks in decision 11.40. Do not
treat the legacy H1 design text as an independent authorization to expand
the current milestone scope.


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

## 7. H1 implementation and release checks

Room entities, DAOs, database, repository adapter, generated Room v1 schema,
and persistence instrumentation tests are present in `data:local`. WP2/WP8
records the generated schema inspection and successful instrumentation CI
evidence. This verifies the initial persistence package only; it does not prove
all M0 lifecycle, recovery, migration, or product acceptance requirements.
Any future schema change must increment the database version and include an
explicit tested migration; destructive fallback remains disabled.
