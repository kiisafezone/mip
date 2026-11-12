# Local-First Session Persistence

**Status:** Release 1 implementation specification

## Requirements and boundaries

- Persist manually entered sessions, events, and correction revisions on-device.
- Work offline without account, cloud sync, telemetry, or network dependency.
- Keep domain models in shared:domain and Android/Room concerns in data:local.
- Persist event source, confidence, validation, payload, and schema versions.
- Export is user-initiated. Deletion is explicit and local.

## Room schema

Room database version 1 contains:
- `match_sessions`: match identity, timestamps, lifecycle status, hero, role, result, mode, notes, game version, and schema version.
- `match_events`: event identity, owning match, type, match-relative and recorded timestamps, source, confidence, validation, actor, JSON payload, and schema version.
- `match_event_revisions`: immutable before/after JSON snapshots, correction reason, event/match identity, timestamp, and schema version.

Events are ordered by `gameTimeMs ASC, recordedAtEpochMs ASC, id ASC`. Session deletion cascades to its events and correction revisions. Event deletion leaves its correction history available for the lifetime of the parent match.

## Migration policy

- The first Room database ships at version 1.
- Every later schema change increments the version and includes an explicit tested Migration.
- Destructive migration fallback is disabled.
- Room schema JSON is exported under `data/local/schemas` and committed with schema changes.
- Test migrations from every supported prior schema, including populated rows.

## Repository and correction audit

The Room adapter implements shared domain repository ports. Room types never leak into domain. Corrections write the updated event and immutable revision in one database transaction. The revision stores complete JSON snapshots so unknown payload keys are not intentionally discarded.

Malformed known records must fail safely and visibly; do not silently replace them with guessed values.

## Recovery, export, and deletion

- Session status and each event are persisted as changes are made.
- On startup, ACTIVE and PAUSED sessions remain visible and can be resumed or completed explicitly.
- The app never invents elapsed match time while it was stopped.
- Export is initiated by the user and contains versioned match/event data and provenance.
- Export excludes unrelated device data and hidden game state.
- Deletion is local and explicit; the UI explains the consequences before action.

## Implementation gate

Before release, verify Room entities/DAOs/database, generated schema JSON, repository adapter, migration behavior, process-restart recovery, DAO tests, Android build, lint, and CI.
