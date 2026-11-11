# Data Dictionary

| Field | Type | Meaning | Constraint |
|---|---|---|---|
| id | UUID string | Stable identifier | Unique |
| matchId | UUID string | Parent session | Must exist |
| gameTimeMs | Int64 | Match-relative time | >= 0 |
| recordedAtEpochMs | Int64 | UTC record time | >= 0 |
| source | Enum | Origin | MANUAL, AUTHORIZED_IMPORT, SYNTHETIC |
| confidence | Float | Event confidence | 0.0–1.0 |
| validation | Enum | Review state | UNREVIEWED, CONFIRMED, CORRECTED, REJECTED |
| gameVersionId | String | Patch/data snapshot | Explicit or unknown |
| durationMs | Int64 | Configured cooldown | >= 0 |
| readyAtGameTimeMs | Int64 | Estimated ready time | >= trigger time |

## Initial event types
- SPELL_USED
- HERO_DIED
- OBJECTIVE_OBSERVED
- TACTICAL_NOTE

Payloads are independently versioned and documented. Unknown event types must
not crash timeline rendering; preserve raw metadata and render as unsupported.