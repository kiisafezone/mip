# MIP — Execution Plan

**Status:** H1 capture validation — approved execution baseline  
**Current stage:** H1 — Capture  
**Next step:** Build and test the smallest post-match capture flow  
**Long-term vision:** [docs/PRODUCT_PLAN.md](docs/PRODUCT_PLAN.md)

## Goal

Test whether active ranked MLBB players will record matches consistently when post-match capture is fast and low-friction.

**A passing H1 means only "capture validated." It does not mean MIP's analysis is accurate, changes behavior, or delivers lasting product value.**

## Hypothesis status

- H1 Capture: **TESTING**
- H2 Understanding: **LOCKED until H1 passes**
- H3 Behavior: **LOCKED until H2 passes**
- H4 Retention: **LOCKED until H3 passes**

**Rule:** passing a stage only unlocks the next test. It is never evidence that a later hypothesis is true.

## H1 build scope

Build only:
- Create a match record: hero, role, result, and essential match metadata.
- Record 3–5 key moments, including structured player deaths and objective events with match-relative timestamps.
- Show a simple chronological event timeline.
- Edit and delete a match and its events.
- Save locally; core capture and review work offline.
- Export the recorded match data on explicit user request.

Do not build coaching, tactical verdicts, cross-match patterns, cooldown intelligence, elaborate summaries, accounts, cloud sync, telemetry, live capture, or overlays for H1.

Capture only fields with a defined H1 or H2 evaluation purpose. In particular, structure death timestamps and Turtle objective/contest timestamps so the candidate H2 rule can be evaluated retrospectively without adding a new capture burden. A missing contest marker must remain missing; do not infer it.

## H1 validation protocol

### Participants

Recruit **5 active ranked MLBB players** who are not participating merely to please the developer. Record ranked-play activity and prior familiarity with match tracking. This is a sanity check for obvious usability/value problems, not statistical proof or product-market-fit evidence.

### Capture-time definition

Start the timer when the participant opens **New Match**. Stop when the match record is successfully saved. Include all required entry and event-recording steps; exclude unrelated discussion and interruptions.

Measure first-time and repeat captures separately. Report individual times, median first-time duration, and median repeat duration. The target is **under 3 minutes**. Do not hide slow participants by reporting only the fastest or best sessions.

### Completion and return behavior

- Observe whether participants complete at least 3 match records.
- After initial onboarding, do not remind or prompt participants to record another match.
- Count separately how many participants voluntarily return and log at least one additional match without a reminder.
- Record total completed matches per participant and voluntary return timing where practical.
- Unprompted return is a stronger signal than a positive survey response.

### Post-timeline question

After a participant reviews their own timeline, ask exactly:

> Did reviewing this change what you'd do next match? What specifically?

Record the answer verbatim. Do not convert a generic "yes" into evidence of learning. Categorize whether the answer identifies a specific intended behavior change tied to an event in that participant's timeline. H1 still does not prove that the behavior later changed.

### H1 decision criteria

**Pass — capture validated:** all of the following:
- At least 3 of 5 participants complete at least 3 match records.
- Median repeat capture time is under 3 minutes.
- At least 3 of 5 participants voluntarily return and log another match without prompting.

**Fail — simplify and retest before any H2 work:** either:
- Fewer than 3 of 5 participants complete at least 3 match records; or
- Median first-time capture duration exceeds 5 minutes; or
- Median repeat capture duration exceeds 5 minutes.

Report first-time and repeat medians separately. This prevents a fast repeat flow from hiding a first-time onboarding failure. A pass requires a repeat median under 3 minutes and a first-time median no greater than 5 minutes.

**Inconclusive / revise:** results meet neither the pass nor failure definition—for example, capture is quick but voluntary return is below 3 of 5. Investigate the observed friction or lack of recurring motivation, revise the flow or test design, and run another H1 test. Do not advance to H2 on an inconclusive result.

Always report:
- Participants recruited and their ranked-play context.
- Participants completing 3+ matches.
- Participants voluntarily returning without prompts.
- First-time and repeat capture durations (individual values and medians).
- Abandonment points and assistance required.
- Verbatim answers to the post-timeline question.
- Specific behavior-change intentions grounded in a timeline event.
- Deviations from this protocol and any missing data.

## H2 pre-registration — locked

Candidate rule for a future experiment:

> Flag a player's recorded death if its match-relative timestamp falls within the 30 seconds immediately before that player's manually recorded Turtle contest start.

This is a temporal relationship only. It must not be described as causing a lost Turtle, proving a late rotation, or establishing a bad engagement.

Before H2 implementation, pre-register:
- Exact definitions of "player death", "Turtle contest start", eligible match phase, and the 30-second interval (inclusive/exclusive boundaries).
- Required evidence and exclusions, including missing, corrected, contradictory, or uncertain timestamps.
- What counts as a true positive, false positive, false negative, and insufficient evidence.
- How the player and an experienced MLBB reviewer independently adjudicate flagged and sampled unflagged situations.
- Minimum sample size and the pass threshold. A proposed starting threshold is at least 70% of adjudicated flagged situations confirmed as genuinely meeting the rule and contextually represented accurately; this threshold must be finalized before H2 is built.
- How disagreements, reviewer uncertainty, and insufficient evidence are reported.

Do not treat player agreement alone as ground truth. If there are too few eligible situations to evaluate the rule, H2 is inconclusive, not a pass.

## H1 Definition of Done

- [ ] A participant can create a match with hero, role, result, and essential metadata.
- [ ] A participant can record 3–5 key moments with structured event types and match-relative timestamps.
- [ ] Death and Turtle objective/contest events are structured sufficiently for the candidate H2 rule; unknown times remain unknown.
- [ ] The timeline displays recorded events in deterministic chronological order.
- [ ] Match and event records can be edited and deleted.
- [ ] Records survive app restart and core flows work offline.
- [ ] Explicit export produces usable, documented match data.
- [ ] UI distinguishes user-reported entries from derived information; H1 makes no tactical verdicts.
- [ ] Automated tests cover validation, timestamp handling, ordering, persistence, edit/delete, and export.
- [ ] Android build and configured CI checks pass.
- [ ] The H1 participant protocol is completed and results are recorded against the predefined criteria.
- [ ] The outcome is explicitly labeled PASS, FAIL, or INCONCLUSIVE; no H2 implementation begins without an H1 PASS.

## Technical decision checkpoint

Before expanding implementation, verify which existing Kotlin Multiplatform modules are actually needed by this Android-first H1. Compare keeping the shared core with a simpler Android-only structure using delivery effort, testability, and migration cost. Do not rewrite the architecture solely for preference; document the decision and its evidence. Keep local persistence and domain logic appropriately separated without adding abstractions that H1 does not need.

## Working rules

- This file is the authority for current scope, stage status, next step, and Definition of Done.
- docs/PRODUCT_PLAN.md is the long-term vision, not authorization to build future stages.
- No H2/H3/H4 implementation while the relevant stage is locked.
- No live-game capture, OCR, overlays, memory/packet access, hidden-state inference, or game automation. Follow docs/POLICY_AND_COMPLIANCE.md.
- Keep changes small and reviewable. Do not merge a pull request without explicit maintainer approval.
