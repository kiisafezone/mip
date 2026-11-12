# MIP — Mobile Legends Intelligence Platform

MIP is an independent, Android-first match review and tactical learning platform.
Release 1 provides a player-controlled way to record match details and key
moments, review a chronological timeline, preserve corrections, store records
locally, and export them on request.

**Release 1 does not provide tactical verdicts or claim to improve win rate.**
It establishes reliable match records for later, separately validated releases.

## Current release: Release 1 — Functional Match Review Foundation

- Create, review, edit, and delete match records.
- Record player-reported death, objective, and tactical-note events with match time.
- Review a deterministic chronological event timeline.
- Preserve event corrections with before/after snapshots and a reason.
- Store match records on-device using Room; no account or network is required.
- Export one match and its event/provenance history as versioned JSON.

## Product roadmap

1. **Release 1 — Functional Match Review Foundation:** structured manual capture, timeline, local history, correction, and export.
2. **Release 2 — Tactical Intelligence:** evidence-based tactical analysis and supported timing intelligence.
3. **Release 3 — Cross-Match Intelligence:** recurring patterns, comparisons, and measured progress.
4. **Release 4 — Personal Improvement System:** priorities, practice objectives, and reassessment.

Only Release 1 is in the current implementation scope. See [PLAN.md](PLAN.md)
and [Release 1 execution checklist](docs/releases/RELEASE_1_EXECUTION.md).

## Architecture

- Android: Kotlin, Jetpack Compose, lifecycle-aware presentation.
- Shared domain/contracts/engine: Kotlin Multiplatform; domain logic stays independent of Android.
- Local data: Room database and repository adapters.
- Offline-first: no backend, account, telemetry, or live-game connection.

See [Architecture](docs/ARCHITECTURE.md), [Android setup](docs/ANDROID_SETUP.md),
and [CI](docs/CI.md).

## Compliance boundary

MIP does not modify MLBB, automate gameplay, inspect process memory, intercept
packets, read hidden game state, or provide live overlays. All Release 1 match
information is entered by the player. See
[Policy and Compliance](docs/POLICY_AND_COMPLIANCE.md).

MIP is independent and is not affiliated with, endorsed by, or sponsored by
Moonton or Mobile Legends: Bang Bang.

## License

Original MIP code and documentation are licensed under Apache-2.0. This does
not license third-party game assets, trademarks, or publisher content. See
[LICENSE](LICENSE) and [Licensing scope](docs/LICENSING.md).
