# Data Dictionary

## MatchSession

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| id | String | Stable session identifier | Nonblank, unique |
| startedAtEpochMillis | Int64 | UTC session start time | >= 0 |
| endedAtEpochMillis | Nullable Int64 | UTC session end time | >= start; required when COMPLETED |
| status | Enum | Session lifecycle | ACTIVE, PAUSED, COMPLETED |
| gameVersionId | Nullable String | Explicit game patch/data snapshot | Nonblank when present |
| notes | Nullable String | User-authored session notes | Optional |
| schemaVersion | Int32 | Serialized session schema | >= 1 |
| heroName | Nullable String | Manually entered hero | Nonblank when present; required by H1 UI |
| role | Nullable String | Manually entered role/lane | Nonblank when present; required by H1 UI |
| result | Nullable Enum | WIN, LOSS, DRAW, UNKNOWN | Explicitly selected in H1 UI |
| mode | Nullable String | Match mode when known | Nonblank when present |
| durationMs | Nullable Int64 | Match duration in milliseconds | >= 0 when present |

## MatchEvent

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| id | String | Stable event identifier | Nonblank, unique |
| matchId | String | Parent session | Nonblank; repository enforces existence |
| type | String | Event type identifier | Nonblank; unknown types are preserved |
| gameTimeMs | Int64 | Match-relative time | >= 0 |
| recordedAtEpochMs | Int64 | UTC record time | >= 0 |
| source | Enum | Origin | MANUAL, AUTHORIZED_IMPORT, SYNTHETIC |
| confidence | Float64 | Event confidence | finite, 0.0–1.0 |
| validation | Enum | Review state | UNREVIEWED, CONFIRMED, CORRECTED, REJECTED |
| actorId | Nullable String | Optional actor identifier | Nonblank when present |
| payload | JSON object | Versioned event-specific data | Unknown optional keys are preserved |
| schemaVersion | Int32 | Serialized event schema | >= 1 |

## Initial event types and payloads

| Event type | Required payload fields | Notes |
|---|---|---|
| SPELL_USED | abilityId: nonblank string | A manually logged ability/spell use |
| HERO_DIED | none in schema v1 | Actor/killer context may be optional payload data |
| OBJECTIVE_OBSERVED | objectiveId: nonblank string | Observation only; does not imply hidden-state tracking |
| TACTICAL_NOTE | text: nonblank string | User-authored note |

Unknown event type strings are retained and reported as unsupported by the v1
payload validator; they must not crash timeline rendering. Event payload JSON
objects retain unknown optional keys during serialization.

## Timeline ordering

Events are ordered by gameTimeMs, then recordedAtEpochMs, then stable event ID.
This produces deterministic order when multiple events share the same match time.

## Cooldown mechanics data

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| gameVersionId | String | Exact game data version | Nonblank; no cross-version fallback |
| abilityId | String | Stable ability key | Nonblank |
| durationMs | Int64 | Configured cooldown | >= 0 |
| provenance | DataProvenance | Source/review trail | Required |
| schemaVersion | Int32 | Mechanics record schema | >= 1 |

A resolver returns unavailable for unknown game version or missing exact
version/ability definitions. It must never guess or use hard-coded defaults.
See docs/GAME_MECHANICS_DATA.md.

## Other fields

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| durationMs | Int64 | Configured cooldown | >= 0 |
| readyAtGameTimeMs | Int64 | Estimated ready time | >= trigger time |


## MatchEventRevision

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| id | String | Stable correction identifier | Nonblank, unique |
| eventId | String | Corrected event identifier | Nonblank |
| matchId | String | Parent match | Nonblank |
| beforeSnapshotJson | String | Full event snapshot before correction | Nonblank JSON |
| afterSnapshotJson | String | Full event snapshot after correction | Nonblank JSON |
| reason | String | Player-provided correction reason | Nonblank |
| changedAtEpochMs | Int64 | Correction time | >= 0 |
| schemaVersion | Int32 | Revision schema | >= 1 |

Correction history is retained when an individual event is deleted and is
removed with its parent match. H1 exports include all revisions for the match.
