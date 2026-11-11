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

## Other fields

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| gameVersionId | String | Patch/data snapshot | Explicit or unknown |
| durationMs | Int64 | Configured cooldown | >= 0 |
| readyAtGameTimeMs | Int64 | Estimated ready time | >= trigger time |

Cooldown definitions and estimates are versioned separately from match event
contracts. Missing or mismatched definitions must produce UNAVAILABLE, never a
guessed duration.
