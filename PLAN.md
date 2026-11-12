# MIP — Release 1 Execution Plan

**Status:** Approved Release 1 execution baseline
**Release:** 1 — Functional Match Review Foundation
**Platform:** Android-first; local-first; offline-capable
**Authority:** This file defines the current implementation scope. [Product Plan](docs/PRODUCT_PLAN.md) remains the long-term product direction.

## Objective

Deliver a usable, player-controlled match review workflow that records manually supplied match information and events, preserves corrections and provenance, stores data locally, and supports timeline review and explicit export.

Release 1 is a functional foundation. It does not claim tactical accuracy, behavior change, or improved win rate.

## In scope

1. Create a match with hero, role, result, mode, duration/timing metadata, and optional notes when known.
2. Record 3–5 key moments, including structured player-death and objective/contest events with match-relative timestamps.
3. Preserve evidence source, confidence, validation state, and schema version.
4. Display a deterministic chronological event timeline.
5. Edit and delete matches and events. Corrections retain an auditable before/after record and a reason; deletion is explicit.
6. Persist sessions and events with Room on-device; work offline and recover saved sessions after process restart.
7. Provide a basic factual match summary and user-initiated versioned export.
8. Test validation, lifecycle, ordering, correction history, deletion, persistence, recovery, and export.
9. Keep product, architecture, privacy, and compliance documentation aligned with implemented behavior.

## Explicitly out of scope

- Tactical verdicts, coaching recommendations, cooldown estimates, and tactical scoring.
- Cross-match pattern detection and player progress analytics.
- Live match capture, OCR, overlays, screen scraping, memory inspection, packet interception, hidden-state inference, enemy tracking, gameplay automation, or game modification.
- Accounts, cloud sync, telemetry, social features, and remote storage.
- Any unsupported claim that a recorded event caused a match outcome.

## Evidence and uncertainty

- Manual entries must be labeled as player-reported in the UI; retain the existing MANUAL source enum for persisted compatibility.
- Synthetic data is test/demo data only and must be visibly distinguished.
- Never invent a timestamp. Missing or unknown times remain unavailable.
- Corrections must preserve the previous event snapshot, corrected snapshot, reason, and correction time.
- Unknown event types and payload keys must be preserved where possible; malformed known data must fail visibly and safely rather than be silently rewritten.

## Release 1 Definition of Done

- [ ] Create, list, open, edit, and delete match records.
- [ ] Add, edit, and delete structured events.
- [ ] Record player death and objective/contest timestamps without inference.
- [ ] Timeline is deterministic: game time, recorded time, stable ID.
- [ ] Corrections are auditable.
- [ ] Room persistence and migrations are implemented and tested.
- [ ] Data survives process restart; interrupted active sessions are recoverable.
- [ ] Core workflows work without network access.
- [ ] Basic summary contains recorded facts only.
- [ ] Explicit export creates a versioned, documented file containing provenance.
- [ ] UI clearly distinguishes player-reported data from derived information.
- [ ] Unit and instrumentation tests cover the critical paths.
- [ ] Android build, lint, and configured CI checks pass.
- [ ] Policy/compliance and privacy documentation are accurate.

## Delivery sequence

1. Repository and documentation alignment; preserve the former H1 plan.
2. Domain model, lifecycle, event correction history, and repository ports.
3. Room schema, DAOs, repositories, migration/recovery tests.
4. Android match list/create/detail/timeline flows.
5. Versioned export and factual summary.
6. Verification, CI, accessibility review, and release notes.

Each step must remain reviewable. Do not merge the pull request without explicit maintainer approval.
