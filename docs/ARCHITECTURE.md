# MIP architecture

## Module map

MIP keeps domain rules independent from Android APIs. Android is the first host;
shared source sets use Kotlin Multiplatform commonMain code with Android and JVM
targets.

| Module | Responsibility | May depend on |
|---|---|---|
| shared:domain | Core entities, invariants, repository/export ports | Kotlin standard library |
| shared:contracts | Versioned interchange and export contracts | shared:domain |
| shared:engine | Platform-neutral deterministic analysis | shared:domain, shared:contracts |
| apps:android:presentation | Compose screens, UI state, ViewModel | shared:domain |
| data:local | Room entities, DAOs, database, repository and export adapters | shared:domain, shared:contracts, Android/Room |
| apps:android | Android composition root | presentation, data:local |

## Dependency direction

~~~text
apps:android
  ├── apps:android:presentation ──> shared:domain
  ├── data:local ─────────────────> shared:domain + shared:contracts
  └── shared APIs

data:local ──implements──> shared:domain repository/export ports
shared:domain -X-> Android, Compose, Room, network, or storage frameworks
shared:contracts -X-> Android or persistence frameworks
shared:engine -X-> Android, Compose, Room, network, or storage frameworks
~~~

The app module is the composition root: it creates the Room repository and
export adapter, then injects domain-facing interfaces into the presentation
ViewModel. UI code does not access Room entities or DAOs directly.

## Release 1 data flow

1. Compose screens collect immutable UI state from MatchSessionViewModel.
2. The ViewModel validates user input and invokes domain repository ports.
3. RoomMatchRepository maps domain records to Room rows and persists them.
4. Event corrections update the current event and append the revision snapshot
   in one database transaction.
5. MatchExportService assembles the versioned export contract from the local
   repositories after explicit user action.

## Shared-core verification

Run:

~~~sh
./gradlew verifySharedCoreNoAndroidImports
./gradlew :shared:domain:compileKotlinJvm :shared:contracts:compileKotlinJvm :shared:engine:compileKotlinJvm
~~~

The root Gradle task rejects Android framework imports from shared Kotlin files.

## Persistence boundary

Room and Android storage APIs are confined to data:local. Domain repository
interfaces and export ports are platform independent. The Room schema is
versioned; later schema changes require explicit tested migrations.

## Scope boundaries

Release 1 records and reviews player-entered data only. Tactical analysis,
cooldown calculations, cross-match intelligence, live capture, overlays,
hidden-state inference, and game automation remain outside this release.
