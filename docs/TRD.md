# Technical Requirements Document (TRD)

**Document scope:** H1 — Capture  
**Status:** Current implementation reference; technical decisions remain subject
to the root execution plan  
**Execution authority:** [root PLAN.md](../PLAN.md)

## 1. Technology in the current repository

The current implementation uses:

- Kotlin and Gradle Kotlin DSL with the repository version catalog.
- Jetpack Compose and Material 3 for Android UI.
- Kotlin Multiplatform shared source sets for domain/contracts/engine code,
  currently targeting Android and JVM where configured.
- Room and KSP in the Android local-data adapter.
- kotlinx.serialization for domain serialization and versioned JSON export.
- Android Lifecycle ViewModel and coroutines for presentation state.
- Kotlin/JUnit tests for shared logic and Android instrumentation tests for
  Room persistence.
- GitHub Actions with the repository's configured JDK and Android SDK.

The root `PLAN.md` contains an explicit technical decision checkpoint:
compare retaining the existing Kotlin Multiplatform modules with a simpler
Android-only structure using delivery effort, testability, and migration cost.
That decision is not recorded as complete by this document. Do not treat the
existing module layout as proof that KMP is necessary or as authorization to
expand shared modules. Record the evidence-backed decision before expanding
the architecture.

## 2. Current module responsibilities

| Module | Current responsibility |
|---|---|
| `shared:domain` | Match/session/event domain models, invariants, repository ports |
| `shared:contracts` | Versioned event and export boundary contracts |
| `shared:engine` | Platform-neutral analysis foundation; H1 does not authorize cooldown or tactical analysis |
| `apps:android:presentation` | Compose presentation, UI state, and ViewModel |
| `data:local` | Room entities, DAOs, database, local repository adapter, and export adapter |
| `apps:android` | Android application entry point and dependency composition |

Domain code must not import Android, Compose, Room, network, or storage APIs.
The UI must not access Room records or DAOs directly. Keep dependencies directed
toward domain abstractions.

These are descriptions of the current repository layout, not a decision that
every module is required for H1.

## 3. H1 domain data

### MatchSession

The current match-session model includes:

- Stable session ID.
- Start timestamp and nullable end timestamp.
- Lifecycle status enum (`ACTIVE`, `PAUSED`, `COMPLETED`).
- Optional game-version ID and notes.
- Hero, role, result, and mode metadata.
- Schema version.
- Optional `durationMs` match-duration field.

The model validates non-negative start/end timestamps, end time not preceding
start time, positive schema version, nonblank optional metadata, non-negative
duration, and consistency between COMPLETED status and end timestamp.

The presence of lifecycle status values does not make pause/resume/complete
transition controls an H1 requirement. Those methods and associated UI actions
remain deferred pending maintainer decision because they are outside the root
`PLAN.md` H1 “Build only” list.

### MatchEvent

The event model includes a stable ID, parent match ID, event type,
match-relative time, recorded wall-clock time, source, confidence, validation
status, optional actor, payload, and schema version.

H1 event capture is limited to the approved manual event types and must retain
player-reported provenance. Unknown event type strings and optional payload
keys should remain forward-compatible where the current serialization
contracts support them.

### MatchEventRevision

Where event correction history is implemented, a revision stores immutable
before/after snapshots, correction reason, event/match IDs, correction time,
and schema version. Do not conflate event correction history with the separate
session-metadata revision-history requirement.

## 4. Event rules

- Reject blank IDs/types and negative timestamps.
- Confidence must be finite and within [0,1].
- Order timeline events by `gameTimeMs ASC`, `recordedAtEpochMs ASC`, then
  stable event ID.
- Manual H1 entries use the manual source and are labeled player-reported.
- Preserve unknown event type strings and optional payload keys where supported.
- Corrections must preserve the original and corrected event snapshots where
  revision history is supported.
- Do not infer missing match times or objective/contest markers.

## 5. Local persistence

Room is the Android local-persistence adapter. The current Room v1 schema
contains `match_sessions`, `match_events`, and
`match_event_revisions`. The generated schema is maintained under
`data/local/schemas`.

The adapter maps domain models to Room records at the persistence boundary.
Session and event records are stored locally; match deletion cascades to its
events and their correction revisions according to the configured foreign-key
relationships.

Every later schema change must increment the database version and include an
explicit tested Room `Migration`. Destructive migration fallback is disabled;
do not discard user data as a migration strategy. Review and commit generated
schema changes with the corresponding migration.

## 6. Export

Export is explicitly user initiated through Android's document creation flow.
The versioned JSON export contains the selected match and its supported event
data, provenance, and correction history. It must not include unrelated device
data or hidden game state.

## 7. Android H1 screens

The current H1 workflow is organized around:

- Recent match list.
- New/edit match metadata form.
- Match detail.
- Manual event entry for approved H1 event types.
- Chronological event timeline with supported correction/deletion.
- Factual display of recorded metadata and event counts.
- Explicit JSON export.

Do not add cooldown estimates, tactical verdicts, coaching, cross-match
intelligence, live capture, OCR, overlays, or gameplay automation under H1.

## 8. Quality gates

Use the Gradle tasks configured by the repository and CI. The current shared
and Android checks include:

~~~sh
./gradlew verifySharedCoreNoAndroidImports
./gradlew :shared:domain:allTests :shared:contracts:allTests :shared:engine:allTests
./gradlew :apps:android:testDebugUnitTest :apps:android:lintDebug assembleDebug
./gradlew :data:local:connectedDebugAndroidTest
~~~

Room instrumentation tests must cover persistence round trips, deterministic
ordering, correction transaction/history, deletion, and cascade behavior as
applicable to the current implementation. Do not claim H1 is complete merely
because a build or an individual CI job passes; use the root `PLAN.md` DoD and
participant validation gate.
