# MIP H1 Implementation Review Log

This file records implementation work package by package. It does not replace maintainer review, H1 participant validation, or the phase gate in `PLAN.md`.

## Work package 1 — Domain model and event correction contract

**Commit:** `228e54d1d3d079e7fb8d4fcb58a4cdc227479723`

### Files changed
- `shared/domain/src/commonMain/kotlin/com/kiisafezone/mip/domain/MatchSession.kt`
- `shared/domain/src/commonMain/kotlin/com/kiisafezone/mip/domain/MatchEventRepository.kt`
- `shared/domain/src/commonMain/kotlin/com/kiisafezone/mip/domain/MatchEventRevision.kt`
- `shared/domain/src/commonTest/kotlin/com/kiisafezone/mip/domain/MatchSessionTest.kt`
- `REVIEW.md` (this log)

### Tests run + results
- Not run locally in this execution environment.
- Shared domain tests are scheduled for pull-request CI verification; no pass is claimed yet.

### Assumptions
- Existing `MatchEvent` shape and event type identifiers remain the H1 event contract.
- Match metadata remains optional at the serialization boundary for compatibility; the H1 UI will require hero/role and collect result.
- Event corrections retain immutable before/after snapshots, reason, and correction time.
- No tactical analysis or H2 rule is introduced.

### Known issues
- Build/CI verification is pending.
- Correction history is not complete until local persistence is implemented and verified.

### BUILD_PLAN.md items addressed (not checked)
- §6.1 match record metadata and editability.
- §6.2 event source/time and creation/edit history support.
- §10 correction provenance and traceability.
- §12 domain/persistence separation and deterministic domain tests.

### Decision
- [ ] Approve
- [ ] Reject
- [ ] Change: ____

---


## Work package 2 — On-device Room persistence

**Commit:** `1eefe971bd0dbb539ac9928670aa4cedbd7dc263`

### Files changed
- `data/local/build.gradle.kts`
- `data/local/schema/001_initial.sql`
- `data/local/schemas/.gitkeep`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MatchDaos.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MatchDatabaseFactory.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MatchEventRecord.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MatchEventRevisionRecord.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MatchSessionRecord.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MipDatabase.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/RoomMatchRepository.kt`
- `data/local/src/androidTest/java/com/kiisafezone/mip/data/local/RoomMatchRepositoryTest.kt`
- `gradle/libs.versions.toml`
- `build.gradle.kts`
- `REVIEW.md` (this log)

### Tests run + results
- Instrumentation tests added for persistence/reopen, deterministic event ordering, correction transaction, event deletion, and match cascade deletion.
- Not run locally in this execution environment.
- CI verification was deferred until the complete workflow was wired; no pass is claimed yet.

### Assumptions
- Room is selected because H1 requires durable local persistence and `docs/LOCAL_PERSISTENCE.md` already specifies Room as the Android adapter.
- Database schema starts at version 1 because no Room database has shipped on `main`.
- No destructive migration fallback is enabled.
- No network or game observation is added.

### Known issues
- Generated Room schema JSON still needs to be produced by KSP, inspected, and committed after CI/build.
- Build and instrumentation results are pending.

### BUILD_PLAN.md items addressed (not checked)
- §6.1 local persistence and offline-ready repository boundary.
- §6.2 structured event persistence and deterministic ordering.
- §10 provenance/correction persistence.
- §12 local persistence adapter and separation from shared domain.

### Decision
- [ ] Approve
- [ ] Reject
- [ ] Change: ____

---

## Work package 3 — Android post-match capture and review UI

**Commit:** `af42fac0cd52140def5a63bbc2eb9dcd2979097e`

### Files changed
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchApp.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchSessionViewModel.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchUiState.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/NewMatchScreen.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchListScreen.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchDetailScreen.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchTimelineScreen.kt`
- `apps/android/src/main/java/com/kiisafezone/mip/MainActivity.kt`
- `apps/android/build.gradle.kts`
- `apps/android/presentation/build.gradle.kts`
- `shared/domain/src/commonMain/kotlin/com/kiisafezone/mip/domain/MatchSession.kt`
- `shared/domain/src/commonTest/kotlin/com/kiisafezone/mip/domain/MatchSessionTest.kt`

### Tests run + results
- Not run locally in this execution environment.
- Automated domain, app unit, lint, and Android build checks will be run by pull-request CI after package 4 is submitted.
- No pass is claimed yet.

### Assumptions
- A post-match record is created manually; no live game observation or automated capture is required.
- Hero and role are required. Result must be explicitly selected, including UNKNOWN only when the player genuinely cannot provide it.
- Death, objective/contest, and tactical-note events are the only event types exposed in H1 UI.
- Objective and note details are required; no placeholder event type or timestamp is fabricated.
- Match-relative times are entered as mm:ss. Overflow and invalid values are rejected.
- H1 does not need live session pause/resume/finish controls; the UI presents saved match records and their review timeline.
- Event corrections can change event type, time, and detail, and require a reason.

### Known issues
- No automated Compose/ViewModel UI tests have been added; behavior is covered through domain and repository tests plus CI build/lint.
- Accessibility review still needs human device testing.
- Build/CI results are pending.

### BUILD_PLAN.md items addressed (not checked)
- §6.1 match creation, 3–5 moment guidance, timeline review, edit/delete.
- §6.2 manual event type and match-relative timestamps; explicit handling of unknown times.
- §10 visible player-reported provenance and correction distinction.
- §12 Android-first UI separated from shared domain and local data adapter.

### Decision
- [ ] Approve
- [ ] Reject
- [ ] Change: ____

---

## Work package 4 — Versioned export, documentation, and CI verification wiring

**Commit:** `68fa195b7a06f5155ed144eff306fd02ae697296`

### Files changed
- `shared/domain/src/commonMain/kotlin/com/kiisafezone/mip/domain/MatchExportRepository.kt`
- `shared/contracts/src/commonMain/kotlin/com/kiisafezone/mip/contracts/MatchExportContract.kt`
- `shared/contracts/src/commonTest/kotlin/com/kiisafezone/mip/contracts/MatchExportContractTest.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/export/MatchExportService.kt`
- `data/local/src/test/java/com/kiisafezone/mip/data/local/export/MatchExportServiceTest.kt`
- `docs/EXPORT_FORMAT.md`
- `docs/ARCHITECTURE.md`
- `docs/LOCAL_PERSISTENCE.md`
- `docs/DATA_DICTIONARY.md`
- `.github/workflows/android-ci.yml`

### Tests run + results
- Export contract round-trip test added.
- Export service test added for session, events, revision history, and export timestamp.
- CI workflow now runs shared tests, local-data unit tests, app unit tests, lint, Android assembly, and Room instrumentation tests.
- CI has not yet been triggered for this branch; no pass is claimed.

### Assumptions
- JSON export schema version starts at 1.
- Export includes one selected match, its manually entered events, and event correction history only.
- Events are explicitly sorted by match-relative time, recorded time, and stable ID before serialization.
- The Android document picker is the user-controlled export destination; MIP does not upload exported data.
- Room instrumentation runs on an API 35 Google APIs x86_64 emulator with a 15-minute boot timeout.

### Known issues
- Generated Room schema JSON is not committed yet; KSP output must be generated by CI/build and reviewed before release. The checked-in SQL is a readable companion only.
- Emulator instrumentation may be slow or fail to boot in hosted CI; if it fails, inspect logs and fix the test infrastructure without removing persistence coverage.
- Compose/ViewModel interaction and accessibility still need human-device review.
- H1 participant protocol and outcome remain outside implementation work and must be completed by the maintainer.

### BUILD_PLAN.md items addressed (not checked)
- §6.1 explicit user-requested export and usable documented match data.
- §6.2 traceable event fields and correction history.
- §10 evidence provenance and correction history in export.
- §12 automated verification and offline/local persistence checks.
- §14 recorded change control and separate review/merge authorization.

### Decision
- [ ] Approve
- [ ] Reject
- [ ] Change: ____

---

## Work package 5 — Complete H1 match metadata capture

**Commit:** `34e2251dac026518bcdcac5204e9761e5511e043`

### Files changed
- `shared/domain/src/commonMain/kotlin/com/kiisafezone/mip/domain/MatchSession.kt`
- `shared/domain/src/commonTest/kotlin/com/kiisafezone/mip/domain/MatchSessionTest.kt`
- `data/local/src/main/java/com/kiisafezone/mip/data/local/MatchSessionRecord.kt`
- `data/local/schema/001_initial.sql`
- `data/local/src/androidTest/java/com/kiisafezone/mip/data/local/RoomMatchRepositoryTest.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/NewMatchScreen.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchSessionViewModel.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchApp.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchDetailScreen.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchListScreen.kt`
- `docs/DATA_DICTIONARY.md`
- `docs/LOCAL_PERSISTENCE.md`
- `docs/EXPORT_FORMAT.md`
- `.github/workflows/android-ci.yml`

### Tests run + results
- Added domain validation for non-negative match duration and round-trip coverage for duration.
- Extended Room persistence test fixture to verify patch and duration survive persistence.
- Not run locally; updated pull-request CI will run all tests and upload generated Room schema JSON.
- No pass is claimed yet.

### Assumptions
- Match duration is optional and entered as match-relative mm:ss; unknown duration remains absent.
- Game patch/version is optional and manually entered; no patch is inferred or fetched.
- Duration is stored as milliseconds in the domain and local database.
- Existing Room schema is still version 1 because this database has not shipped; the first schema is being finalized before release.

### Known issues
- Match metadata edits currently update the session record in place; event corrections have before/after audit records, but session metadata does not yet have a separate revision history.
- Generated Room schema JSON and CI results are pending.
- Human-device accessibility review and participant testing remain outstanding.

### BUILD_PLAN.md items addressed (not checked)
- §6.1 hero, role, result, mode, patch, duration, and match edit capture.
- §6.2 match identity and duration/patch metadata; optional unknown fields remain absent.
- §10 provenance boundaries and explicit unavailable/unknown values.
- §12 local persistence, schema, and verification.

### Decision
- [ ] Approve
- [ ] Reject
- [ ] Change: ____

---

## Work package 6 — Input validation hardening

**Commit:** pending

### Files changed
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/MatchDetailScreen.kt`
- `apps/android/presentation/src/main/java/com/kiisafezone/mip/presentation/match/NewMatchScreen.kt`

### Tests run + results
- No new automated test files in this package.
- Added UI-level guards so an event cannot be added without an explicitly entered match-relative time.
- Added UI validation for optional match duration syntax and overflow.
- CI verification is pending; no pass is claimed yet.

### Assumptions
- A blank event timestamp means unavailable and must remain unrecorded; it must not default to `00:00`.
- Optional match duration may remain absent when the player does not know it.
- UI validation complements, but does not replace, domain validation in the ViewModel and model.

### Known issues
- Session metadata edits still do not have a separate before/after revision log; event corrections do.
- Human-device accessibility review remains outstanding.
- CI and generated Room schema review are pending.

### BUILD_PLAN.md items addressed (not checked)
- §6.1 structured key-moment capture with explicit match-relative timestamps.
- §6.2 missing/unknown times and optional duration remain unavailable rather than fabricated.
- §10 explicit unavailable states and evidence integrity.

### Decision
- [ ] Approve
- [ ] Reject
- [ ] Change: ____

---
