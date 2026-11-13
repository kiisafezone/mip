# MIP architecture

> **Authority notice (2026-11-13):** This file describes the architecture currently present in the repository; it is not the complete target architecture or an independent authorization to implement future milestones. The approved source of truth is [PRODUCT_PLAN v1.1.1](PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md). Current status remains **M0 unverified; Sprint 0 complete; M1 not started**. The repository's Kotlin Multiplatform modules align with the v1.1.1 shared-intelligence technology baseline. Decision 11.39 defers specification/investigation of desktop persistence, how the Tauri application consumes the shared Kotlin core, and whether event contracts are shared as code or schema. It does not authorize a bridge, a new persistence technology, or a technology change. No desktop implementation is authorized by this document.

## Module map

MIP keeps product rules independent from Android APIs. Android is the first host,
but the shared source sets are Kotlin Multiplatform commonMain code with Android
and JVM targets.

| Module | Responsibility | May depend on |
|---|---|---|
| shared:domain | Core entities, invariants, and repository ports | Kotlin standard library |
| shared:contracts | Versioned boundary contracts for data exchanged between modules | Kotlin standard library |
| shared:engine | Platform-neutral deterministic analysis entry points | shared:domain, shared:contracts |
| apps:android:presentation | Android-facing presentation models and UI adapters | shared:domain |
| data:local | Local persistence records, Room database/DAO implementations, and repository adapters | shared:domain, Android/Room |
| apps:android | Android composition root and Compose screens | Presentation, local data, shared APIs |

## Dependency direction

Dependencies point inward toward stable domain abstractions:

~~~text
apps:android
  ├── apps:android:presentation ──> shared:domain
  ├── data:local ─────────────────> shared:domain
  ├── shared:engine ──────────────> shared:domain + shared:contracts
  └── shared:domain

shared:domain    -X-> Android, Compose, Room, network, or storage frameworks
shared:contracts -X-> Android or persistence frameworks
shared:engine   -X-> Android, Compose, Room, network, or storage frameworks
~~~

The domain owns repository interfaces (ports); adapters implement those
interfaces in platform/data modules. UI code must not access Room or persistence
records directly. The app module is the composition root that wires adapters to
domain-facing consumers.

## Shared-core verification

Run:

~~~sh
./gradlew verifySharedCoreNoAndroidImports
./gradlew :shared:domain:compileKotlinJvm :shared:contracts:compileKotlinJvm :shared:engine:compileKotlinJvm
~~~

The root Gradle task scans Kotlin files under shared/ and fails if a source
file imports android.* or androidx.*. Android module preBuild tasks depend on
this guard, so the check runs as part of an Android build. The JVM targets allow
the shared modules to compile independently of the Android toolchain.

## Persistence boundary

`data:local` is the Android persistence adapter. H1 wires Room entities, DAOs,
the database, and `RoomMatchRepository` to the shared domain repository ports.
Room and Android dependencies remain confined to `data:local`; shared domain,
contracts, and engine code remain platform-neutral.

The initial database schema is version 1. The readable SQL companion is
`data/local/schema/001_initial.sql`. KSP schema export is configured under
`data/local/schemas`; generated Room schema output must be produced by the
Gradle build and reviewed before a production release. No destructive migration
fallback is enabled.

## Scope boundaries

- Match event schemas and event validation belong to the match-contract issue.
- Versioned game-mechanics definitions belong to the game-data issue.
- Cooldown estimation behavior belongs to engine implementation work.
- Live capture, overlays, hidden-state inference, and game automation remain
  blocked by project policy until exact written authorization exists.
