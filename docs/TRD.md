# Technical Requirements Document — Release 1

**Version:** 1.0.0
**Status:** Active implementation specification

## 1. Technology

- Kotlin 2.2.21; Gradle Kotlin DSL; Android Gradle Plugin 8.13.2.
- Jetpack Compose and Material 3 for Android UI.
- Kotlin Multiplatform commonMain domain/contracts/engine with Android and JVM targets.
- Room for Android local persistence; KSP for Room code generation.
- kotlinx.serialization for domain snapshots and versioned JSON export.
- Lifecycle ViewModel and coroutines for presentation state.
- JUnit/Kotlin test for domain and contract tests; Android instrumentation for Room.
- GitHub Actions with JDK 17 and Android SDK Platform 36.

## 2. Module responsibilities

| Module | Responsibility |
|---|---|
| shared:domain | Match/session/event/revision models, invariants, repository ports |
| shared:contracts | Versioned event and export contracts |
| shared:engine | Platform-neutral analysis foundation; no Release 1 tactical analysis |
| apps:android:presentation | Compose screens, UI state, ViewModel |
| data:local | Room entities, DAOs, database, repositories, export adapter |
| apps:android | Android application entry point and dependency composition |

Domain code must not import Android, Compose, Room, network, or storage APIs.

## 3. Domain model

MatchSession includes stable ID, start/end timestamps, lifecycle status,
optional game version and notes, hero, role, result, mode, and schema version.

MatchEvent includes stable ID, parent match, string event type, match-relative
time, recorded wall-clock time, source, confidence, validation status, optional
actor, JSON payload, and schema version.

MatchEventRevision stores immutable before/after JSON snapshots, correction
reason, event/match IDs, correction time, and schema version.

## 4. Event rules

- Reject blank IDs/types and negative timestamps.
- Confidence must be finite and within [0,1].
- Event order is gameTimeMs ASC, recordedAtEpochMs ASC, id ASC.
- Manual entries use MANUAL source and are labeled player-reported in UI.
- Unknown event type strings and optional payload keys remain preserved.
- A correction updates the event and appends a revision in one Room transaction.
- Deleting an event leaves its revisions while the parent match exists.
- Deleting a match cascades to its events and revisions.

## 5. Persistence

Room database v1 contains match_sessions, match_events, and
match_event_revisions. The Android adapter implements shared domain repository
ports. Session/event writes are persisted immediately; match deletion is
cascading and local.

Room schema JSON is exported under data/local/schemas. Every later schema
change increments the database version and includes a tested Migration.
Destructive migration fallback is disabled.

## 6. Export

Export is user initiated through Android's document creation flow. The JSON
envelope includes export schema version, export timestamp, one match, events,
and correction revisions. No unrelated device data or hidden game state is
included.

## 7. Android screens

- Recent match list.
- New/edit match metadata form.
- Match detail and lifecycle controls.
- Manual event entry for death, objective/contest, and tactical note.
- Chronological event timeline with correction and deletion.
- Factual summary and explicit JSON export.

## 8. Quality gates

Run:

~~~sh
./gradlew verifySharedCoreNoAndroidImports
./gradlew :shared:domain:allTests :shared:contracts:allTests :shared:engine:allTests
./gradlew :apps:android:testDebugUnitTest :apps:android:lintDebug assembleDebug
./gradlew :data:local:connectedDebugAndroidTest
~~~

Room tests must cover deterministic ordering, correction transaction/history,
event deletion, and cascading match deletion. Do not claim the Release 1 build
is verified until the CI run completes successfully.
