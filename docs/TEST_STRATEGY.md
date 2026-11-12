# Test Strategy

## Test pyramid

1. Shared domain tests: invariants, session lifecycle, event validation, timeline ordering.
2. Shared contract tests: event and export JSON round trips, forward-compatible payloads.
3. Local data instrumentation tests: Room persistence, ordering, correction transaction,
   event deletion, and match cascade deletion.
4. Presentation tests: ViewModel state transitions and validation.
5. CI build/lint: Android assembly, lint, shared-core boundary, and all configured tests.

## Release 1 required cases

- Match metadata validation and serialization.
- Session create, pause, resume, and complete transitions.
- Event validation, match-relative timestamp parsing, and deterministic ordering.
- Unknown event type and optional payload-key preservation.
- Correction requires a reason and stores before/after snapshots.
- Correction write is atomic with the event update.
- Event deletion leaves its revision history while the match exists.
- Match deletion cascades to events and revisions.
- Saved records remain available after app/database recreation.
- Export includes the match, events, provenance, and revisions.
- Core match review works without a network dependency.

## Quality principles

Inject clocks and ID providers in tests where practical. Use deterministic
synthetic fixtures only; never use real player data or unauthorized game
assets. Do not claim tactical-analysis readiness from Release 1 tests.
