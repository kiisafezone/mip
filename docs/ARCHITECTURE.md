# MIP architecture

## Module map

MIP keeps product rules independent from Android APIs. Android is the first host,
but the shared source sets are Kotlin Multiplatform commonMain code.

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

## Shared-core guard

Run:

~~~sh
./gradlew verifySharedCoreNoAndroidImports
~~~

The root Gradle task scans Kotlin files under shared/ and fails if a source
file imports android.* or androidx.*. Android module preBuild tasks depend on
this guard, so the check runs as part of an Android build.

## Persistence boundary

data:local currently contains only a persistence-shaped record and mapping
functions. Room entities, DAOs, migrations, and the concrete
MatchSessionRepository adapter are intentionally deferred to the local
persistence issue. No Room dependency is introduced into domain or shared core.

## Scope boundaries

- Match event schemas and event validation belong to the match-contract issue.
- Versioned game-mechanics definitions belong to the game-data issue.
- Cooldown estimation behavior belongs to engine implementation work.
- Live capture, overlays, hidden-state inference, and game automation remain
  blocked by project policy until exact written authorization exists.
