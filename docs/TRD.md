# Technical Requirements Document (TRD)

**Version:** 0.1.0-draft | **Status:** Sprint 0 baseline

## 1. Technology
- Kotlin; Jetpack Compose + Material 3.
- Gradle Kotlin DSL, version catalog, Gradle wrapper.
- Kotlin Multiplatform shared core; initially Android and JVM tests only.
- Room in Android data adapter; domain interfaces in shared modules.
- kotlinx.serialization for stable interchange contracts.
- Constructor injection initially; add DI only when justified.
- Kotlin test/JUnit; Android instrumentation tests for persistence/UI boundaries.
- GitHub Actions with supported JDK and Gradle caching.

Pin exact toolchain and dependency versions during bootstrap; do not copy
unverified version numbers from this draft.

## 2. Module responsibilities
- shared:domain — pure MatchSession, MatchEvent, GameVersion, HeroId, AbilityId,
  CooldownDefinition; no Android dependencies.
- shared:contracts — serialized event contracts, enums, provenance, schema version.
- shared:engine — deterministic ordering and cooldown calculations; no UI, I/O,
  network, or Android dependencies.
- shared:test-fixtures — synthetic test data only.
- apps/android — Compose screens, navigation, lifecycle, user interaction.
- data:local — Room entities, DAOs, migrations, repository implementations.
- data:game-data — versioned, human-reviewable mechanics definitions.

## 3. Core models
MatchSession: id, startedAtEpochMs, nullable endedAtEpochMs, status
(ACTIVE|PAUSED|COMPLETED), nullable gameVersionId and notes, schemaVersion.

MatchEvent: id, matchId, type, gameTimeMs, recordedAtEpochMs, source
(MANUAL|AUTHORIZED_IMPORT|SYNTHETIC), confidence [0,1], validation
(UNREVIEWED|CONFIRMED|CORRECTED|REJECTED), nullable actorId, versioned payload,
schemaVersion.

CooldownDefinition: gameVersionId, abilityId, durationMs, optional effective
range, provenance, reviewedAtEpochMs.

CooldownEstimate: triggerEventId, durationMs, readyAtGameTimeMs, status
(ESTIMATED|READY_ESTIMATE|INVALIDATED), calculationVersion.

## 4. Event rules
Store match-relative and wall-clock timestamps. Order by gameTimeMs, then
recordedAtEpochMs, then stable event ID. Preserve source/confidence. Corrections
must be auditable. Reject negative timestamps and confidence outside [0,1].
Unknown optional payload fields should be forward-compatible.

## 5. Cooldown calculation
readyAtGameTimeMs = trigger.gameTimeMs + definition.durationMs

Require a matching versioned definition. If missing, return UNAVAILABLE—never
guess. Invalidate/recompute when a trigger is rejected, deleted, or corrected.
UI wording must say “estimated ready”. Test boundaries, large durations,
patch mismatch, and corrections.

## 6. Persistence and privacy
Store MVP sessions locally; migrate every schema change. No telemetry by default.
Export is user-initiated. Do not store screen recordings or game screenshots in MVP.

## 7. Initial Android screens
Home/recent sessions; create session; manual session logger (no overlay);
event timeline; cooldown estimates; session summary; settings/export/privacy.
Support accessibility semantics, scalable text, adequate contrast, touch targets,
and active-session restoration after process death.

## 8. Quality gates
Run ./gradlew test, ./gradlew lint (when configured), and
./gradlew assembleDebug. CI fails on compilation or unit-test failure. Pin
Gradle, JDK, Kotlin, Android Gradle Plugin, and dependencies.