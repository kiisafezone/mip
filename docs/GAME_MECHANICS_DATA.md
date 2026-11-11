# Versioned Game Mechanics Data

## Purpose

Mechanics data is an explicit, reviewable input to analysis. Calculation code must
not contain permanent cooldown values or substitute values from another patch.

## Cooldown definition schema (v1)

| Field | Type | Constraint |
|---|---|---|
| schemaVersion | Int32 | >= 1 |
| gameVersionId | String | Exact, nonblank version key |
| abilityId | String | Stable, nonblank ability key |
| durationMs | Int64 | >= 0 |
| provenance.sourceKind | Enum | SYNTHETIC_FIXTURE, USER_AUTHORED, AUTHORIZED_PUBLIC_SOURCE |
| provenance.sourceReference | String | Nonblank source identifier/path |
| provenance.retrievedAtEpochMs | Nullable Int64 | >= 0 |
| provenance.reviewedAtEpochMs | Nullable Int64 | >= 0 |
| provenance.reviewedBy | Nullable String | Nonblank when present |
| provenance.notes | Nullable String | Context and limitations |

The v1 Kotlin contract is CooldownDefinition in shared:domain. The resolver in
shared:engine accepts a nullable game version and returns either an exact
definition or an explicit unavailable reason.

## Missing-data behavior

- Unknown/null game version: GAME_VERSION_UNKNOWN.
- No exact game-version + ability match: DEFINITION_MISSING.
- No fallback to another version, inferred value, or hard-coded default.
- A returned definition remains data with provenance; it is not proof that the
  source is authoritative or current.

## Provenance and review workflow

1. Add a candidate record only from a source permitted by POLICY_AND_COMPLIANCE.md.
2. Record the source kind and stable source reference; retain retrieval/review dates
   when applicable.
3. A reviewer checks the version key, ability key, units, value, and source.
4. Update the affected versioned data file and record the change in the PR.
5. Re-run schema/fixture tests; do not overwrite prior version snapshots silently.
6. Mark unavailable until review is complete. Never fill gaps with guesses.

Synthetic fixtures are for tests only and must not be represented as real game
mechanics. The fixture in data/game-data/fixtures/synthetic-cooldowns.json uses
invented identifiers and values.
