PRODUCT_PLAN_V.1.1.2_DRAFT

Product: Mobile Legends Intelligence Platform (MIP)
Reference: Mobile Legends Intelligence System: Technical Architecture & MVP Blueprint v1.0
Version: 1.1.2-draft
Status: DRAFT — pending product-owner review; v1.1.1 remains governing
Draft date: 2026-11-13
Current Milestone: M0 status unverified; Sprint 0 complete; M1 not started.
Current authority: PRODUCT_PLAN_V.1.1.1_LOCKED_SOT. This v1.1.2 draft proposes changes to sections 11.6–11.10 only. It does not supersede v1.1.1 unless and until the product owner explicitly approves it. Historical v1.1.0 remains preserved unchanged for traceability.

---

**Draft control:** This document is a proposal for Phase 1 only. Until explicit product-owner approval, v1.1.1 remains the sole governing product plan. Proposed statements in sections 11.6–11.10 are not approved requirements, and unresolved questions must not be filled by implementation assumptions.

1. Mission

Transform observable gameplay information into actionable tactical intelligence, both during a match and after a match.

MIP is a cross-platform MOBA tactical intelligence companion.

Its four product pillars are:

- Awareness: Understand the battlefield.
- Tracking: Monitor events and cooldowns.
- Prediction: Estimate what happens next.
- Analysis: Understand performance and mistakes.

Target Users

- Competitive and ranked players.
- Players learning macro and micro mechanics.
- Players who want to improve map awareness.
- Players who review their matches to identify mistakes.
- Coaches and team analysts.

---

2. Source Hierarchy

The following hierarchy governs this document.

1. PRODUCT_PLAN_V.1.1.1_LOCKED_SOT and its explicit product-owner decisions — the single source of truth for authorized product scope, architecture decisions, priorities, milestones, constraints, and acceptance requirements.
2. Mobile Legends Intelligence System: Technical Architecture & MVP Blueprint v1.0 — the underlying technical and product reference. Its decisions remain authoritative except where an explicit product-owner decision recorded in this locked document overrides a specific item.
3. MIP - FIXED & FINAL PRODUCT PLAN v1.0.0 — complementary reference only. It may identify gaps or conflicts. Only the constraints explicitly adopted in this locked document apply; it does not independently authorize additional product scope.
4. Vision document — context only. It does not override this locked document or authorize additional scope.

Conflict Resolution

- This locked document and its explicit product-owner decisions govern.
- The Blueprint remains the underlying reference except where an explicit decision in this document records a deviation.
- The Room persistence selection is an explicit deviation from the Blueprint's SQLite + SQLDelight selection. No other Blueprint technology decision is changed.
- v1.0.0 requirements apply only where explicitly adopted in this document as constraints.
- Conflicts and unresolved matters must remain documented under Open Questions or Future / Requires Review.
- No conflicting decision may be silently resolved by importing scope from another document.
- Changes require explicit product-owner approval and a new document version.
- This v1.1.1 document is locked and product-owner approved as of 2026-11-13. It supersedes v1.1.0 for current product-plan authority. v1.1.0 is preserved unchanged as the historical baseline.

---

3. Product Boundaries

MIP is an information and coaching companion, not an automated gameplay bot.

The system must not:

- Control the player's character.
- Automatically cast skills or execute attacks.
- Modify game memory.
- Inject code into the game.
- Intercept or manipulate game packets.
- Reveal hidden enemy information that cannot legitimately be observed.

Predictions must be based on observed information and explicitly communicate uncertainty.

The product prioritizes local processing. Gameplay screenshots are sensitive, real-time latency matters, and the app should remain useful without an internet connection.

Adopted Privacy and Data-Control Constraints

The following constraints are adopted from v1.0.0 without introducing additional product features:

- Local-first operation is the initial product baseline.
- Players control what information they enter, correct, retain, and export.
- No undisclosed collection or transmission of match records.
- Export and deletion must behave as documented.

---

4. Architecture

4.1 Overall Architecture

Use a modular monolith initially, with independently replaceable intelligence modules.

Do not start with microservices. The project needs fast iteration, predictable deployment, and manageable complexity.

The architecture consists of:

User Interfaces

- Android App — Live companion. Android is the only first-release platform.
- Desktop App — Replay and analytics. Desktop development begins at M3.
- Shared contracts and synchronization.

Application Core

- Match Manager.
- Event Manager.
- Game Database.
- Session Manager.

Intelligence Engine

- Computer Vision.
- OCR Engine.
- Game Mechanics.
- Cooldown Engine.
- Wave Analysis.
- Prediction Engine.
- Threat Assessment.
- Decision Support.

Data & Infrastructure

- Local Database.
- Replay Storage.
- Model Registry.
- Patch Data.
- Analytics.

4.2 Architecture Layers

Layer| Responsibility
Presentation| Android and desktop interfaces
Acquisition| Screen capture, replay import, manual events
Perception| OCR, object detection, minimap interpretation
Domain| Heroes, skills, items, events, game rules
Intelligence| Tracking, inference and prediction
Persistence| Match history and event storage
Synchronization| Sharing data between devices
Analytics| Post-match reports and statistics

The intelligence engine must be platform-independent. Android and desktop must consume the same event contracts.

4.3 Technology Stack

Component| Technology| Responsibility
Android application| Kotlin + Jetpack Compose| Native UI, permissions, lifecycle and floating companion interface
Desktop application| Tauri + React + TypeScript| Desktop dashboard, replay viewer, visual analytics and configuration
Shared intelligence| Kotlin Multiplatform| Shared domain models, event processing, game mechanics and tracking logic
Computer vision| Python + PyTorch + ONNX Runtime| Model training in Python; optimized inference on Android and desktop
Local persistence| SQLite + Room| Match sessions, event history, game data and offline storage

Persistence decision: Room is the selected local persistence technology, using the repository's existing Room-based work as the baseline. This is an explicit product-owner decision overriding the Blueprint's SQLite + SQLDelight selection for this item only. No other Blueprint technology decision is changed.

Additional technologies:

Technology| Purpose
OpenCV| Image preprocessing
PaddleOCR| Text recognition
YOLO| Hero, minion and object detection
ONNX Runtime| Cross-platform model inference
WebSocket| Optional live desktop synchronization; excluded from the first release
FastAPI| Optional cloud services; excluded from the first release
GitHub Actions| CI/CD and testing

Desktop live screen capture, WebSocket synchronization, and FastAPI cloud services are excluded from the first release.

Important implementation choice: Train and experiment with models in Python, but deploy optimized ONNX models to the applications. Avoid requiring Python to run on the Android device.

4.4 Repository Structure

```
mlbb-intelligence/
│
├── apps/
│   ├── android/
│   │   ├── presentation/
│   │   ├── capture/
│   │   ├── overlay/
│   │   └── permissions/
│   │
│   └── desktop/
│       ├── src/
│       ├── components/
│       ├── replay/
│       └── analytics/
│
├── shared/
│   ├── domain/
│   ├── contracts/
│   ├── events/
│   ├── game-mechanics/
│   ├── cooldown/
│   └── predictions/
│
├── intelligence/
│   ├── perception/
│   │   ├── minimap/
│   │   ├── hero-detection/
│   │   ├── minion-detection/
│   │   └── ocr/
│   │
│   ├── tracking/
│   │   ├── spell/
│   │   ├── ultimate/
│   │   ├── respawn/
│   │   └── objective/
│   │
│   ├── analysis/
│   │   ├── positioning/
│   │   ├── wave-management/
│   │   ├── zoning/
│   │   └── power-spike/
│   │
│   └── decision-engine/
│
├── game-data/
│   ├── heroes/
│   ├── skills/
│   ├── battle-spells/
│   ├── items/
│   ├── monsters/
│   └── patches/
│
├── ml/
│   ├── datasets/
│   ├── training/
│   ├── evaluation/
│   └── exported-models/
│
├── services/
│   ├── synchronization/
│   ├── api/
│   └── analytics/
│
├── tests/
│   ├── unit/
│   ├── integration/
│   ├── vision/
│   └── replay-validation/
│
├── docs/
│   ├── PRD.md
│   ├── TRD.md
│   ├── ARCHITECTURE.md
│   ├── DATA_DICTIONARY.md
│   └── TEST_STRATEGY.md
│
└── README.md
```

The "game-data/patches/" directory must be versioned. Hero cooldowns, item statistics, respawn rules and other mechanics must not be treated as permanent constants.

Repository milestone clarification: `apps/desktop/` represents the planned desktop application whose development begins at M3; it is not authorization to implement desktop functionality before M3. The `services/` directories (`synchronization/`, `api/`, and `analytics/`) are future/optional structural placeholders only. WebSocket synchronization and FastAPI cloud services remain excluded from the first release. Their presence in the repository tree does not authorize implementation.

---

5. Event Log Specification

5.1 Event-Driven Architecture

Rather than having every module continuously calculate everything, create a common event system.

Every event must carry:

Field| Meaning
Source| vision, OCR, manual, or imported replay
Confidence score| Confidence associated with the event
Match identifier| Identifies the associated match
Game timestamp| Time associated with the event in the game
Detection timestamp| Time when the event was detected
Validation status| Indicates the event's validation status

This allows the application to distinguish between something it actually observed and something it merely predicted.

5.2 Event Types

The Blueprint provides the following illustrative Kotlin event model:

```kotlin
sealed interface MatchEvent {
    data class HeroSpotted(
        val heroId: String,
        val x: Float,
        val y: Float,
        val timestamp: Long
    ) : MatchEvent

    data class SpellUsed(
        val heroId: String,
        val spellId: String,
        val timestamp: Long
    ) : MatchEvent

    data class HeroDied(
        val heroId: String,
        val timestamp: Long
    ) : MatchEvent

    data class MinionDetected(
        val lane: String,
        val timestamp: Long
    ) : MatchEvent
}
```

This is illustrative Kotlin, not a complete implementation.

5.3 Event Processing

The acquisition pipeline is:

```
Gameplay Screen / Replay Video
            |
            v
      Frame Acquisition
            |
            v
       Frame Sampling
            |
            v
    Region of Interest (ROI)
            |
       +----+----+
       |         |
       v         v
      OCR     Object Detection
       |         |
       +----+----+
            |
            v
      Event Recognition
            |
            v
       Event Validation
            |
            v
      Match Event Stream
            |
            v
      Intelligence Engine
```

5.4 Event Sources

Three acquisition methods are defined.

A. Android live capture

Android MediaProjection captures the gameplay screen after explicit user consent.

- Capture minimap.
- Read hero status and cooldown indicators.
- Detect visible deaths and kill notifications.
- Recognize minion waves.
- Process screenshots locally.

Proposed clarification — pending product-owner approval: The M2 capture purpose is limited to the capture-session foundation required by the Android companion: user consent, permission handling, and capture lifecycle. M2 does not display captured frames for manual image inspection, analyze frames, run OCR, perform object detection, interpret the minimap, or recognize gameplay events from images. Event updates in M2 remain manual and event-driven only. This clarification does not authorize additional capture use. M2 and M4 remain subject to the authorization and data-rights release gates in this document.

B. Desktop replay ingestion

Import recorded gameplay videos.

- Frame extraction.
- Timeline reconstruction.
- Event detection.
- Match statistics.
- Tactical heatmaps.
- Mistake identification.

Desktop replay analysis begins at M3. Desktop live screen capture is excluded from the first release.

C. Manual event input

A fallback for events computer vision cannot reliably detect.

- Register enemy hero selection.
- Select enemy Battle Spells.
- Record an observed spell usage.
- Confirm enemy death.
- Correct inaccurate detections.

5.5 Data Integrity Rules

- Preserve the distinction between observed information and predictions.
- Every event must retain its source and confidence information.
- Match events must retain their match identifier and timestamps.
- Events must pass through event validation before entering the match event stream.
- Predictions must communicate uncertainty.
- Previously observed enemy positions must not be treated as current positions.

---

6. P0 / P1 / P2 Priorities

The first release is Android only. Desktop development begins at M3. Desktop entries in the priority table remain planned capabilities for the applicable later desktop milestone and are not first-release scope. The exact milestone boundary of the first externally distributed Android build remains unresolved (see proposed decision 11.35). P0 is a priority classification, not permission to implement a feature before its applicable milestone.

P0 — Required for MVP

Feature| Android| Desktop
Match session management| Yes| Yes
Hero selection and setup| Yes| Yes
Manual spell tracking| Yes| Yes
Automatic spell recognition| Experimental| Yes
Enemy death tracking| Yes| Yes
Respawn countdown| Yes| Yes
Objective countdowns| Yes| Yes
Match event timeline| Yes| Yes
Replay import and analysis| —| Yes

Automatic spell recognition and other automatic recognition are not part of M2. Automatic recognition begins in M4.

P1 — First Major Upgrade

- Automatic minimap interpretation.
- Enemy last-known position.
- Missing enemy warnings.
- Minion detection.
- Freeze/slow push/fast push classification.
- Lane pressure analysis.
- Item and level power spike tracking.

P2 — Advanced Intelligence

- Rotation prediction.
- Zoning analysis.
- Positioning evaluation.
- Skillshot prediction.
- Engagement probability.
- Team-fight analysis.
- Personalized tactical recommendations.

---

7. Development Milestones M0–M5

M0–M5 remain the governing development roadmap. Release groupings 1–4 are future grouping only and do not replace or modify this roadmap.

M0 — Foundation

Scope

- Repository setup.
- Architecture and shared contracts.
- Game database schema.
- Match session lifecycle.
- Event processing engine.

Deliverable: A functioning shared application core.

Done when test:

- Repository setup is present.
- Architecture and shared contracts are established.
- The game database schema exists.
- Match sessions can complete their lifecycle.
- The event processing engine functions.

Current status: M0 status unverified; Sprint 0 complete; M1 not started. M0 must not be marked complete until all M0 acceptance criteria are verified with evidence.

M1 — Tracking Engine

Scope

- Battle Spell tracking.
- Ultimate tracking.
- Enemy death events.
- Respawn calculations.
- Objective timers.

Deliverable: Reliable manual and event-driven tracking.

Done when test:

- Battle Spell and ultimate tracking operate.
- Enemy death events can be recorded.
- Respawn calculations operate from death events.
- Objective timers operate.
- Manual and event-driven tracking can be demonstrated.

M2 — Android Companion

Scope

- Screen capture.
- Permissions.
- Floating interface.
- Live event updates.
- Local persistence.

Deliverable: First usable in-match companion.

M2 operating boundary:

- M2 supports manual and event-driven updates only.
- Automatic recognition does not begin in M2; it begins in M4.
- The floating interface is information-only. It must not control gameplay or execute gameplay actions.
- M2 is blocked from release until the written-authorization and data-rights review gates have passed.

Done when test:

- Screen capture operates after the required permissions are granted.
- The floating interface operates.
- Live event updates are visible.
- Match information is persisted locally.

M3 — Desktop Analysis

Scope (proposed clarification; pending product-owner approval)

- Replay ingestion.
- Frame extraction.
- Manual reconstruction from player-entered or imported timeline events.
- Timeline viewer.
- Match reports.

Automatic event detection from replay video (including OCR, object detection, or other computer-vision recognition) begins in M4, not M3.

Deliverable: Desktop analysis application.

Desktop development begins at M3. Desktop live screen capture, WebSocket synchronization, and FastAPI cloud services are excluded from the first release.

Done when test (subject to approval of the M3 reconstruction boundary):

- A recorded gameplay video can be imported.
- Frames can be extracted.
- Manual or imported timeline events can be associated with the replay.
- A timeline can be viewed.
- Match reports can be produced.

M4 — Computer Vision

Scope

- OCR.
- Minimap detection.
- Hero detection.
- Minion detection.
- Automatic event recognition.

Deliverable: Reduced dependence on manual inputs.

M4 is blocked from release until the written-authorization and data-rights review gates have passed.

Done when test:

- OCR operates on supported samples.
- Minimap detection operates.
- Hero detection operates.
- Minion detection operates.
- Automatic event recognition is demonstrated.

M5 — Tactical Intelligence

Scope

- Map awareness.
- Wave management.
- Threat assessment.
- Power spike detection.
- Positioning and zoning.
- Skillshot prediction.

Deliverable: Full tactical intelligence companion.

M5 is blocked until M4 meets its applicable acceptance targets.

Done when test:

- Map-awareness analysis operates.
- Wave-management analysis operates.
- Threat assessment operates.
- Power-spike detection operates.
- Positioning and zoning analysis operate.
- Skillshot prediction operates.

---

8. Acceptance Criteria

The following are proposed engineering targets, not measured performance claims.

Component| Initial acceptance target
Manual event tracking| No lost confirmed events
Cooldown calculation| Correct against configured game data
Respawn timer| Correct when death event is correctly detected
Replay timeline| Events synchronized to video within 1 second
OCR| At least 95% accuracy on supported UI samples
Hero detection| At least 90% precision on supported test samples
Android responsiveness| No material gameplay interruption
Offline operation| Core tracking works without network

For automatic recognition, measure precision and recall separately.

A false enemy death notification can be worse than no notification.

Intelligence Quality Metrics

Metric| What it tells us
Detection precision| How often alerts are correct
Detection recall| How many observable events are found
Event latency| How quickly an event is recognized
Cooldown error| Difference from actual availability
Position error| Difference between predicted and observed location
Prediction calibration| Whether confidence scores are trustworthy
Battery usage| Cost of live analysis
FPS impact| Whether gameplay is affected

A prediction engine that says it is 90% confident should actually be correct approximately 90% of the time across appropriately evaluated predictions.

8.1 Adopted Definition of Done Constraints

The following v1.0.0 Definition of Done requirements are adopted as constraints. They do not authorize additional product features or change the M0–M5 roadmap.

Product-level completion constraints

- Complete end-to-end workflows.
- Local data survives application restart.
- Correct interrupted-session recovery.
- Tested database migrations.
- Documented export and deletion.
- Invalid input must not silently corrupt records.
- Unit, integration, and UI tests.
- Successful Android build and CI.
- No unresolved critical or high-severity release-blocking defects.
- Verified accessibility.
- Accurate user and developer documentation.
- Reproducible release builds.

Issue-level completion constraints

Every feature or issue must have:

- Acceptance criteria.
- Relevant tests.
- Required documentation.
- Code review.
- CI results.
- Verifiable evidence.

A merged PR or passing CI alone does not prove product completion.

The milestone-specific "Done when" tests and engineering acceptance targets in the Blueprint remain applicable alongside these adopted completion constraints.

8.2 Cooldown Data Integrity Constraint

Unknown or unverified cooldown mechanics must be reported as unavailable rather than guessed. A cooldown result must not present an unsupported value as confirmed. This constraint does not expand the cooldown feature scope or change the applicable milestone.

---

9. Risks

Risk| Severity| Mitigation
Game UI changes| High| Versioned UI profiles
OCR mistakes| High| Confidence thresholds and manual correction
Minimap ambiguity| High| Temporal tracking and uncertainty
Android performance| High| Frame sampling and optimized inference
Battery consumption| Medium| Adaptive processing frequency
Game compatibility| High| Policy review and supported-device testing
Patch changes| High| Versioned game mechanics
False predictions| High| Separate observations from predictions

Additional Engineering Considerations

- Android overlay compatibility must be tested.
- Test MediaProjection, foreground-service requirements, orientation changes, capture interruptions and overlay behavior on the actual device.
- Review Mobile Legends' current terms and anti-cheat rules before distributing an overlay or automated screen-analysis companion.
- Technical permission from Android does not automatically mean a feature is permitted by the game publisher.

9.1 M2 and M4 Authorization and Data-Rights Release Gates

Written authorization and data-rights review are mandatory release gates for M2 and M4.

- The applicable written authorization must be obtained before the corresponding M2 or M4 functionality is released.
- Data-rights review must be completed for the applicable acquisition methods and data used.
- Authorization for one acquisition method does not automatically authorize another.
- Applicable policy review must be completed.
- Nothing in M2 or M4 ships until both the written-authorization and data-rights review gates have passed.

These gates apply in addition to the Blueprint's stated requirement to review Mobile Legends' current terms and anti-cheat rules before distributing an overlay or automated screen-analysis companion.

---

10. Scope Guard

This document is governed by PRODUCT_PLAN_V.1.1.1_LOCKED_SOT and its explicit product-owner decisions. The Blueprint remains the underlying reference except where this locked document records an explicit decision.

A proposed feature or change may enter the plan only when:

1. It reads or writes an event in the match event log.
2. It belongs to the currently authorized milestone.
3. It does not violate the product boundaries.
4. It does not introduce scope from the complementary v1.0.0 plan or the vision document.

A feature that does not satisfy these conditions is outside the authorized scope of this plan.

Priority and Milestone Separation

P0, P1 and P2 define feature priorities.

M0–M5 define the development roadmap.

A priority designation does not independently authorize implementation ahead of its applicable milestone.

No milestone may be skipped or silently treated as completed.

Change Control

- New scope requires explicit review.
- Conflicts must be recorded under Open Questions.
- This locked document governs. The Blueprint remains authoritative for underlying decisions unless this document records an explicit product-owner deviation.
- No unapproved feature may be introduced through implementation details, architecture changes, or documentation updates.
- Product-plan, release-scope, requirements, and Definition of Done changes require approval before implementation resumes.
- Issues must derive from approved product requirements.
- PRs must reference their issues, describe changes, provide test evidence, and identify limitations.
- No PR may be merged without explicit maintainer approval.
- Releases must pass their applicable Definition of Done and release gates.
- Material changes require documented review and approval.
- Changes to this document require a new version number and explicit product-owner approval.

---

11. Open Questions and Recorded Decisions

The following records preserve the original conflicts, gaps, and ambiguities together with their product-owner decisions. A recorded decision applies only to the extent stated; it does not silently resolve unrelated technical details.

Interpretation and traceability: Historical “Decision required” paragraphs are retained to preserve the original question and decision history. Where an explicit “Recorded decision” is followed by “Status: Resolved,” that recorded decision governs the matter it expressly resolves; the earlier decision-required wording does not reopen or override it. Any unresolved detail remains open. This clarification applies to 11.14, 11.16, 11.20, 11.28, 11.30, 11.33, and 11.34. The explicit source hierarchy in Section 2 governs over the historical Blueprint-as-single-source-of-truth wording in 11.13.

11.1 Android Live Capture in M2 vs. Manual-First Tracking

Blueprint statements:

- M1 establishes reliable manual and event-driven tracking.
- M2 introduces Android screen capture, permissions, floating interface, live event updates, and local persistence.
- Android live capture is an acquisition method.
- P0 includes experimental automatic spell recognition.

Ambiguity:

The Blueprint does not explicitly establish whether M2 live capture is initially limited to acquiring screenshots and updating manually supported events, or whether automatic recognition is expected to become operational during M2.

Decision required:

Clarify the boundary between manual-first M1 tracking and automatic recognition introduced by live capture.

Recorded decision: M2 supports manual and event-driven updates only. Automatic recognition starts in M4, not M2. M2 remains subject to its written-authorization and data-rights release gates.

Status: Resolved.

11.2 Skillshot, Zoning and Positioning in M5

Blueprint statements:

- Skillshot prediction is P2.
- Zoning analysis is P2.
- Positioning evaluation is P2.
- M5 includes positioning and zoning and skillshot prediction.

Ambiguity:

The Blueprint places these capabilities in the advanced-priority category and M5 but does not define their individual technical acceptance thresholds, required input quality, or validation datasets.

Decision required:

Define what constitutes successful M5 implementation for each capability without expanding beyond the stated feature scope.

Recorded decision: M5 is blocked until M4 meets its applicable acceptance targets. This decision establishes the M4 dependency and does not independently define individual technical acceptance thresholds for each M5 capability.

Status: M4 dependency recorded; capability-specific acceptance thresholds remain to be defined within the applicable M5 specification.

11.3 Desktop and Kotlin Multiplatform Scope for the First Release

Blueprint statements:

- The product is cross-platform.
- Android and desktop share one intelligence engine and event contracts.
- Desktop uses Tauri + React + TypeScript.
- Shared intelligence uses Kotlin Multiplatform.
- M3 introduces desktop replay analysis.
- The final technical recommendation includes Android + Windows/Desktop.

Ambiguity:

The Blueprint does not clearly define whether both complete applications and Kotlin Multiplatform integration are mandatory for the first release, or whether the desktop application is introduced incrementally at M3.

Decision required:

Confirm the first-release platform boundary and the required extent of shared Kotlin Multiplatform functionality.

Recorded decision: The first release is Android only. Desktop development begins at M3. The Blueprint's shared intelligence and event-contract direction remains unchanged.

Status: Resolved.

11.4 Blueprint's Current-Milestone Start Point

Blueprint statements:

- M0 is the foundation milestone.
- The recommendation explicitly says: start with M0, not computer vision.
- The roadmap proceeds from M0 through M5.

Ambiguity:

The Blueprint establishes M0 as the recommended starting point but does not identify the current implementation state of the existing MIP repository or establish whether M0 has already been completed.

Decision required:

Determine the repository's verified milestone status before selecting the next implementation milestone. Do not assume M0 is incomplete or complete without evidence.

Recorded decision: Current repository status is: "M0 status unverified; Sprint 0 complete; M1 not started." Do not mark M0 complete until all M0 acceptance criteria are verified with evidence.

Status: Recorded. M0 completion remains unverified.

11.5 Desktop Live Screen Capture

Blueprint statements:

- The Android live-capture method specifies Android MediaProjection.
- The Android and desktop responsibility table marks live screen capture as Yes on both platforms.
- Desktop replay ingestion is specified separately.

Ambiguity:

The desktop live-capture mechanism and its relationship to replay ingestion are not specified.

Decision required:

Clarify what desktop live screen capture means and whether it is part of the first desktop release.

Recorded decision: Desktop live screen capture is excluded from the first release. Desktop development at M3 is for the specified replay-analysis scope.

Status: First-release boundary resolved. No desktop live-capture mechanism is authorized for the first release.

11.6 Event Confidence Representation

Blueprint requirement:

Every event carries a confidence score.

Proposed M0 specification:

- Every event confidence value is a finite normalized number in the inclusive range [0.0, 1.0].
- The confidence value is metadata about the support for the event claim. It is distinct from event validation status; a high score does not confirm an event, and a confirmed event does not imply a score of 1.0.
- Unless a calibration method and evaluation evidence are approved, the score must not be described as a probability or as a measured likelihood that the event is correct.
- Confidence must be preserved unchanged across domain, persistence, and event-contract serialization.
- M0 does not require a computer-vision confidence model or calibration dataset. Automatic recognition remains outside M0.
- No confidence threshold may silently discard, confirm, or promote an event.

Current-repository alignment:

The current MatchEvent model and MatchEventContract require a finite Double in [0.0, 1.0]. The Data Dictionary describes the same range. This establishes representation and range only; it does not settle the score's meaning or manual-entry behavior.

Assumptions used by this proposal:

- Retain the existing numeric range and finite-value constraint.
- Keep confidence independent from the validation lifecycle.
- Do not introduce automated recognition or probability claims into M0.

Open questions for product-owner decision:

- Q11.6-A: Should confidence mean (a) the source's subjective certainty, (b) an evidence-strength score, or (c) a calibrated probability that the event claim is correct?
- Q11.6-B: For MANUAL events, should confidence be entered by the user, assigned a defined fixed value, or be represented as unavailable? The current non-null numeric field does not represent unavailable.
- Q11.6-C: For an import source that provides no confidence value, should the event be rejected, assigned a defined neutral value, or should the contract allow null?
- Q11.6-D: Are any numeric thresholds required in M0, or should thresholds be deferred until a specific consumer and evaluation method are approved?

Status: Proposed specification; pending answers to Q11.6-A through Q11.6-D.

11.7 Event Timestamp Semantics

Blueprint requirement:

Every event carries a game timestamp and a detection timestamp.

Proposed M0 specification:

- gameTimeMs represents elapsed match-clock time in milliseconds from the match's defined in-game time origin (00:00). It is not elapsed time since the MIP session record was created.
- recordedAtEpochMs represents the UTC Unix epoch time in milliseconds at which MIP records the event. It must not be silently treated as the time the gameplay event occurred or the time an automated detector observed it.
- If the product requires a distinct detection/observation timestamp, it must be represented separately from recordedAtEpochMs. A manual entry's record time is not proof of the time the player observed the event.
- Timestamps must be non-negative when present. The system must not invent, interpolate, or extrapolate an unknown game time.
- An interruption or gap in capture must remain a gap. The system must not synthesize events or timestamps for the unobserved interval.
- A replay-relative timestamp mapping is not specified here. Any M3 replay-ingestion mapping remains subject to the M3 scope and acquisition decisions; this section does not authorize replay ingestion.
- If a source clock resets, pauses, or cannot be mapped reliably to the match clock, the affected game time must be marked unavailable rather than guessed.

Current-repository alignment:

The current model requires non-null gameTimeMs and recordedAtEpochMs Long values. The Data Dictionary describes gameTimeMs as match-relative and recordedAtEpochMs as UTC record time. The historical H1 PRD says unknown event times remain unknown. The Blueprint separately calls for a detection timestamp. These statements do not fully align with the current non-null model or establish whether a third timestamp is needed.

Assumptions used by this proposal:

- Millisecond units are retained.
- Match-clock time and MIP record time are separate concepts.
- Unknown times must not be fabricated.
- M0 does not define M3 replay synchronization or authorize M2 capture.

Open questions for product-owner decision:

- Q11.7-A: Should gameTimeMs become nullable so events with unknown match time can be retained without inventing a value, or must an event without a known game time be held outside the MatchEvent contract?
- Q11.7-B: Should the contract add a separate detectedAtEpochMs field, nullable for manual events, while retaining recordedAtEpochMs as the MIP record-creation time?
- Q11.7-C: For manual events, is recordedAtEpochMs the time the user submits the event, while gameTimeMs is the user's estimate of when it occurred?
- Q11.7-D: What is the required behavior if the game clock pauses, resets, or is unavailable? Is preserving a gap and marking time unavailable sufficient?
- Q11.7-E: Should replay timestamp mapping remain wholly deferred to M3, or must the M0 contract reserve explicit source-clock/origin metadata now?

Status: Proposed specification; pending answers to Q11.7-A through Q11.7-E.

11.8 Event Validation Status

Blueprint requirement:

Every event carries a validation status, and events must pass through event validation before entering the match event stream.

Proposed M0 specification:

- Structural/payload validation and human review status are separate concepts.
- Structural validation determines whether the event envelope and its type-specific payload satisfy the registered contract. An invalid payload must not be treated as a valid event. An unknown event type may be preserved for forward compatibility but must be reported as unsupported and must not be processed as a known event type.
- The review status records whether a person or an explicitly authorized validation process has reviewed the event. It must not be inferred from structural validity or confidence.
- Proposed review-state meanings:
  - UNREVIEWED: recorded but not explicitly reviewed.
  - CONFIRMED: explicitly reviewed and accepted as a supported event.
  - CORRECTED: event content has been changed and is awaiting confirmation of the corrected version; the change must have an immutable before/after revision and reason.
  - REJECTED: explicitly reviewed and rejected as an event that should not be treated as a confirmed match fact.
- Proposed transitions:
  - UNREVIEWED -> CONFIRMED, CORRECTED, or REJECTED.
  - CONFIRMED -> CORRECTED or REJECTED.
  - CORRECTED -> CONFIRMED, CORRECTED (another revision), or REJECTED.
  - REJECTED is terminal unless the product owner explicitly approves a reopen transition.
- A correction must not erase the prior event value or its revision history.
- The event-processing boundary must define which structurally valid review states it accepts. A rejected event must not be presented as a confirmed fact.

Current-repository alignment:

The current domain enum contains UNREVIEWED, CONFIRMED, CORRECTED, and REJECTED. A separate payload validator returns VALID, INVALID, or UNSUPPORTED_TYPE. Event revisions are modeled separately. The enum exists, but its meanings, transition rules, rejected-event behavior, and event-engine eligibility are not defined by the current contract.

Assumptions used by this proposal:

- Retain the four existing review-state values.
- Keep structural validation separate from human review.
- Treat CORRECTED as a corrected revision awaiting confirmation, rather than proof that the correction is factually correct.
- Preserve rejected records for audit rather than silently deleting them.

Open questions for product-owner decision:

- Q11.8-A: Do you approve the proposed meanings of UNREVIEWED, CONFIRMED, CORRECTED, and REJECTED?
- Q11.8-B: Should REJECTED be terminal, or may a rejected event be reopened for correction/review?
- Q11.8-C: May the event-processing engine consume structurally valid UNREVIEWED events, or only CONFIRMED events? Should CORRECTED events be excluded until re-confirmed?
- Q11.8-D: Does "entering the match event stream" mean persistence, display in the timeline, or eligibility for analysis? These may need separate rules.
- Q11.8-E: Should structural INVALID events be rejected before persistence, or retained in a separate error/audit path?

Status: Proposed specification; pending answers to Q11.8-A through Q11.8-E.

11.9 Event Schema Completeness

Blueprint requirement:

The event model must support the required event types and preserve the Blueprint's common event fields and source distinctions. The Blueprint's HeroSpotted, SpellUsed, HeroDied, and MinionDetected examples are illustrative, not complete contracts.

Proposed M0 specification:

- The common event envelope must define, at minimum: stable event ID, match ID, event type, game-relative timestamp semantics, record/detection timestamp semantics as resolved under 11.7, source, confidence as resolved under 11.6, validation status as resolved under 11.8, actor/subject identity where applicable, versioned type-specific payload, and event schema version.
- Each registered event type must have a versioned payload contract defining required fields, optional fields, units, identifier semantics, allowed values, source constraints, and structural validation rules.
- Unknown event types and unknown optional payload fields must be handled according to an explicit forward-compatibility rule; they must not be silently reinterpreted as a known event.
- M0 may define the generic event envelope, registry, and schema-validation boundary. Definition of M1 tracking payloads in this document does not authorize M1 tracking implementation before M1.
- The M1 P0 tracking requirements identified by the roadmap must have explicit contracts before their implementation: Battle Spell use, ultimate use, hero death, and objective events sufficient to establish objective timer anchors. The contract must distinguish an observed event from a derived countdown.
- Respawn and objective countdown values are derived outputs, not raw observations. Their derivation requires the relevant confirmed event, versioned game rules, and explicit handling of missing/unverified mechanics.
- Event contracts for replay-derived or automatically recognized events must preserve their actual source and must not be treated as authorized merely because the schema supports them.

Current-repository alignment:

The current event type registry contains SPELL_USED, HERO_DIED, OBJECTIVE_OBSERVED, and TACTICAL_NOTE. The SPELL_USED payload currently requires abilityId; HERO_DIED has no mandatory payload fields; OBJECTIVE_OBSERVED requires objectiveId; TACTICAL_NOTE requires text. The existing contracts do not yet establish the complete M1 P0 event semantics for Battle Spell versus ultimate use, hero/side identity, objective lifecycle/anchor events, or derived countdown inputs.

Assumptions used by this proposal:

- Keep the generic event envelope separate from type-specific payloads.
- Treat M1 tracking event contracts as specification dependencies that can be defined without authorizing M1 implementation.
- Treat respawn/objective countdowns as derived values, not raw event types.
- Keep M3 replay and M4 automatic recognition source boundaries intact.

Open questions for product-owner decision:

- Q11.9-A: Should M0 define only the generic envelope and validation framework, with M1 event payloads specified later, or should this M0 specification also define the payload contracts for M1 P0 tracking?
- Q11.9-B: Should Battle Spell use and Ultimate use be separate event types, or one SPELL_USED type with an explicit ability category?
- Q11.9-C: For HERO_DIED, which identity is required: hero only, actor/team side, player/enemy classification, killer, or some subset? Which values may be unknown?
- Q11.9-D: Which objective event(s) are authoritative timer anchors (for example, spawn observed, objective defeated/secured, or another explicit observation)? Which objective categories are in scope?
- Q11.9-E: Are HeroSpotted and MinionDetected excluded from M0/M1 contracts because they belong to later perception milestones, or must the generic schema reserve them now?
- Q11.9-F: Should TACTICAL_NOTE remain in the initial event registry, or is it outside the required M0 event contract?
- Q11.9-G: Which event sources are permitted for each M0/M1 event type? Manual-only and synthetic fixtures are distinct from authorized imports, replay-derived events, and automated recognition.

Status: Proposed specification; pending answers to Q11.9-A through Q11.9-G.

11.10 Game Data and Patch Versioning

Blueprint requirement:

The game database covers heroes, skills, items, and game rules. Patch-specific data must be versioned, and calculations must use the applicable patch, equipment, talents, and cooldown modifiers where relevant.

Proposed M0 specification:

- M0 defines a versioned game-data schema and provenance/verification workflow. A schema definition is not evidence that a production dataset exists or that its values are verified.
- Every production mechanics record must identify its exact gameVersionId/patch, schema version, stable entity/ability/item/rule identifier, value and unit, source provenance, effective patch/version scope, retrieval or publication reference, review status, and reviewer/review date where applicable.
- Game-data snapshots are immutable once approved for use. A patch update creates a new versioned snapshot; it must not silently overwrite the prior patch's values.
- Production data may be added only after the source, terms, license/permission, rights to store/display/use/redistribute, and applicable policy requirements have been reviewed and recorded.
- Synthetic fixtures are test-only and must be clearly identified. They must never be represented as real MLBB mechanics or used as production fallback values.
- Missing, stale, unreviewed, conflicting, or unverified values must be reported as unavailable. No cross-patch fallback, inferred value, or hard-coded substitute is permitted.
- A data update must include source evidence, schema validation, review of units and identifiers, regression tests for the affected records, and a reviewable change record.
- This section does not authorize scraping, automated extraction, use of protected game assets, or any acquisition method blocked by POLICY_AND_COMPLIANCE.md.
- The exact dataset required by M0 must be limited to the approved M0 schema/acceptance scope. Later feature data must not be pulled into M0 merely because the schema can represent it.

Current-repository alignment:

The repository has a CooldownDefinition schema with exact gameVersionId/abilityId matching and provenance fields, a resolver that returns unavailable for missing definitions, and a synthetic-only fixture. The current fixture is not a verified production dataset. The broader hero, skill, item, and game-rule schema and the authoritative source/update workflow are not established by that fixture.

Assumptions used by this proposal:

- Preserve exact-version lookup and fail-closed missing-data behavior.
- Treat synthetic data as test-only.
- Require provenance and rights review before production use.
- Do not assume an official/public data source is authorized for every use.

Open questions for product-owner decision:

- Q11.10-A: Does M0 require only the complete versioned game-data schema and update/verification workflow, or also a populated, verified dataset for a named patch?
- Q11.10-B: Which game-data entities and fields are required in the M0 schema: heroes, skills/abilities, Battle Spells, items, monsters/objectives, respawn rules, cooldown rules, or another explicit subset?
- Q11.10-C: Which source(s) may be used for production data, and what evidence is sufficient to establish the required rights and permitted uses?
- Q11.10-D: What is the canonical gameVersionId/patch naming rule, and how should hotfixes or mid-patch balance changes be represented?
- Q11.10-E: Who may review/approve a data snapshot, and what makes data stale or invalidated?
- Q11.10-F: What update cadence or trigger is required (official patch, hotfix, manual review, or another event)?
- Q11.10-G: Should M0 define schemas for all game-data categories while populating only the minimum data required for M1, or should both schema and population be restricted to a narrower M0 subset?

Status: Proposed specification; pending answers to Q11.10-A through Q11.10-G.

11.11 Optional Synchronization and Cloud Services

Blueprint statements:

- WebSocket is optional for live desktop synchronization.
- FastAPI is optional for cloud services.
- The product prioritizes local processing and offline usefulness.

Ambiguity:

The conditions under which synchronization or cloud services are enabled are not specified.

Decision required:

Clarify whether these are excluded from the first release or remain optional implementation choices.

Recorded decision: WebSocket synchronization and FastAPI cloud services are excluded from the first release.

Status: First-release boundary resolved.

11.12 Computer Vision Acceptance Coverage

Blueprint statements:

- OCR has a proposed target of at least 95% accuracy on supported UI samples.
- Hero detection has a proposed target of at least 90% precision on supported test samples.
- Automatic recognition requires separate precision and recall measurements.

Ambiguity:

The supported UI samples, test dataset composition, and evaluation conditions are not defined.

Decision required:

Define the test samples and evaluation conditions used to verify these targets.

Status: Deferred to M4 dataset and evaluation preparation. The supported samples and evaluation conditions must be defined before M4 acceptance is measured; this item is not, by itself, an M0 completion prerequisite.

11.13 v1.0.0 Complementary Reference — Closed

v1.0.0 statement:

The complete verbatim v1.0.0 document is supplied for comparison.

Blueprint statement:

The Blueprint was described as the single source of truth in the earlier reference material. That wording is historical; the explicit source hierarchy in Section 2 of this plan governs.

Decision required:

None. The source-availability limitation is closed. The v1.0.0 text is used only to identify conflicts and gaps below; it does not authorize additional scope.

Status: Closed. The historical source-hierarchy wording is superseded by the explicit hierarchy in Section 2.

11.14 Live Capture, OCR, Overlay and Authorization

v1.0.0 statements:

- “The initial release must work without automated interaction with the running game.”
- “Automated live-game capture,” “OCR against the running game,” and “Live overlays” remain blocked without specific written authorization and applicable policy review.
- Authorization for one acquisition method does not authorize another.
- The initial approved approach includes manual match information, player-entered event records, player-authored observations, synthetic data for testing, reviewed mechanics data, and explicitly authorized imports.

Blueprint statements:

- Android MediaProjection captures the gameplay screen after explicit user consent.
- M2 includes screen capture, permissions, a floating interface, live event updates, and local persistence.
- The acquisition architecture includes OCR and object detection.
- M4 includes OCR and automatic event recognition.
- The Blueprint says to review Mobile Legends' current terms and anti-cheat rules before distributing an overlay or automated screen-analysis companion.

Conflict / gap:

The v1.0.0 written-authorization restriction is stricter than the Blueprint's stated Android user-consent requirement and its M2/M4 acquisition roadmap. The Blueprint does not explicitly state that the separate written authorization required by v1.0.0 is a prerequisite for each of these methods.

Decision required:

Decide whether the v1.0.0 written-authorization restriction is to be adopted as an additional constraint. Until that decision, the Blueprint governs; no v1.0.0 authorization requirement is imported into the plan.

Recorded decision: Adopt the written-authorization requirement as a release gate for M2 and M4. Written authorization and data-rights review must both pass before anything in M2 or M4 ships. Authorization for one acquisition method does not automatically authorize another. Applicable policy review remains required.

Status: Resolved. M2 and M4 release gates recorded in Section 9.1.

11.15 Android-First vs. Android and Desktop

v1.0.0 statements:
- Platform: Android-first.
- Technical direction specifies an Android-first application and Kotlin Multiplatform shared core.
- Desktop is not established as a required first-release application in the v1.0.0 technical direction.

Blueprint statements:

- MIP is a cross-platform product with Android and desktop user experiences.
- Desktop uses Tauri + React + TypeScript.
- Android and desktop share event contracts and intelligence logic.
- M3 introduces the desktop analysis application.
- The final technical recommendation lists Android + Windows/Desktop.

Conflict / gap:

The v1.0.0 Android-first platform framing does not establish the same cross-platform commitment and desktop roadmap as the Blueprint. It does not explicitly prohibit desktop, but its platform commitment is narrower.

Decision required:

No change to the Blueprint's cross-platform direction unless explicitly decided. Clarify whether Android-first sequencing from v1.0.0 has any intended meaning beyond the Blueprint's milestone order.

Recorded decision: The first release is Android only. Desktop development begins at M3. The cross-platform direction and shared intelligence/event-contract architecture remain unchanged.

Status: Resolved.

11.16 Room vs. SQLDelight

v1.0.0 statement:

- “Local persistence through Room.”

Blueprint statement:

- The Blueprint originally specified “SQLite + SQLDelight” for local persistence.
- SQLDelight was specified for match sessions, event history, game data and offline storage.
- This is historical Blueprint wording; the explicit Room decision in Section 4.3 governs local persistence.

Conflict:

The local persistence technologies differ. Room is specified by v1.0.0; SQLDelight is specified by the Blueprint.

Decision required:

The Blueprint's SQLDelight decision remains authoritative. Decide explicitly if Room should replace it; until then, do not introduce Room.

Recorded decision: Room is selected as the local persistence technology, using the repository's existing Room-based work as the baseline. This is an explicit product-owner override of the Blueprint's SQLite + SQLDelight selection for this one item only. No other Blueprint technology decision is changed.

Status: Resolved. Blueprint deviation recorded in the change log.

11.17 Releases 1–4 vs. Milestones M0–M5

v1.0.0 statements:

- Release 1 — Functional Match Review Foundation.
- Release 2 — Tactical Intelligence.
- Release 3 — Cross-Match Intelligence.
- Release 4 — Personal Improvement System.
- Each release has its own scope and exit outcome.

Blueprint statements:

- The development roadmap consists of M0 Foundation, M1 Tracking Engine, M2 Android Companion, M3 Desktop Analysis, M4 Computer Vision, and M5 Tactical Intelligence.
- Each milestone has its own scope, deliverable, and “Done when” test.

Conflict / gap:

The two documents use different roadmap structures and group capabilities differently. The v1.0.0 release sequence is not a one-to-one replacement for the Blueprint's M0–M5 milestones. Cross-match intelligence and a personal improvement system are specified as releases in v1.0.0 but are not separate Blueprint milestones.

Decision required:

Retain M0–M5 as the governing roadmap. Decide separately whether any release-level grouping is needed; do not substitute Releases 1–4 or import their deliverables into the Blueprint milestones.

Recorded decision: M0–M5 remain the governing roadmap. Releases 1–4 are future grouping only and do not replace the milestones, authorize implementation, or import their deliverables into the roadmap.

Status: Resolved.

11.18 Skillshot, Zoning and Rotation Prediction vs. v1.0.0 Boundaries

v1.0.0 statements:

- P2 includes rotation prediction, zoning analysis, positioning evaluation, and skillshot prediction.
- Explicit non-goals include an unofficial live-game advantage overlay, a hidden enemy tracker, and a platform that fabricates tactical certainty.
- The product must not reveal hidden enemy information.

Blueprint statements:

- P2 includes rotation prediction, zoning analysis, positioning evaluation, and skillshot prediction.
- M5 includes positioning and zoning and skillshot prediction.
- Predictions must be based on observed information and explicitly communicate uncertainty.
- The product boundaries prohibit revealing hidden enemy information that cannot legitimately be observed.
- Previously observed enemy positions must not be treated as current positions.

Conflict / gap:

There is no direct conflict between the listed P2 capabilities and the v1.0.0 non-goals when those capabilities use legitimate observable information and communicate uncertainty. The v1.0.0 restriction on an unofficial live-game advantage overlay may, however, conflict with the Blueprint's floating in-match interface depending on its functionality and publisher-policy status. The Blueprint does not define detailed acceptance or validation thresholds for these advanced predictions.

Decision required:

Keep the Blueprint's P2 and M5 scope and its observable-information boundaries. Clarify the permitted functionality of the floating interface and the validation requirements for advanced predictions. Do not treat the v1.0.0 non-goals as authorization to remove Blueprint features or as authorization to expand them.

Recorded decision: Keep the Blueprint's P2 and M5 scope and observable-information boundaries. The floating interface is information-only. M5 is blocked until M4 meets its applicable acceptance targets.

Status: Scope and interface boundary resolved. M5 dependency recorded in Section 7.

11.19 Evidence Classes vs. Event Source Field

v1.0.0 statements:

The evidence model classifies information as:

- Observed.
- Player-reported.
- Imported.
- Synthetic.
- Calculated.
- Inferred.
- Recommended.

It also requires supporting information to be identified and uncertainty to be shown.

Blueprint statements:

Every event carries a source field with the values vision, OCR, manual, or imported replay, along with confidence score, match identifier, game timestamp, detection timestamp, and validation status.

Conflict / gap:

The v1.0.0 evidence classes describe the epistemic or analytical status of information, while the Blueprint's source field identifies the acquisition source of an event. They are not equivalent taxonomies. The Blueprint does not specify the additional v1.0.0 evidence-classification field or its seven categories.

Decision required:

Keep the Blueprint's event source field and its defined source distinctions. Decide explicitly whether an additional evidence-classification model is needed; do not add the v1.0.0 classifications to the event schema without approval.

Status: Deferred to M0 spec work. Original question text retained.

11.20 Definition of Done

v1.0.0 statements:

- Defines a product-level Definition of Done across product functionality, data and intelligence, reliability, quality, security and compliance, and documentation and delivery.
- Defines a separate Definition of Done for every feature and issue.
- Requires acceptance criteria, tests, documentation, code review, CI, and verifiable evidence before an issue is Done.
- States that a merged PR or passing CI alone does not prove product completion.

Blueprint statements:

- Each milestone has a deliverable and a milestone-specific “Done when” test.
- The Blueprint provides proposed engineering acceptance targets and intelligence quality metrics.
- The Blueprint does not define the same comprehensive product-level and issue-level Definition of Done.

Gap:

The Blueprint's milestone tests and acceptance criteria do not specify all of the additional product-level and issue-level completion gates described in v1.0.0.

Decision required:

Keep the Blueprint's milestone “Done when” tests and acceptance criteria authoritative. Decide whether to adopt any additional Definition of Done requirements; none are imported automatically from v1.0.0.

Recorded decision: Adopt the v1.0.0 product-level and issue-level Definition of Done requirements as constraints only. They supplement, and do not replace, the Blueprint's milestone tests and acceptance criteria. See Section 8.1.

Status: Resolved.

11.21 Match Management and Match Metadata

v1.0.0 statements:

- Create, edit, review, and delete match records.
- Record match date and duration, game mode and patch when known, selected role and hero, and relevant match context.
- Maintain match history and preserve records when some information is unavailable.

Blueprint statements:

- M0 includes match session lifecycle and a game database schema.
- P0 includes match session management and hero selection and setup.
- The event model includes a match identifier.
- The Blueprint does not enumerate the full v1.0.0 match metadata fields or the complete record-management workflow.

Gap:

The Blueprint does not specify all of the match metadata fields and match-record management operations listed in v1.0.0.

Decision required:

Use the Blueprint's M0 and P0 scope as written. Decide explicitly whether any additional metadata fields or record-management operations are required; do not import the v1.0.0 list by default.

Status: Deferred to M0 spec work. Original question text retained.

11.22 Event Recording Fields and Correction

v1.0.0 statements:

Every event must retain:

- Match-relative timestamp.
- Recording timestamp.
- Source.
- Confidence or validation state where applicable.
- Relevant actor or participant, if known.
- Structured event details.

The player must be able to correct or remove inaccurate records.

Blueprint statements:

Every event carries source, confidence score, match identifier, game timestamp, detection timestamp, and validation status. The Blueprint also requires event validation before an event enters the match event stream.

Conflict / gap:

The Blueprint specifies game timestamp and detection timestamp, but does not separately define a recording timestamp, actor or participant field, or the complete structured event-details contract. The Blueprint mentions event validation but does not explicitly define the player correction and removal workflow for all recorded events.

Decision required:

Retain the Blueprint's defined event fields and validation pipeline. Decide whether additional event fields and correction/removal operations are needed without adding them automatically from v1.0.0.

Status: Deferred to M0 spec work. Original question text retained.

11.24 Cooldown Intelligence Detail

v1.0.0 statements:

- Versioned cooldown definitions.
- Ability and spell identification.
- Cooldown-ready-time estimates.
- Source and version provenance.
- Missing-data handling.
- Clear distinction between estimated and confirmed availability.
- Recalculation when relevant events are corrected.
- Unknown or unverified mechanics must return unavailable rather than guessed values.

Blueprint statements:

- The Cooldown Engine is an intelligence module.
- M1 includes Battle Spell and ultimate tracking.
- Cooldown calculations should account for patches, equipment, talents and cooldown reductions where applicable.
- Unknown modifiers should cause the result to be marked approximate.

Gap:

The Blueprint does not explicitly define all the v1.0.0 cooldown data provenance, unavailable-state, confirmed-versus-estimated, and recalculation requirements. Its instruction to mark results approximate when modifiers are unknown is not identical to the v1.0.0 instruction to return unavailable for unknown or unverified mechanics.

Decision required:

Follow the Blueprint's cooldown scope and stated approximation behavior. Decide explicitly whether additional cooldown provenance, unavailable-state, or recalculation rules are required.

Recorded decision: Adopt the v1.0.0 "unavailable, not guessed" rule as a constraint. Unknown or unverified cooldown mechanics must be unavailable rather than guessed. This does not expand cooldown feature scope.

Status: Resolved as a data-integrity constraint. See Section 8.2.

11.28 Privacy, Local-First Operation and Data Control

v1.0.0 statements:

- Local-first operation is the initial product baseline.
- Players control what information they enter, correct, retain, and export.
- No undisclosed collection or transmission of match records.
- Export and deletion must behave as documented.

Blueprint statements:

- The product prioritizes local processing and offline usefulness.
- Screenshots are sensitive and should be processed locally.
- WebSocket synchronization and FastAPI cloud services are optional.
- Local persistence and offline operation are part of the architecture.

Gap:

The Blueprint establishes local processing and offline usefulness but does not specify all the v1.0.0 user-control, retention, export, deletion, and undisclosed-transmission requirements.

Decision required:

Retain the Blueprint's local-processing and offline direction. Decide explicitly whether additional data-control and privacy requirements are needed; do not import them automatically from v1.0.0.

Recorded decision: Adopt the v1.0.0 privacy and data-control rules as constraints only. See Section 3. They do not authorize additional product features.

Status: Resolved.

11.29 Product Principles and Explicit Non-Goals

v1.0.0 statements:

The product principles include decision-focused analysis, evidence-based conclusions, transparency, actionable feedback, progress orientation, uncertainty handling, player control, privacy, compliance, and maintainability.

The explicit non-goals also include a matchmaking service, social network, replacement for the MLBB client, and generic statistics dashboard without decision analysis.

Blueprint statements:

- The product pillars are Awareness, Tracking, Prediction, and Analysis.
- The product boundaries prohibit gameplay control, skill automation, memory modification, code injection, packet interception or manipulation, and illegitimate hidden enemy information.
- Predictions must communicate uncertainty.
- The Blueprint does not enumerate all the v1.0.0 principles or non-goals.

Gap:

The Blueprint does not explicitly state the full v1.0.0 principles list or every additional non-goal listed above.

Decision required:

The Blueprint's product pillars and boundaries remain authoritative. Decide explicitly whether additional principles or non-goals are needed; do not add them as requirements from v1.0.0.

Status: Deferred to M0 spec work. Original question text retained.

11.30 Technical Architecture Baseline and Change Authority

v1.0.0 statements:

- The existing technical foundation remains the baseline unless a demonstrated product requirement justifies changing it.
- Product requirements take priority over preserving an existing architecture decision.
- Technical design must support the agreed product.

Blueprint statements:

- The Blueprint recommends a modular monolith.
- It specifies Tauri + React + TypeScript, Kotlin Multiplatform, Python + PyTorch + ONNX Runtime, and originally specified SQLite + SQLDelight.
- The SQLDelight selection is historical and is superseded only for local persistence by the Room decision in Section 4.3; no other Blueprint technology decision is changed.
- It recommends starting with M0 and establishing the domain model, event contracts, game database, and tracking engine.

Conflict / gap:

The v1.0.0 technical-direction language establishes a conditional baseline and change principle that is not stated in the same way in the Blueprint. The Blueprint itself specifies its technical stack and starting recommendation.

Decision required:

Keep the Blueprint's technical decisions authoritative. Decide explicitly whether any additional architecture-change governance is required; do not use v1.0.0 to replace or qualify the Blueprint's stack.

Recorded decision: Adopt explicit review and approval for material architecture changes as a governance constraint. The Blueprint's technology decisions remain unchanged except for the explicitly approved Room persistence deviation recorded in Section 4.3.

Status: Resolved.

11.31 Governance and Approval Gates

v1.0.0 statements:

- Product plan, release scope, requirements, and Definition of Done must be approved before implementation resumes.
- Issues must derive from approved product requirements.
- PRs must reference issues, describe changes, provide test evidence, and identify limitations.
- No PR may be merged without explicit maintainer approval.
- Releases must pass their applicable Definition of Done and release gates.
- Material changes require documented review and approval.

Blueprint statements:

The Blueprint provides a recommended technical baseline and milestone roadmap, but does not specify the same issue, PR, merge-approval, and release-governance procedure.

Gap:

The Blueprint does not define the v1.0.0 project execution and governance gates.

Decision required:

Do not import these governance requirements automatically into the Blueprint-based product scope. Decide separately whether project governance rules are required and where they should be documented.

Recorded decision: Adopt the v1.0.0 governance and approval gates as project constraints. They do not add product features or alter the M0–M5 roadmap.

Status: Resolved. Governance constraints recorded in Section 10.

11.33 Data Acquisition, Rights and Compliance Review

v1.0.0 statements:

- Mechanics data may be used only when its source and usage rights have been reviewed.
- Explicitly authorized imports require all required reviews.
- The compliance document remains a mandatory engineering and release constraint.
- Required third-party rights and permissions must be verified before release.

Blueprint statements:

- The Blueprint requires review of Mobile Legends' current terms and anti-cheat rules before distributing an overlay or automated screen-analysis companion.
- It states that Android technical permission does not automatically mean a feature is permitted by the game publisher.
- Patch data and game mechanics are versioned.
- The Blueprint does not specify the same general third-party data-rights and mandatory compliance-document requirements.

Gap:

The Blueprint does not explicitly define the complete v1.0.0 data-rights review and compliance-document requirements.

Decision required:

Keep the Blueprint's stated policy-review requirement. Decide explicitly whether additional data-rights and compliance-document gates are required; do not import them automatically from v1.0.0.

Recorded decision: Adopt data-rights review as a release gate for M2 and M4. Nothing in M2 or M4 ships until the applicable data-rights review and written-authorization gates have passed. Applicable policy review remains required.

Status: Resolved. Release gates recorded in Section 9.1.

11.34 Product-Level Reliability and Delivery Requirements

v1.0.0 statements:

The product-level Definition of Done requires, among other things:

- Complete end-to-end workflows.
- Local data surviving application restart.
- Correct interrupted-session recovery.
- Tested database migrations.
- Documented export and deletion.
- Invalid input not silently corrupting records.
- Unit, integration, and UI tests.
- Successful Android build and CI.
- No unresolved critical or high-severity release-blocking defects.
- Verified accessibility.
- Accurate user and developer documentation.
- Reproducible release builds.

Blueprint statements:

- M0 includes match session lifecycle and game database schema.
- M2 includes local persistence.
- The acceptance criteria include offline tracking, responsiveness, and engineering quality targets.
- The Blueprint does not define the complete v1.0.0 reliability, accessibility, testing, documentation, and release-delivery checklist.

Gap:

The Blueprint does not explicitly include the full product-level reliability and delivery checklist from v1.0.0.

Decision required:

Use the Blueprint's milestones and acceptance criteria as the governing requirements. Decide explicitly whether any additional product-level verification requirements are needed; do not import the v1.0.0 checklist automatically.

Recorded decision: Adopt the v1.0.0 product-level reliability and delivery requirements as Definition of Done constraints only. They supplement the Blueprint's milestone acceptance criteria and do not introduce additional product features. See Section 8.1.

Status: Resolved.


11.35 First-Release Milestone Boundary — Pending Product-Owner Decision

Issue:

The plan defines the first release as Android only and states that desktop development begins at M3. The P0 table spans multiple milestones, while “Required for MVP” could be mistaken for immediate first-release authorization.

Proposed clarification:

“First release” means the first externally distributed Android build. The milestone(s) included in that build must be explicitly named by the product owner before a release scope is approved. “MVP” in the priority table describes priority across the roadmap and does not independently define the first-release milestone boundary. P0 does not authorize implementation before the applicable milestone.

No milestone range is selected in this draft.

Status: Pending product-owner decision. No first-release milestone boundary is inferred.

11.36 M3 Replay Reconstruction and Acquisition Gate — Pending Product-Owner Decision

Issue:

M3 includes replay ingestion, frame extraction, and event reconstruction, while automatic OCR and computer-vision recognition begin in M4. Section 9.1 currently names M2 and M4 only.

Proposed clarification:

M3 replay reconstruction is limited to manually entered or imported timeline events associated with extracted replay frames. Automatic event detection from replay video, including OCR and object detection, begins in M4. Whether the written-authorization and data-rights release gates in Section 9.1 also apply to M3 replay ingestion must be decided explicitly by acquisition method; this draft does not silently extend or waive those gates. Applicable source, rights, and policy review remains an open prerequisite to using third-party recordings.

Status: Pending product-owner decision. No extension of Section 9.1 is made by this draft.

11.37 M2 Screen-Capture Purpose — Pending Product-Owner Decision

Issue:

M2 includes screen capture and permissions, but M2 is limited to manual and event-driven updates and automatic recognition begins in M4.

Proposed clarification:

M2 screen capture is limited to the capture-session foundation required by the Android companion, including consent, permission handling, and capture lifecycle. M2 does not display captured frames for manual image inspection, analyze frames, run OCR, perform object detection, interpret the minimap, or recognize gameplay events from images. Manual and event-driven event updates remain the only M2 update methods. This clarification does not authorize additional capture use and does not alter the M2 release gates in Section 9.1.

Status: Pending product-owner decision. No frame-display or image-analysis capability is authorized by this draft.

11.38 Cooldown Base Values and Unknown Modifiers — Pending Product-Owner Decision

Issue:

Section 8.2 requires unknown or unverified cooldown mechanics to be unavailable rather than guessed. The Blueprint also describes results as approximate when modifiers are unknown.

Proposed clarification:

A verified base cooldown may be displayed as a “Base cooldown” value when modifiers are unknown, provided it is not represented as the adjusted cooldown or actual ready time. An adjusted cooldown or ready-time result is unavailable unless the base mechanics and all modifiers required for that calculation are sufficiently verified. No unsupported adjusted value may be presented as confirmed. Any separately approved estimate must identify its assumptions and remain clearly labeled as an estimate.

This proposal does not authorize a new estimation feature or change the applicable milestone.

Status: Pending product-owner decision. Section 8.2 remains authoritative until this clarification is approved.

11.39 Desktop Persistence and Shared-Core Integration — Deferred Specification Only

Issue:

The architecture identifies Room for Android/local persistence and Tauri + React + TypeScript for desktop, but does not specify desktop local storage or the consumption path for shared Kotlin Multiplatform logic from the TypeScript application.

Proposed disposition:

Defer specification/investigation of (a) desktop local storage, (b) how the shared Kotlin Multiplatform core is consumed by the Tauri application, and (c) whether event contracts are shared as code or as a schema. This is specification/investigation only. It does not authorize implementation, selection of a new persistence technology, a Kotlin/TypeScript bridge, or any architecture/technology change. Any material architecture change remains subject to Section 10 and explicit approval.

Status: Proposed for deferred specification/investigation only; pending product-owner approval. No implementation is authorized.

11.40 M0 Specification and M4 Evaluation Readiness — Pending Product-Owner Decision

Issue:

Items 11.6–11.10 are deferred to M0 specification work. Item 11.12 concerns the supported computer-vision samples and evaluation conditions required to measure M4 targets. Treating all of these as identical M0 blockers would unnecessarily make M0 dependent on M4 evaluation materials.

Proposed clarification:

M0 may be marked complete only when the specifications in 11.6–11.10 have been resolved to the extent necessary to verify the M0 deliverables, and the M0 “Done when” checks are expressed as measurable checks with evidence. Item 11.12 is M4 dataset/evaluation preparation: its supported samples and evaluation conditions must be defined before M4 acceptance is measured, and therefore before the M5 dependency on M4 acceptance can be evaluated. Item 11.12 is not, by itself, an M0 completion prerequisite.

This proposal does not mark M0 complete, change the current milestone status, or authorize M4 implementation.

Status: Pending product-owner decision.
---

12. Future / Requires Review

Item IDs below retain their original Section 11 numbers for traceability; they are not renumbered as Section 12 items.

The following items are retained for possible future review. They are not authorized product scope, do not alter the M0–M5 roadmap, and require explicit approval before becoming requirements.

11.23 Tactical Analysis Output and Explanation Requirements

v1.0.0 statements:

Each analysis must provide:

1. Situation.
2. Evidence.
3. Interpretation.
4. Impact.
5. Alternative.
6. Confidence.
7. Action.

It also lists supported tactical situations such as objective contest timing, rotation timing, battle spell timing, deaths before objectives, engagement conditions, positioning observations, team-fight participation, and available information before a decision.

Blueprint statements:

- The intelligence engine includes tracking, inference and prediction.
- The tactical decision engine combines validated events into tactical warnings and recommendations.
- P2 includes engagement probability, team-fight analysis, and personalized tactical recommendations.
- The Blueprint does not prescribe the seven-part analysis output format.

Gap:

The Blueprint does not specify the seven required analysis output fields or the full v1.0.0 list of tactical analysis situations as a separate output contract.

Decision required:

Keep the Blueprint's stated intelligence modules, priorities, and milestone scope. Decide explicitly whether a standardized analysis output contract is required; do not import the seven-part format or additional analysis requirements by default.

Status: Future / Requires Review. Not authorized scope.

11.25 Cross-Match Pattern Analysis

v1.0.0 statements:

- Compare reviewed matches to identify repeated mistakes and strengths.
- Identify patterns such as repeated deaths before objectives, late rotations, poor objective preparation, spell-timing problems, unfavorable engagements, and recurring positioning problems.
- Require multiple observations, included matches, supporting events, limitations, confidence, and suggested improvement.
- A single event must not be presented as a recurring behavioral pattern.

Blueprint statements:

- The intelligence engine includes analytics and post-match reports.
- P2 includes personalized tactical recommendations and team-fight analysis.
- The M0–M5 roadmap does not define a separate cross-match pattern-analysis milestone or the v1.0.0 pattern evidence requirements.

Gap:

The Blueprint does not specify the cross-match pattern-analysis capability and its detailed evidence requirements as described in v1.0.0.

Decision required:

Do not add cross-match pattern analysis as a separate commitment from v1.0.0. Decide explicitly whether it belongs in a future approved scope, subject to the event-log and milestone Scope Guard.

Status: Future / Requires Review. Not authorized scope.

11.26 Improvement and Coaching Workflow

v1.0.0 statements:

- Provide prioritized improvement opportunities and specific practice recommendations.
- Track progress and compare earlier and later reviewed matches.
- Recognize improved behavior and reassess previous weaknesses.
- Provide measurable practice objectives.

Blueprint statements:

- The product includes a Decision Support module.
- P2 includes personalized tactical recommendations.
- The Blueprint does not define a separate improvement-coaching workflow, practice-objective tracking, or reassessment cycle.

Gap:

The Blueprint does not specify the complete v1.0.0 improvement and coaching workflow or its progress-tracking requirements.

Decision required:

Keep the Blueprint's Decision Support and P2 scope as written. Decide separately whether the broader coaching workflow is needed; do not import it from v1.0.0.

Status: Future / Requires Review. Not authorized scope.

11.27 Complete Match Review and Learning Journey

v1.0.0 statement:

The main user journey has eight steps: Prepare, Record, Review, Analyze, Understand, Improve, Compare, and Reassess. It identifies the complete learning loop as the primary success criterion.

Blueprint statements:

- The Blueprint defines Android live companion and desktop replay and analytics experiences.
- It specifies match session management, event processing, replay ingestion, timelines, reports, and tactical intelligence.
- It does not define the same eight-step user journey or designate that complete learning loop as its primary success criterion.

Gap:

The Blueprint does not specify the eight-step user journey as a required end-to-end workflow.

Decision required:

Retain the Blueprint's user-facing workflows and milestone deliverables. Decide explicitly whether an additional end-to-end learning journey is required; do not add it automatically.

Status: Future / Requires Review. Not authorized scope.

11.32 Success Metrics

v1.0.0 statements:

Product metrics include:

- Percentage of reviewed matches with complete enough evidence for analysis.
- Percentage of supported analyses with traceable evidence.
- Number of recurring patterns supported by multiple matches.
- Percentage of recommendations with measurable practice objectives.
- Player ability to explain why an identified situation mattered.
- Progress on previously identified improvement objectives.
- Data correction and analysis rejection rates.
- Reliability of match records and persistence.

It also distinguishes product-value metrics from project activity metrics.

Blueprint statements:

The Blueprint defines intelligence quality metrics for detection precision, detection recall, event latency, cooldown error, position error, prediction calibration, battery usage, and FPS impact.

Gap:

The Blueprint's engineering and intelligence metrics do not include the full set of product-outcome metrics specified in v1.0.0.

Decision required:

Keep the Blueprint's stated metrics authoritative. Decide explicitly whether additional product-outcome metrics are needed; do not add the v1.0.0 metrics automatically.

Status: Future / Requires Review. Not authorized scope.

12.1 Future Release Grouping Reference

The following v1.0.0 release labels are retained only as possible future grouping references:

- Release 1 — Functional Match Review Foundation.
- Release 2 — Tactical Intelligence.
- Release 3 — Cross-Match Intelligence.
- Release 4 — Personal Improvement System.

These labels do not replace M0–M5, authorize their listed deliverables, or establish a release commitment.

---

13. Change Log

This section preserves the historical v1.1.0 change log. The v1.1.1 draft changes are recorded in 13.1 below.

1. Document identity and status
   
   - Changed the document title from "PRODUCT_PLAN_V.1.1.0_DRAFT" to "PRODUCT_PLAN_V.1.1.0_LOCKED_SOT".
   - Set version to 1.1.0.
   - Changed status from Draft — pending review to LOCKED.
   - Added date: 2026-11-12.
   - Added the Current Milestone line using the product owner's exact confirmed wording.

2. Source hierarchy
   
   - Replaced the draft's Blueprint-first hierarchy with the locked document and explicit product-owner decisions as the single source of truth.
   - Retained the Blueprint as the underlying reference, subject to explicitly recorded decisions.
   - Retained v1.0.0 as complementary only, with only explicitly adopted constraints applying.
   - Retained the vision document as context only.

3. First-release platform
   
   - Recorded Android as the only first-release platform.
   - Recorded that desktop development begins at M3.
   - Kept the cross-platform direction and shared intelligence/event-contract architecture.

4. M2 and M4 acquisition boundaries
   
   - Restricted M2 to manual and event-driven updates.
   - Recorded that automatic recognition begins in M4.
   - Added written-authorization and data-rights release gates for M2 and M4.
   - Recorded that nothing in M2 or M4 ships until both applicable gates pass.

5. Desktop live capture and optional services
   
   - Excluded desktop live screen capture from the first release.
   - Excluded WebSocket synchronization from the first release.
   - Excluded FastAPI cloud services from the first release.
   - Retained the existing architecture references to these technologies and capabilities without authorizing them for the first release.

6. M5 dependency and floating interface
   
   - Blocked M5 until M4 meets its applicable acceptance targets.
   - Clarified that the floating interface is information-only.

7. Persistence technology — explicit Blueprint deviation
   
   - Replaced "SQLite + SQLDelight" with "SQLite + Room" for local persistence.
   - Adopted the repository's existing Room-based work as the baseline.
   - This is an explicit product-owner override of the Blueprint's persistence selection for this item only.
   - No other Blueprint technology decision was changed.

8. Roadmap
   
   - Retained M0–M5 as the governing development roadmap.
   - Recorded Releases 1–4 as future grouping only.
   - Did not import release deliverables into M0–M5.

9. M0 status
   
   - Recorded: "M0 status unverified; Sprint 0 complete; M1 not started."
   - Explicitly prohibited marking M0 complete until all M0 acceptance criteria are verified with evidence.

10. Adopted constraints
    
    - Adopted v1.0.0 Definition of Done requirements as constraints only.
    - Adopted v1.0.0 privacy and data-control rules as constraints only.
    - Adopted v1.0.0 governance and approval gates as constraints only.
    - Adopted the "unavailable, not guessed" cooldown rule as a data-integrity constraint.
    - These adoptions do not authorize additional product features or replace the Blueprint's milestone scope and acceptance criteria.

11. Open Questions disposition
    
    - Recorded decisions and status for 11.1–11.5, 11.11, 11.13–11.18, 11.20, 11.24, 11.28, 11.30, 11.31, 11.33, and 11.34.
    - Marked 11.6–11.10, 11.12, 11.19, 11.21, 11.22, and 11.29 as Deferred to M0 spec work, retaining their original question text.
    - Moved 11.23, 11.25, 11.26, 11.27, and 11.32 into Future / Requires Review, retaining their original question text.
    - Added the future-only Releases 1–4 grouping reference.
    - Preserved unresolved details as unresolved rather than assuming they had been decided.

12. Scope and governance
    
    - Updated Scope Guard authority to reflect the locked source hierarchy.
    - Added the adopted project approval and delivery constraints.
    - Preserved the existing product boundaries, priority categories, and M0–M5 milestone structure except for the explicit decisions recorded above.

13.1 PRODUCT_PLAN_V.1.1.1_DRAFT Change Summary

1. Folded the E1–E3 clarifications into the plan text: Room supersedes historical SQLDelight wording; desktop priority-table entries are later-milestone capabilities, not first-release scope; Section 2 governs source hierarchy.
2. Clarified that historical “Decision required” text remains for traceability and does not override an explicit recorded decision with resolved status.
3. Preserved original Section 11 identifiers for items retained under Section 12.
4. Clarified that `apps/desktop/` represents planned M3 development and is not authorization before M3; `services/` entries are future/optional placeholders only.
5. Clarified the M2 capture boundary without authorizing frame display, manual image inspection, OCR, object detection, or automatic recognition.
6. Added proposed decision records 11.35–11.40 for first-release milestone boundary, M3 replay reconstruction and gate applicability, M2 capture purpose, cooldown treatment, deferred desktop architecture specification, and separation of M0 specification from M4 evaluation readiness.
7. No product-owner decision in 11.35–11.40 is treated as approved. No implementation, architecture change, or milestone authorization is granted by this draft.

---

Product-owner approval record

Approved: 2026-11-13. The product owner approved this v1.1.1 document as written. Items explicitly marked deferred, unresolved, or future/requires review remain open and are not converted into product decisions by this approval. No implementation, architecture change, milestone advancement, release, or merge is authorized by this document approval alone.

End of PRODUCT_PLAN_V.1.1.1_LOCKED_SOT

This locked plan is authoritative following product-owner approval on 2026-11-13. PRODUCT_PLAN_V.1.1.0_LOCKED_SOT is preserved unchanged as the historical baseline.