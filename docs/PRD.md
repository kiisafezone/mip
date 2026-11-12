# Product Requirements Document — Release 1

**Product:** MIP — Mobile Legends Intelligence Platform
**Release:** 1 — Functional Match Review Foundation
**Platform:** Android
**Status:** Active implementation scope

## 1. Objective

Deliver a functional offline match-review workflow. A player can create a match,
record important events, review the event timeline, correct mistakes, retain
history locally, and export the record.

Release 1 is not a tactical coaching release and does not claim that the app
improves rank or win rate.

## 2. Target user

A Mobile Legends player who wants to manually record and review their own match
events without connecting MIP to the running game or depending on a server.

## 3. User outcomes

1. Record the basic identity and outcome of a match.
2. Capture 3–5 meaningful events with match-relative timestamps.
3. Review events in chronological order.
4. Correct inaccurate entries without losing the correction trail.
5. Reopen saved match records after app restart.
6. Export a portable copy when explicitly requested.

## 4. Functional requirements

- **FR-01 — Match creation:** hero and role are required; result, mode, and notes
  are recorded when known.
- **FR-02 — Match lifecycle:** sessions can be active, paused, resumed, and
  completed through explicit user actions.
- **FR-03 — Event capture:** manually record player deaths, objectives/contests,
  and tactical notes with match-relative timestamps.
- **FR-04 — Provenance:** retain source, confidence, validation status, and
  schema version for each event.
- **FR-05 — Timeline:** show events ordered by match time, recorded time, and
  stable event ID.
- **FR-06 — Correction:** allow event timestamp correction with a required
  reason and immutable before/after snapshots.
- **FR-07 — Deletion:** allow explicit deletion of events and match records;
  match deletion also deletes its local events and correction history.
- **FR-08 — Persistence:** store sessions, events, and revisions locally using
  Room; core workflows do not require network access.
- **FR-09 — Recovery:** active and paused sessions remain available after app
  process restart; the app does not invent elapsed game time.
- **FR-10 — Summary:** show recorded match metadata and event counts only.
- **FR-11 — Export:** export one match as a versioned JSON document, including
  provenance and correction history, after explicit user action.

## 5. Non-functional requirements

- No account, cloud sync, telemetry, or network dependency.
- Domain models and repository ports remain independent of Android APIs.
- Persistence schema changes use explicit, tested migrations.
- Corrections and exports preserve source and audit information.
- Unknown event type strings and optional payload keys are preserved.
- Invalid data is surfaced safely; never silently replaced with guessed values.
- UI clearly labels manual entries as player-reported.
- Touch targets, labels, and text remain usable on Android phone screens.

## 6. Release 1 non-goals

No cooldown estimates, tactical verdicts, coaching recommendations, cross-match
pattern detection, player scoring, live capture/OCR, overlays, hidden-state
access, enemy tracking, gameplay automation, accounts, cloud services, or social
features.

## 7. Acceptance criteria

A Release 1 build is complete only when all requirements in the root
[PLAN.md](../PLAN.md) Definition of Done are met, automated tests pass, the
Android build and lint pass, and the implementation has been reviewed before
merge.
