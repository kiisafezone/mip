# Product Requirements Document (PRD)

> **Current authority notice (2026-11-13):** This document is a legacy H1 product-requirements reference retained to describe the existing repository and its historical H1 work. It is not an independent product-scope, milestone, or implementation authority. The approved source of truth is [PRODUCT_PLAN v1.1.1](PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md). Current status remains **M0 unverified; Sprint 0 complete; M1 not started**. Where this document's older H1 framing conflicts with v1.1.1, v1.1.1 controls. This notice preserves the document's useful technical/history content; it does not resolve open decisions 11.35–11.40 or authorize work.

**Product:** MIP — Mobile Legends Intelligence Platform  
**Document scope:** H1 — Capture  
**Historical status snapshot:** H1 implementation and validation in progress; not the current milestone status  
**Current product authority:** [locked v1.1.1 plan](PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md)

## 1. Objective

Test whether active ranked Mobile Legends: Bang Bang players will consistently
record matches when post-match capture is fast and low-friction.

H1 delivers a player-controlled, local-first workflow for recording match
metadata and a small number of key moments, reviewing a chronological timeline,
and retaining or exporting the player's own records.

An H1 pass means only that capture has been validated against the protocol in
the root `PLAN.md`. It does not establish tactical accuracy, behavior change,
improved rank or win rate, retention, or product-market fit.

## 2. Target user

An active ranked MLBB player who wants to record and review their own match
events after a match without connecting MIP to the running game or depending on
a server.

## 3. H1 user outcomes

1. Create a match record with hero, role, result, and essential metadata.
2. Record 3–5 meaningful moments, including structured player-death and
   objective/contest events with match-relative timestamps when known.
3. Review recorded events in chronological order.
4. Correct or delete mistaken event entries and edit or delete match records.
5. Reopen saved match records after normal app restart.
6. Export a match record when explicitly requested.

## 4. Functional requirements

- **FR-01 — Match record:** create a match record with hero, role, result, and
  essential metadata. Optional details such as mode, patch, duration, and notes
  are recorded when known and useful to H1 evaluation.
- **FR-02 — Event capture:** manually record 3–5 key moments, including
  structured player deaths and objective/contest markers with match-relative
  timestamps. Unknown times remain unknown; the app must not infer a missing
  contest marker.
- **FR-03 — Provenance:** preserve the fact that H1 event entries are
  player-reported/manual records. Do not present them as independently verified
  observations or automated game data.
- **FR-04 — Timeline:** display recorded events in deterministic chronological
  order, including a stable tie-break for equal timestamps.
- **FR-05 — Correction and deletion:** allow the player to correct or delete
  event entries and to edit or delete a match record. Where correction history
  is supported, retain the before/after record and the stated reason.
- **FR-06 — Local persistence:** retain match records and events locally across
  normal app restarts.
- **FR-07 — Offline operation:** core H1 capture and review workflows do not
  require network access.
- **FR-08 — Export:** export recorded match data on explicit user request in a
  documented, versioned format.
- **FR-09 — Evidence labeling:** distinguish player-entered information from
  derived information; H1 must not produce tactical verdicts or coaching.

Session lifecycle transition controls (pause, resume, and complete) are not
included in the root `PLAN.md` H1 “Build only” scope. Their implementation is
deferred pending a maintainer decision; their presence in earlier draft
requirements does not independently authorize H1 scope expansion.

## 5. Non-functional requirements

- No account, cloud sync, telemetry, or network dependency for H1.
- Keep domain logic independent of Compose, Android framework, and persistence
  implementation.
- Keep local persistence versioned and use explicit, tested migrations.
- Preserve event source, confidence/validation metadata, and unknown optional
  payload fields where supported by the data contract.
- Surface invalid or unavailable data safely; never silently replace it with
  guessed values.
- Label manual entries as player-reported.
- Keep touch targets, labels, and text usable on Android phone screens.
- Minimize collected data to fields with a defined H1 or H2 evaluation purpose.

## 6. H1 non-goals

H1 does not include cooldown calculations or intelligence, tactical verdicts,
coaching recommendations, cross-match pattern detection, player scoring,
elaborate summaries, live capture/OCR, overlays, hidden-state access, enemy
tracking, gameplay automation, accounts, cloud services, telemetry, or social
features.

No claim of official MLBB affiliation or endorsement is made.

## 7. Validation and success criteria

H1 is evaluated only through the participant protocol and explicit
PASS/FAIL/INCONCLUSIVE criteria in the root [PLAN.md](../PLAN.md). The PRD does
not redefine those thresholds.

Report capture completion, first-time and repeat capture durations, voluntary
return behavior, abandonment/assistance, verbatim participant answers,
protocol deviations, and missing data as required by `PLAN.md`.

An H1 PASS validates capture only. H2 remains locked until the root plan records
an explicit H1 PASS and the required maintainer authorization is obtained.

## 8. Release and scope gates

The root `PLAN.md` is the sole authority for current H1 scope, stage status,
validation protocol, next step, and Definition of Done. The broader product
roadmap in `docs/PRODUCT_PLAN.md` is long-term direction, not permission to
build future-stage capabilities.

Do not start H2, H3, or H4 implementation while those stages remain locked.
Any proposed scope change must be reconciled with `PLAN.md` and reviewed
before implementation.
