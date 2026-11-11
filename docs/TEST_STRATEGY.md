# Test Strategy

## Test pyramid
1. Shared domain unit tests: fast, deterministic, no Android runtime.
2. Engine tests: event ordering and cooldown calculations using synthetic fixtures.
3. Data tests: Room round-trip, migrations, repository behavior.
4. Presentation tests: ViewModel transitions and validation.
5. Instrumentation: critical Android flows and process recreation.

## Required cases
- Session lifecycle: create, pause, resume, complete.
- Event validation: missing IDs, negative time, invalid confidence, unknown enum.
- Timeline ordering: out-of-order insertion and equal timestamps.
- Cooldown: exact version match, missing version, correction, rejected trigger.
- Persistence: save/load after restart.
- Migration: upgrade from each supported schema version.
- Offline: MVP flows succeed without network.

## Quality principles
Inject a clock to avoid flaky time-dependent tests. Use deterministic UUID
providers where useful. Never use real player data or unauthorized game assets
in fixtures. Do not claim computer-vision readiness from synthetic-only tests.