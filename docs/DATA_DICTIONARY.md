# Data Dictionary

## MatchSession

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| id | String | Stable session identifier | Nonblank, unique |
| startedAtEpochMillis | Int64 | Session creation/start time | >= 0 |
| endedAtEpochMillis | Nullable Int64 | Completion time | >= start; required when COMPLETED |
| status | Enum | Session lifecycle | ACTIVE, PAUSED, COMPLETED |
| gameVersionId | Nullable String | Explicit game patch/data version | Nonblank when present |
| notes | Nullable String | Player-authored match notes | Optional |
| schemaVersion | Int32 | Serialized session schema | >= 1 |
| heroName | Nullable String | Recorded hero | Nonblank when present |
| role | Nullable String | Recorded role/lane | Nonblank when present |
| result | Nullable Enum | Match outcome | WIN, LOSS, DRAW, UNKNOWN |
| mode | Nullable String | Match mode | Nonblank when present |

## MatchEvent

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| id | String | Stable event identifier | Nonblank, unique |
| matchId | String | Parent session | Nonblank; repository enforces existence |
| type | String | Event type identifier | Nonblank; unknown types are preserved |
| gameTimeMs | Int64 | Match-relative time | >= 0 |
| recordedAtEpochMs | Int64 | Wall-clock record time | >= 0 |
| source | Enum | Origin | MANUAL, AUTHORIZED_IMPORT, SYNTHETIC |
| confidence | Float64 | Entry confidence | finite, 0.0–1.0 |
| validation | Enum | Review state | UNREVIEWED, CONFIRMED, CORRECTED, REJECTED |
| actorId | Nullable String | Optional actor identifier | Nonblank when present |
| payload | JSON object | Event-specific data | Unknown optional keys are preserved |
| schemaVersion | Int32 | Serialized event schema | >= 1 |

## Initial event types and payloads

| Event type | Required payload fields | Meaning |
|---|---|---|
| SPELL_USED | abilityId: nonblank string | Manually logged ability/spell use |
| HERO_DIED | none in schema v1 | Player-reported death; context may be optional |
| OBJECTIVE_OBSERVED | objectiveId: nonblank string | Manually recorded objective/contest |
| TACTICAL_NOTE | text: nonblank string | Player-authored note |

Unknown event type strings are retained. Event payload JSON objects retain
unknown optional keys during serialization.

## MatchEventRevision

| Field | Type | Meaning |
|---|---|---|
| id | String | Stable revision identifier |
| eventId | String | Event being corrected |
| matchId | String | Parent match |
| beforeSnapshotJson | String | Complete event snapshot before correction |
| afterSnapshotJson | String | Complete event snapshot after correction |
| reason | String | Required player-supplied correction reason |
| changedAtEpochMs | Int64 | Correction wall-clock timestamp |
| schemaVersion | Int32 | Revision schema version |

Revisions remain available after an individual event is deleted, until its
parent match is deleted. Deleting a match cascades to its events and revisions.

## Export contract

MatchExportContract includes the export schema version, export timestamp, one
MatchSession, its current events, and all correction revisions for that match.
The export is generated only after an explicit user action.

## Timeline ordering

Events are ordered by gameTimeMs, then recordedAtEpochMs, then stable event ID.

## Future-release mechanics data

CooldownDefinition and DataProvenance are reserved for future timing
intelligence. Release 1 does not calculate cooldown-ready times or provide
tactical conclusions. See docs/GAME_MECHANICS_DATA.md.
