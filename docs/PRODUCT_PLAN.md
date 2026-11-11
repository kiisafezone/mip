# MIP — Product Vision & Long-Term Plan

**Document status:** Governing product vision; hypotheses, not a delivery commitment  
**Version:** 1.0.0  
**Platform:** Android-first  
**Product:** Mobile Legends Intelligence Platform

## 1. Product thesis

Players can see match outcomes, but often struggle to understand the decisions and situations that produced them. MIP aims to help players move from match records to evidence-grounded tactical understanding and, eventually, better in-game decisions.

MIP is an independent, player-controlled companion for reviewing matches. It is not a gameplay automation tool, a source of hidden information, or a substitute for player judgment.

**Core problem:** players know that a match went badly, but may not know which specific, reviewable situations contributed to the outcome or what they could change next time.

The product thesis is a hypothesis to validate—not a claim that MIP already improves performance.

## 2. Users and problems

MIP may serve:
- Casual players who want to improve but do not know what to practice.
- Competitive and ranked players who want structured review.
- Solo-queue players who want to examine their own decisions without depending on teammates' explanations.
- Aspiring high-rank players who want to identify recurring, actionable mistakes.

The problems MIP may address:
1. **Unactionable feedback:** a result or KDA does not explain the decisions behind it.
2. **Forgotten events:** important deaths, spell uses, rotations, and objective moments are hard to recall later.
3. **Timing awareness:** players may not remember relevant cooldown and objective timings.
4. **Repeated mistakes:** similar situations may recur across matches without being recognized.
5. **Limited self-analysis:** aggregate statistics rarely explain the context of an individual decision.
6. **Fragmented review:** match notes, timelines, statistics, and learning material are scattered.

These are problem hypotheses. Research and testing may narrow or change the target problem.

## 3. Product principles

1. **Evidence before advice.** Never present an inference or estimate as an observed fact.
2. **Narrow rules before broad coaching.** A small, testable rule is preferable to vague tactical scoring.
3. **Player-controlled capture.** The player decides what to record and when.
4. **Transparent uncertainty.** Show source, confidence, limitations, and unavailable states.
5. **Local-first by default.** V1 works offline and does not require an account or backend.
6. **No hidden advantage.** MIP must not expose information unavailable to the player through permitted play.
7. **Earn every release.** A capability is built only when the preceding hypothesis has passed.
8. **Measure behavior, not enthusiasm alone.** Stated interest and novelty are weaker evidence than repeated voluntary use and observed behavior change.

## 4. Evidence model

Every input and conclusion must preserve its evidence category:

| Category | Meaning | Example |
|---|---|---|
| OBSERVED | Directly recorded observation with a traceable source | A player-entered death timestamp |
| USER-REPORTED | A statement or event entered by the player | Player's note about a rotation |
| DERIVED | Deterministic result computed from recorded inputs | A death occurred within 30 seconds before a recorded objective contest |
| INFERRED | Interpretation that depends on context or a rule | A situation may indicate a positioning issue |
| ESTIMATED | Approximation based on documented/versioned data | Estimated cooldown-ready time |
| SYNTHETIC | Fictional data used only for tests or demonstrations | Test match fixture |
| UNAVAILABLE | Evidence is missing, stale, contradictory, or insufficient | No reliable timestamp for a required event |

Derived and inferred outputs must link to their supporting inputs and rule version. A temporal correlation is not proof of causation. When evidence is insufficient, MIP must say so rather than guess.

## 5. Hypothesis-led product progression

The stages below describe possible future capabilities. They are not promises or automatic release commitments.

| Stage | Hypothesis | Potential capability | Unlock condition |
|---|---|---|---|
| H1 — Capture | Players will consistently record matches when capture is fast and useful enough | Match record, simple event timeline, edit/delete, local save, export | H1 validation protocol passes |
| H2 — Understanding | A narrowly defined tactical rule can produce accurate, contextually useful conclusions | One pre-registered tactical rule with evidence and limitations | H1 passes; H2 rule and evaluation are pre-registered |
| H3 — Behavior | A recommendation based on credible analysis changes a targeted player behavior | One targeted, measurable recommendation | H2 passes; behavioral test is pre-registered |
| H4 — Retention | Players continue using MIP because it provides recurring value | Repeat review and improvement workflow | H3 passes; retention test is pre-registered |

**Progression rule:** passing one stage only unlocks the next test. It is never evidence that a later hypothesis is true.

H1 passing means **capture validated**, not product validated. H2 agreement does not establish behavior change. H3 behavior change does not by itself establish durable retention.

### H1 — Capture

The first build is intentionally small:
- Create a match record with hero, role, result, and essential match metadata.
- Record 3–5 key moments, including structured death and objective events with match-relative timestamps.
- Review a simple chronological timeline.
- Edit and delete records.
- Save locally and work offline.
- Export recorded match data on explicit user request.

No coaching, tactical verdicts, cross-match pattern detection, or elaborate summaries are required for H1.

The detailed participant protocol, measurements, pass/fail rules, and Definition of Done live in the root [PLAN.md](../PLAN.md). H1 results must be reported as capture validation only.

### H2 — Understanding

H2 tests one tactical rule, not a general coaching engine. A candidate experiment is identifying a player's death within 30 seconds before a manually recorded Turtle contest. The exact event definitions, eligible cases, exclusions, false-positive/false-negative adjudication, minimum sample, and pass threshold must be registered before H2 implementation.

The output must describe the recorded temporal relationship—not claim that the death caused the objective loss, prove a late rotation, or label an engagement as bad without validated evidence.

A domain-knowledgeable MLBB reviewer should review the rule and disputed cases. Player agreement is necessary feedback, but must not be treated as objective ground truth by itself.

### H3 — Behavior

H3 tests one recommendation and one observable target behavior. It compares later matches against a pre-defined baseline, uses comparable situations where possible, and records confounders and sample limitations. A stated intention to improve is not a behavior-change result.

### H4 — Retention

H4 tests whether players continue voluntarily after novelty fades and because they perceive recurring value. Journaling habit, reminders, and social pressure must be distinguished from use motivated by useful analysis.

## 6. Data capture and future intelligence

H1 should capture only a small number of fields that make later evaluation possible without increasing capture burden unnecessarily:
- Match identity, duration, mode/patch when known, hero, role, and result.
- Event type and match-relative timestamp.
- Objective type and timestamp, including a clearly defined contest/engagement marker if the H2 rule requires it.
- Event source, confidence/validation status, and optional player context.
- Record creation/edit history sufficient to distinguish original entry from later correction.

Do not collect fields merely because they might someday be useful. Every field must have a current product or evaluation purpose.

## 7. Data acquisition, privacy, and compliance

MIP remains usable without connecting to, observing, or controlling the running game.

Permitted initial direction:
- Manual player-entered records.
- Synthetic fixtures clearly labeled as fictional.
- Documented/versioned mechanics data only after provenance, rights, and permission review.
- Local processing, local history, and explicit user-controlled export.

Do not implement automated live capture/OCR, overlays, memory or packet inspection, game modification, input automation, hidden-state inference, or automated enemy tracking unless exact written authorization and the project's separate policy review explicitly permit the specific method, data, purpose, platform, and distribution model.

Android permissions, public availability, community practice, and app-store approval are not substitutes for publisher authorization. Follow [Policy and Compliance](POLICY_AND_COMPLIANCE.md). Derived outputs inherit the restrictions and provenance of their inputs.

Minimize personal data. Explain what is stored and exported. Provide user control over correction and deletion. Cloud sync, accounts, telemetry, and sharing are not V1 requirements and require separate product, privacy, and security review.

## 8. Technical direction

The product is Android-first and local-first. The existing repository uses Kotlin, Jetpack Compose, and a Kotlin Multiplatform shared core. Retain or simplify that structure based on measured V1 delivery value—not architectural preference.

Before expanding the stack:
- Confirm which shared-core modules are actually consumed by V1.
- Compare the cost of keeping the shared core with the cost and risk of an Android-only implementation.
- Keep domain logic independent of UI and persistence where that separation provides practical value.
- Avoid adding Room, abstractions, modules, or dependencies without a concrete V1 requirement.
- Preserve deterministic tests and clear persistence boundaries.

This document does not mandate a backend, account system, live integration, or cross-platform release.

## 9. Explicit non-goals unless separately validated and approved

- A full tactical coaching engine in the first build.
- Broad tactical ratings or unsupported labels such as "bad player", "bad rotation", or "late rotation".
- Cross-match pattern claims without adequate player-specific data.
- Automatic match capture or live-game assistance.
- Hidden enemy tracking or information not legitimately available to the player.
- Social network, matchmaking, leaderboard, cloud account, or paid service.
- Claims of improved win rate or rank without an appropriate evaluation.

## 10. Product quality principles

- A player can understand where each displayed fact came from.
- Corrections do not silently erase the distinction between original and corrected information.
- Missing or contradictory evidence produces an explicit unavailable state.
- Timelines are deterministic and chronological.
- Local data survives normal app restarts and is exportable by the user.
- Core workflows work offline.
- Tactical rules are versioned, testable, reviewed, and explainable.
- No release presents correlation as causation or estimates as facts.

## 11. Governance and decision rules

Keep governance lightweight:
- One concise execution plan defines the current hypothesis, scope, next step, and Definition of Done.
- Each hypothesis has a pre-registered test and explicit pass/fail/inconclusive outcome.
- Scope changes are recorded in the plan before implementation.
- Compliance-sensitive features require a documented review before implementation.
- A plan approval is not an implementation or merge approval.
- A passing experiment unlocks a test, not an automatic feature expansion.

## 12. Success is evidence, not a feature count

MIP is successful only if its hypotheses are supported by observed results:
- H1: players reliably capture matches with acceptable effort.
- H2: a narrow analysis rule is accurate and contextually useful.
- H3: recommendations measurably change targeted behavior.
- H4: players return voluntarily because the demonstrated value persists.

Do not report H1 as proof of tactical value, H2 as proof of behavior change, or H3 as proof of long-term retention.

---

**Current execution authority:** the root [PLAN.md](../PLAN.md) is the sole source for the active build scope and current hypothesis status. If this vision conflicts with the execution plan, do not silently expand scope; revise the execution plan and obtain maintainer approval first.
