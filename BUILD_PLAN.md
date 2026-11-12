# MIP — BUILD PLAN

**Product:** Mobile Legends Intelligence Platform  
**Product-plan baseline:** `docs/PRODUCT_PLAN.md` — Version 1.0.0  
**Document role:** Master product-wide phase and completion checklist  
**Current execution authority:** `PLAN.md`  
**Current stage:** H1 — Capture  
**Current hypothesis status:** H1 TESTING · H2 LOCKED · H3 LOCKED · H4 LOCKED

---

## 1. Purpose and authority

This file is the master checklist for the complete MIP product direction described by `docs/PRODUCT_PLAN.md` v1.0.0. It records the full phase sequence and the cross-cutting requirements that must not be forgotten as the project evolves.

It is not permission to implement every listed capability immediately.

The documents have distinct responsibilities:

- **`BUILD_PLAN.md`** — master product-wide roadmap, phase checklist, and record of completion across H1–H4.
- **`docs/PRODUCT_PLAN.md`** — governing product vision, principles, evidence model, boundaries, and intended hypothesis progression.
- **`PLAN.md`** — sole authority for the currently active hypothesis, implementation scope, next step, validation protocol, and current Definition of Done.
- **`docs/POLICY_AND_COMPLIANCE.md`** — compliance constraints and review requirements.
- **`docs/ARCHITECTURE.md`** — current technical boundaries and dependency rules.

If this file and `PLAN.md` differ about what may be built now, **`PLAN.md` controls current execution**. If a proposed change conflicts with the product vision or policy, stop and resolve it through a documented plan change before implementation.

A checked item means its stated work or evidence has actually been completed and reviewed. It must never be checked merely because code exists, a PR was opened, CI passed, or a feature was discussed.

A stage may advance only when its validation outcome satisfies the gate defined below and in the current execution plan. Passing one stage unlocks the next test; it does not automatically authorize broad feature expansion.

## 2. Product thesis and intended outcome

MIP aims to help Mobile Legends: Bang Bang players move from manually recorded match information to evidence-grounded tactical understanding and, eventually, better in-game decisions.

MIP is an independent, player-controlled match-review companion. It is not gameplay automation, a source of hidden information, or a substitute for player judgment.

The core problem being tested is whether players can identify specific, reviewable situations behind match outcomes and understand what they could change next time.

The product thesis remains a hypothesis. Do not claim MIP improves performance, win rate, or rank without appropriate evaluation.

## 3. Global product principles

These principles apply to every phase and every release:

- [ ] **Evidence before advice:** never present an inference or estimate as an observed fact.
- [ ] **Narrow rules before broad coaching:** prefer a small, testable rule over vague tactical scoring.
- [ ] **Player-controlled capture:** the player decides what to record and when.
- [ ] **Transparent uncertainty:** show source, confidence, limitations, and unavailable states.
- [ ] **Local-first by default:** V1 works offline and does not require an account or backend.
- [ ] **No hidden advantage:** do not expose information unavailable to the player through permitted play.
- [ ] **Earn every release:** build a capability only after the preceding hypothesis has passed.
- [ ] **Measure behavior, not enthusiasm alone:** repeated voluntary use and observed behavior change are stronger evidence than stated interest or novelty.

These are enduring acceptance principles, not tasks that can be considered complete once and then ignored.

## 4. Phase overview and gates

| Phase | Hypothesis | Intended proof | Gate |
|---|---|---|---|
| H1 — Capture | Players will consistently record matches when capture is fast and useful enough | Reliable match capture with acceptable effort and voluntary repeat recording | H1 protocol passes |
| H2 — Understanding | A narrowly defined tactical rule can produce accurate, contextually useful conclusions | One pre-registered rule evaluated against independently adjudicated evidence | H1 passes; H2 rule and evaluation are pre-registered; H2 evaluation passes |
| H3 — Behavior | A recommendation based on credible analysis changes a targeted player behavior | One measurable behavior change against a predefined baseline | H2 passes; behavioral test is pre-registered; H3 evaluation passes |
| H4 — Retention | Players continue using MIP because it provides recurring value | Voluntary continued use after novelty fades, attributable to recurring value | H3 passes; retention test is pre-registered; H4 evaluation passes |

**Progression is sequential.** H2, H3, and H4 remain locked until their preceding gates pass. A pass validates only the hypothesis tested at that stage.

---

## 5. Phase 0 — Project baseline and governance

This is an enabling checkpoint, not an additional product hypothesis or a promise of user-facing functionality.

- [ ] Confirm `docs/PRODUCT_PLAN.md` v1.0.0 is the product-vision baseline for this roadmap.
- [ ] Keep `PLAN.md` synchronized with the currently authorized phase, scope, next step, validation protocol, and Definition of Done.
- [ ] Confirm the applicable policy/compliance document is present and reviewed before compliance-sensitive work.
- [ ] Record the current repository architecture and the modules actually used by the Android-first product.
- [ ] Compare retaining the existing Kotlin Multiplatform shared core with simplifying to Android-only, using delivery effort, testability, and migration cost; document the evidence before an architecture change.
- [ ] Keep domain logic independent of UI and persistence where that separation provides practical value.
- [ ] Avoid adding modules, abstractions, frameworks, dependencies, Room, or infrastructure without a concrete approved requirement.
- [ ] Ensure phase changes and scope changes are documented before implementation.
- [ ] Ensure implementation approval and merge approval are treated as separate decisions.

**Exit condition:** project governance, policy boundaries, and the active execution baseline are clear. This checkpoint does not unlock H2.

## 6. H1 — Capture

<badge-placeholder></badge-placeholder>

**Status:** TESTING  
**Unlocks:** H2 pre-registration and evaluation only after H1 PASS.  
**Purpose:** Determine whether active ranked MLBB players will consistently record matches when post-match capture is fast and low-friction.

### 6.1 H1 product scope

- [ ] Create a match record containing hero, role, result, and essential match metadata.
- [ ] Allow the player to record 3–5 key moments.
- [ ] Support structured player-death events with match-relative timestamps.
- [ ] Support structured objective events, including Turtle objective/contest timestamps where recorded.
- [ ] Keep unknown or missing timestamps unknown; never infer a missing contest marker.
- [ ] Show a simple, deterministic chronological event timeline.
- [ ] Allow a match and its events to be edited.
- [ ] Allow a match and its events to be deleted.
- [ ] Persist records locally across normal app restarts.
- [ ] Keep core capture and review flows usable offline.
- [ ] Provide explicit, user-requested export of recorded match data.
- [ ] Make the UI distinguish player-entered/user-reported information from derived information.
- [ ] Keep H1 free of tactical verdicts, coaching, cross-match patterns, cooldown intelligence, and elaborate summaries.

### 6.2 H1 data and quality requirements

- [ ] Capture only fields with a defined H1 or H2 evaluation purpose.
- [ ] Include match identity, duration, mode/patch when known, hero, role, and result where relevant to the approved H1 scope.
- [ ] Store event type and match-relative timestamp.
- [ ] Store objective type and timestamp, including a clearly defined contest/engagement marker if required for later H2 evaluation.
- [ ] Preserve event source and confidence/validation status where applicable.
- [ ] Preserve optional player context without making unnecessary fields mandatory.
- [ ] Keep enough creation/edit history to distinguish original entry from later correction.
- [ ] Make timeline ordering deterministic, including equal-time events.
- [ ] Make missing, stale, contradictory, or insufficient evidence explicit rather than guessing.

### 6.3 H1 validation protocol

- [ ] Recruit 5 active ranked MLBB players who are not participating merely to please the developer.
- [ ] Record each participant's ranked-play activity and prior familiarity with match tracking.
- [ ] Start capture timing when a participant opens **New Match** and stop when the record is successfully saved.
- [ ] Include all required entry and event-recording steps; exclude unrelated discussion and interruptions.
- [ ] Measure first-time and repeat capture separately; retain individual values and report both medians.
- [ ] Observe whether each participant completes at least 3 match records.
- [ ] After onboarding, do not remind or prompt participants to record another match.
- [ ] Count participants who voluntarily return and log another match without prompting.
- [ ] Record total completed matches and voluntary return timing where practical.
- [ ] After timeline review, ask exactly: **“Did reviewing this change what you'd do next match? What specifically?”**
- [ ] Record answers verbatim and distinguish a specific intended behavior change tied to a timeline event from a generic positive response.
- [ ] Record abandonment points, assistance required, protocol deviations, and missing data.

### 6.4 H1 decision criteria

- [ ] **PASS:** at least 3 of 5 participants complete at least 3 match records.
- [ ] **PASS:** median repeat capture time is under 3 minutes.
- [ ] **PASS:** at least 3 of 5 participants voluntarily return and log another match without prompting.
- [ ] **PASS:** median first-time capture time is no greater than 5 minutes.
- [ ] **FAIL:** fewer than 3 of 5 participants complete 3 records, or median first-time capture exceeds 5 minutes, or median repeat capture exceeds 5 minutes.
- [ ] **INCONCLUSIVE / REVISE:** results meet neither the complete pass nor fail definition; investigate, revise, and repeat H1 without advancing.
- [ ] Publish the participant context, individual and median times, completion/return counts, abandonment/assistance, verbatim answers, behavior-change intentions, deviations, and missing data.
- [ ] Record the explicit H1 outcome as **PASS**, **FAIL**, or **INCONCLUSIVE**.
- [ ] Update `PLAN.md` with the evidence and outcome.
- [ ] Do not begin H2 implementation unless H1 is explicitly PASS.

**H1 Definition of Done**

- [ ] All approved H1 capture, timeline, edit/delete, persistence, offline, export, and evidence-label requirements are implemented.
- [ ] Automated tests cover validation, timestamp handling, ordering, persistence, edit/delete, and export.
- [ ] Android build and configured CI checks pass.
- [ ] The H1 participant protocol is completed and reported.
- [ ] The H1 outcome is recorded against the predefined criteria.
- [ ] Maintainer review explicitly authorizes the next phase.

**H1 does not prove:** analysis accuracy, tactical understanding, behavior change, product-market fit, improved win rate/rank, or long-term product value.

## 7. H2 — Understanding

**Status:** LOCKED until H1 passes.  
**Purpose:** Test whether one narrow, evidence-grounded tactical rule can produce accurate and contextually useful conclusions.

### 7.1 Pre-registration — required before implementation

Candidate rule from Product Plan v1.0.0:

> Flag a player's recorded death if its match-relative timestamp falls within the 30 seconds immediately before that player's manually recorded Turtle contest start.

This is a temporal relationship only. It does not prove that the death caused a lost Turtle, prove a late rotation, or establish a bad engagement.

- [ ] Confirm H1 is PASS and record the evidence.
- [ ] Define “player death” precisely.
- [ ] Define “Turtle contest start” precisely.
- [ ] Define eligible match phases.
- [ ] Define the 30-second interval and inclusive/exclusive boundaries.
- [ ] Define required evidence and exclusions for missing, corrected, contradictory, or uncertain timestamps.
- [ ] Define true positive, false positive, false negative, and insufficient-evidence cases.
- [ ] Define how flagged cases and sampled unflagged cases will be selected for review.
- [ ] Define independent adjudication by the player and an experienced MLBB reviewer.
- [ ] Define the minimum eligible sample size.
- [ ] Finalize the pass threshold before implementation. The product plan's proposed starting point is at least 70% of adjudicated flagged situations confirmed as genuinely meeting the rule and contextually represented accurately; this is not an approved final threshold until pre-registered.
- [ ] Define how reviewer disagreement, uncertainty, and insufficient evidence will be reported.
- [ ] Save the pre-registration and obtain explicit approval before building H2.

### 7.2 H2 implementation and evidence

- [ ] Implement only the approved, pre-registered tactical rule.
- [ ] Link every derived result to its supporting input records.
- [ ] Record the rule/version used to produce each derived result.
- [ ] Clearly distinguish OBSERVED, USER-REPORTED, DERIVED, INFERRED, ESTIMATED, SYNTHETIC, and UNAVAILABLE evidence.
- [ ] Explain the temporal relationship and its limitations in the UI.
- [ ] Do not convert correlation into causation.
- [ ] Do not label a player, rotation, or engagement as bad without separately validated evidence.
- [ ] Handle missing, stale, contradictory, corrected, and insufficient inputs explicitly.
- [ ] Test deterministic rule behavior, interval boundaries, invalid inputs, and unavailable states.
- [ ] Evaluate flagged and sampled unflagged cases using the pre-registered protocol.
- [ ] Report sample size, adjudication results, disagreements, uncertainty, false positives/negatives, and limitations.
- [ ] If too few eligible situations exist, record H2 as INCONCLUSIVE rather than PASS.
- [ ] Record H2 as PASS, FAIL, or INCONCLUSIVE against the pre-registered threshold.
- [ ] Update `PLAN.md` with the result and obtain explicit approval before H3 work.

**H2 Definition of Done**

- [ ] Pre-registration was approved before implementation.
- [ ] The narrow rule is implemented, versioned, deterministic, tested, and explainable.
- [ ] Evidence provenance and limitations are visible and traceable.
- [ ] Independent adjudication and evaluation are completed.
- [ ] The result is recorded against the pre-registered criteria.
- [ ] Maintainer review explicitly authorizes any H3 work.

**H2 does not prove:** that the rule explains match outcomes generally, that the recorded death caused an objective outcome, that the player changed behavior, or that MIP improves performance.

## 8. H3 — Behavior

**Status:** LOCKED until H2 passes.  
**Purpose:** Test whether one recommendation based on credible analysis changes one targeted player behavior.

### 8.1 Behavioral experiment design

- [ ] Confirm H2 is PASS and record the evidence.
- [ ] Select one recommendation directly supported by the validated H2 analysis.
- [ ] Define one specific, observable target behavior.
- [ ] Define the baseline before exposing participants to the recommendation.
- [ ] Define how comparable situations will be identified across matches.
- [ ] Define the observation period and sample requirements.
- [ ] Define the success threshold and failure/inconclusive conditions before the test.
- [ ] Define how confounders, missing data, and sample limitations will be recorded.
- [ ] Pre-register the behavioral test and obtain explicit approval before implementation or participant testing.

### 8.2 H3 implementation and evaluation

- [ ] Present only the approved targeted recommendation.
- [ ] Show the supporting evidence, rule version, and limitations.
- [ ] Avoid broad coaching claims or unsupported tactical ratings.
- [ ] Measure the predefined behavior in later comparable situations.
- [ ] Compare observed behavior with the predefined baseline.
- [ ] Record confounders and cases that cannot be fairly compared.
- [ ] Separate stated intention from observed behavior.
- [ ] Evaluate results against the pre-registered threshold.
- [ ] Record H3 as PASS, FAIL, or INCONCLUSIVE.
- [ ] Update `PLAN.md` with the evidence and obtain explicit approval before H4 work.

**H3 Definition of Done**

- [ ] H2 is confirmed PASS.
- [ ] The recommendation and target behavior were pre-registered.
- [ ] Baseline, observation method, sample requirements, and decision criteria were defined before the test.
- [ ] Later behavior was measured using the approved method.
- [ ] Confounders, missing data, and limitations are reported.
- [ ] The outcome is recorded against the pre-registered criteria.
- [ ] Maintainer review explicitly authorizes any H4 work.

**H3 does not prove:** durable retention, broad player improvement, improved win rate/rank, or general effectiveness across players and situations.

## 9. H4 — Retention

**Status:** LOCKED until H3 passes.  
**Purpose:** Test whether players continue using MIP voluntarily because it provides recurring value after novelty fades.

### 9.1 Retention experiment design

- [ ] Confirm H3 is PASS and record the evidence.
- [ ] Define the target user cohort and observation period.
- [ ] Define what counts as a meaningful return and repeat review.
- [ ] Define the minimum sample and retention threshold before testing.
- [ ] Define how novelty effects will be distinguished from sustained value.
- [ ] Define how reminders, journaling habit, social pressure, and other external prompts will be accounted for.
- [ ] Define how participants' perceived recurring value will be collected without substituting enthusiasm for behavior.
- [ ] Pre-register the retention test and obtain explicit approval before implementation or participant testing.

### 9.2 H4 implementation and evaluation

- [ ] Build only the repeat-review workflow needed for the approved retention experiment.
- [ ] Measure voluntary returns over the predefined period.
- [ ] Distinguish prompted from unprompted use.
- [ ] Record whether repeat use is connected to recurring review value rather than novelty, reminders, or social pressure.
- [ ] Report cohort, sample size, return behavior, observation window, missing data, and limitations.
- [ ] Evaluate results against the pre-registered threshold.
- [ ] Record H4 as PASS, FAIL, or INCONCLUSIVE.
- [ ] Update `PLAN.md` and the product roadmap with the evidence-backed outcome.

**H4 Definition of Done**

- [ ] H3 is confirmed PASS.
- [ ] Retention definitions, cohort, observation period, and thresholds were pre-registered.
- [ ] Voluntary and prompted use are distinguished.
- [ ] The test evaluates recurring value after novelty fades.
- [ ] The outcome and limitations are documented.

**H4 does not automatically authorize:** accounts, cloud sync, telemetry, social features, paid services, or any other infrastructure. Those require separate product, privacy, security, and maintainer review.

---

## 10. Cross-phase evidence model

Every input and conclusion must preserve its evidence category:

| Category | Meaning | Required handling |
|---|---|---|
| **OBSERVED** | Directly recorded observation with a traceable source | Preserve the source and timestamp where available. |
| **USER-REPORTED** | Statement or event entered by the player | Identify it as player-reported, not independently verified. |
| **DERIVED** | Deterministic result computed from recorded inputs | Link supporting inputs and the rule/version used. |
| **INFERRED** | Interpretation that depends on context or a rule | Identify it as interpretation and show supporting evidence and limitations. |
| **ESTIMATED** | Approximation based on documented/versioned data | Label it as an estimate and expose its source/version. |
| **SYNTHETIC** | Fictional data used only for tests or demonstrations | Clearly label it as synthetic; never present it as a real match. |
| **UNAVAILABLE** | Evidence is missing, stale, contradictory, or insufficient | Show unavailable/uncertain state; do not guess. |

- [ ] Every displayed fact and conclusion has traceable provenance appropriate to its category.
- [ ] Derived and inferred outputs link to supporting inputs and rule version.
- [ ] Estimates are visibly distinguished from observations.
- [ ] Synthetic fixtures cannot be confused with real player data.
- [ ] Missing, stale, contradictory, or insufficient evidence is represented explicitly.
- [ ] Corrections preserve the distinction between original and corrected information.
- [ ] Correlation is never presented as causation.

## 11. Cross-phase data, privacy, and compliance

### Data minimization and user control

- [ ] Collect only fields with a current product or evaluation purpose.
- [ ] Explain what information is stored and what is included in exports.
- [ ] Provide user control over correction and deletion.
- [ ] Keep local processing and local history as the default direction.
- [ ] Keep core V1 workflows usable offline.
- [ ] Do not require an account or backend for V1.
- [ ] Treat cloud sync, accounts, telemetry, and sharing as separate proposals requiring product, privacy, and security review.

### Permitted initial data direction

- [ ] Use manual player-entered records.
- [ ] Use synthetic fixtures only when clearly labeled as fictional.
- [ ] Use documented/versioned game-mechanics data only after provenance, rights, and permission review.
- [ ] Keep exports explicit and user-controlled.
- [ ] Ensure derived outputs inherit the provenance and restrictions of their inputs.

### Prohibited without exact written authorization and separate review

- [ ] Do not implement automated live capture or OCR.
- [ ] Do not implement gameplay overlays or live-game assistance.
- [ ] Do not inspect game memory or packets.
- [ ] Do not modify the game.
- [ ] Do not automate player input or gameplay.
- [ ] Do not infer hidden game state or automate enemy tracking.
- [ ] Do not treat Android permissions, public availability, community practice, or app-store approval as publisher authorization.
- [ ] Before considering any exception, obtain exact written authorization and review the specific method, data, purpose, platform, and distribution model.

Follow `docs/POLICY_AND_COMPLIANCE.md` at all times. This checklist does not grant an exception.

## 12. Cross-phase technical and quality requirements

- [ ] Keep the product Android-first unless a separately approved product decision changes that direction.
- [ ] Keep the product local-first and offline-capable for V1.
- [ ] Retain or simplify Kotlin Multiplatform based on measured delivery value, not preference.
- [ ] Keep domain logic independent of UI and persistence where useful.
- [ ] Keep dependency direction inward toward stable domain abstractions.
- [ ] Do not let shared domain/contracts/engine depend on Android, Compose, Room, network, or storage frameworks.
- [ ] Do not let UI code access persistence records or Room directly.
- [ ] Use deterministic, repeatable tests for domain rules and analysis.
- [ ] Keep persistence versioned/migratable when persistence implementation requires it.
- [ ] Keep timelines deterministic and chronological.
- [ ] Ensure local data survives normal app restarts.
- [ ] Ensure core workflows work offline.
- [ ] Ensure export is user-initiated and documented.
- [ ] Keep tactical rules versioned, testable, reviewed, and explainable.
- [ ] Avoid dependencies, modules, abstractions, and infrastructure without a concrete approved requirement.
- [ ] Do not add a backend, account system, live integration, or cross-platform release as an assumed requirement.

## 13. Explicit non-goals unless separately validated and approved

The following are not implied by this roadmap and must not be added merely because they appear useful:

- [ ] No full tactical coaching engine in the first build.
- [ ] No broad tactical ratings or unsupported labels such as “bad player”, “bad rotation”, or “late rotation”.
- [ ] No cross-match pattern claims without adequate player-specific data.
- [ ] No automatic match capture or live-game assistance.
- [ ] No hidden enemy tracking or information not legitimately available to the player.
- [ ] No social network, matchmaking, leaderboard, cloud account, or paid service by default.
- [ ] No claim of improved win rate or rank without an appropriate evaluation.
- [ ] No automatic expansion of scope after a phase passes.

## 14. Governance, change control, and completion rules

- [ ] Keep this file as the master checklist for the complete v1.0.0 product roadmap.
- [ ] Keep `PLAN.md` as the active execution authority for current scope, phase status, next step, validation protocol, and Definition of Done.
- [ ] Before starting a phase, confirm its predecessor passed and its own experiment is pre-registered where required.
- [ ] Record scope changes in the relevant plan before implementation.
- [ ] Record why a checklist item is being added, removed, or changed.
- [ ] Require explicit maintainer approval for implementation scope changes.
- [ ] Treat approval of a plan as distinct from approval to implement, and both as distinct from approval to merge.
- [ ] Keep changes small and reviewable.
- [ ] Do not merge a pull request without explicit maintainer approval.
- [ ] Record each phase outcome as PASS, FAIL, or INCONCLUSIVE with supporting evidence.
- [ ] On FAIL or INCONCLUSIVE, revise and retest the current hypothesis; do not skip ahead.
- [ ] Update this checklist only when work is genuinely complete and the evidence is available.
- [ ] Never mark a future phase complete based on assumptions, mock data, implementation alone, or a predecessor's pass.

## 15. Product-level success definition

MIP's success is evidence-based, not a feature count:

- [ ] **H1:** players reliably capture matches with acceptable effort.
- [ ] **H2:** a narrow analysis rule is accurate and contextually useful.
- [ ] **H3:** a recommendation measurably changes targeted behavior.
- [ ] **H4:** players return voluntarily because demonstrated value persists.

Do not report H1 as proof of tactical value, H2 as proof of behavior change, or H3 as proof of long-term retention.

---

## 16. Current progress snapshot

This snapshot must be updated only from verified repository state and recorded experiment results.

| Area | Current status |
|---|---|
| Product vision baseline | v1.0.0 |
| Active execution phase | H1 — Capture |
| H1 | TESTING |
| H2 | LOCKED — requires H1 PASS |
| H3 | LOCKED — requires H2 PASS |
| H4 | LOCKED — requires H3 PASS |
| Product-level validation | NOT YET ESTABLISHED |

**Current next step:** follow the root `PLAN.md` and complete the approved H1 capture implementation and validation. Do not treat this master checklist as authorization to start H2, H3, or H4.

**Source baseline:** `docs/PRODUCT_PLAN.md` v1.0.0 and the current root `PLAN.md`. If the product plan is revised, review this file for an explicit, traceable update rather than silently changing the roadmap.
