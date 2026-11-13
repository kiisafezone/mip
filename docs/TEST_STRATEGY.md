# Test Strategy

> **Current authority notice (2026-11-13):** This document is a legacy H1 test-strategy reference retained to describe the existing repository and its historical H1 work. It is not an independent product-scope, milestone, or implementation authority. The approved source of truth is [PRODUCT_PLAN v1.1.1](PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md). Current status remains **M0 unverified; Sprint 0 complete; M1 not started**. Where this document's older H1 framing conflicts with v1.1.1, v1.1.1 controls. This notice preserves the document's useful technical/history content; it does not resolve open decisions 11.35–11.40 or authorize work.

**Current test scope:** H1 — Capture  
**Execution authority:** [root PLAN.md](../PLAN.md)

## Test pyramid

1. **Shared domain tests:** model invariants, metadata validation, serialization,
   event validation, and deterministic timeline ordering.
2. **Shared contract tests:** event and export JSON round trips and
   forward-compatible payload handling.
3. **Local data instrumentation tests:** Room persistence, ordering, correction
   transactions/history, deletion, and cascading match deletion.
4. **Presentation tests:** ViewModel state transitions and input validation.
5. **Android instrumentation/build checks:** configured Android build, lint,
   persistence instrumentation, and critical persistence/recreation boundaries.

## H1 required cases

### Match records

- Required hero and role validation.
- Optional metadata handling, including mode, patch, duration, result, and notes.
- Non-negative start/end timestamp invariants and valid completed-record
  timestamp consistency in the domain model.
- Serialization round trip preserves current fields, including `durationMs`.
- Match create, read, update, and delete behavior through the repository.
- Saved match records remain available after normal app/database recreation.

### Event capture and timeline

- Reject malformed match-relative time input, negative time, invalid event
  metadata, and invalid confidence values.
- Preserve manual/player-reported source and validation metadata.
- Preserve unknown event type strings and optional payload keys where supported.
- Deterministic event ordering for out-of-order insertion and equal timestamps.
- Event timestamps are handled consistently against known match duration;
  unknown duration remains explicitly unavailable rather than guessed.
- Event create, read, correction, and delete behavior.
- Correction requires a nonblank reason and stores immutable before/after
  snapshots where revision history is supported.
- Correction writes are atomic with the event update.
- Event deletion and match deletion preserve the documented cascade behavior.

### Export and offline behavior

- Export JSON round trip and schema/version metadata.
- Export includes the selected match, supported events, provenance, and
  correction history.
- Export is initiated explicitly by the user.
- Core match capture and review paths do not require network access.

### Persistence and migration

- Room entity/DAO/repository round trips for sessions and events.
- Room schema matches the committed generated schema.
- Migration tests cover every supported prior schema version with populated
  records; destructive migration fallback remains disabled.
- Process/database recreation restores saved match records and event timelines.
- Persistence failures are surfaced without silently discarding user input.

## Deferred or separately gated tests

- Pause/resume/complete lifecycle transition behavior is not an H1 “Build only”
  requirement in the root `PLAN.md`. Keep transition-method tests deferred
  until a maintainer explicitly decides whether lifecycle controls belong in
  H1.
- Cooldown calculations, tactical verdicts, coaching recommendations,
  cross-match patterns, and behavior-change outcomes are not H1 tests. They
  require their own later-stage authorization and evaluation design.
- Live capture, OCR, overlays, hidden-state access, and gameplay automation
  remain prohibited under the current project policy.

## Quality principles

- Inject clocks and ID providers where practical to avoid flaky tests.
- Use deterministic synthetic fixtures only; never use real player data or
  unauthorized game assets in fixtures.
- Test both successful and failure paths, including persistence failures and
  user-input preservation.
- Do not claim process-death recovery, UI accessibility, or participant
  usability from unit tests alone.
- Do not claim tactical-analysis readiness from synthetic-only tests.
- CI success is necessary engineering evidence, not proof that H1 participant
  validation passed.
