# Android development setup

## Pinned toolchain

| Component | Version |
|---|---:|
| JDK | 17 |
| Gradle wrapper | 8.13 |
| Android Gradle Plugin | 8.13.2 |
| Kotlin | 2.2.21 |
| KSP | 2.2.21-2.0.4 |
| Android compile SDK | 36 |
| Android target SDK | 36 |
| Android minimum SDK | 26 |
| Compose BOM | 2025.10.00 |
| Room | 2.8.3 |

Gradle, Android Gradle Plugin, Kotlin, KSP, and library versions are pinned in
the wrapper properties and gradle/libs.versions.toml. Update the catalog rather
than adding dependency versions directly to module build files.

## Prerequisites

- JDK 17 installed and selected as JAVA_HOME.
- Android SDK with Android SDK Platform 36 and platform tools.
- Android SDK command-line tools.
- An Android emulator or physical device for launch verification.
- An API 36 emulator for Room instrumentation tests.

Set ANDROID_HOME (or ANDROID_SDK_ROOT) to the SDK directory. Android Studio
can install the required SDK platform and configure the SDK path. Keep the
machine-specific local.properties file untracked.

## Build and verification

From the repository root:

~~~sh
./gradlew --version
./gradlew verifySharedCoreNoAndroidImports
./gradlew :shared:domain:allTests :shared:contracts:allTests :shared:engine:allTests
./gradlew :apps:android:testDebugUnitTest :apps:android:lintDebug assembleDebug
./gradlew :data:local:connectedDebugAndroidTest
~~~

The complete CI-equivalent commands are also listed in [CI.md](CI.md).

To install on a connected device or running emulator:

~~~sh
./gradlew :apps:android:installDebug
~~~

The first build downloads the pinned Gradle distribution and Android/Compose/
Room dependencies. An internet connection is required for initial setup.

## Release 1 application scope

The Android app provides manual match creation, match history, event capture,
timeline review, correction/deletion, local persistence, and explicit JSON
export. It does not connect to or observe the running game.

See [Policy and Compliance](POLICY_AND_COMPLIANCE.md) for blocked acquisition
methods and authorization requirements.
