> **Product-plan authority (2026-11-13):** The current approved source of truth is [`docs/PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md`](docs/PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md). The project-decision bullets below are a historical repository snapshot and do not authorize implementation. The prior locked baseline is preserved at [`docs/archive/PRODUCT_PLAN_V.1.1.0_LOCKED_SOT.md`](docs/archive/PRODUCT_PLAN_V.1.1.0_LOCKED_SOT.md). Follow the current plan's milestone, scope, and approval gates.

---

# MIP — Mobile Legends Intelligence Platform

An Android-first, open-source tactical learning and match-analysis platform.
MIP is designed as a local-first coaching tool. It does not modify MLBB, automate
gameplay, inspect process memory, intercept packets, or reveal hidden game state.

## Project decisions
- Solo-maintained, open-source project
- Android first; Kotlin, Jetpack Compose, Kotlin Multiplatform shared core
- Local-first and offline-capable; backend deferred
- MVP: match sessions, manual event logging, versioned cooldown estimates,
  event timeline, local history, and basic reports
- Live-match integrations and overlays are blocked pending explicit written authorization

## Planned layout
```text
apps/android/       Android app, UI, and feature modules
shared/             domain, contracts, engine, synthetic test fixtures
data/local/         Room persistence adapter
data/game-data/     versioned mechanics definitions with provenance
docs/               product, technical, architecture, policy, and sprint documentation
.github/            issue templates and CI workflows
```

## Android build

See [Android setup and build instructions](docs/ANDROID_SETUP.md). With JDK 17
and Android SDK Platform 36 installed, run:

```sh
./gradlew assembleDebug
```

See [CI documentation](docs/CI.md) for automated verification and the
[contribution guide](CONTRIBUTING.md) for development workflow.

## Compliance boundary

Until Moonton explicitly authorizes a specific method and use in writing, do not
implement automated live-match capture/OCR, overlays, hidden enemy tracking,
memory or packet inspection, game modification, or input automation. Android
permissions are not publisher authorization. See
[Policy and Compliance](docs/POLICY_AND_COMPLIANCE.md).

MIP is independent and is not affiliated with, endorsed by, or sponsored by Moonton
or Mobile Legends: Bang Bang.

## License

Original MIP code and documentation are licensed under Apache-2.0. This does not
license third-party game assets, trademarks, or publisher content. See
[LICENSE](LICENSE) and [Licensing scope](docs/LICENSING.md).
