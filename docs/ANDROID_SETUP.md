# Android development setup

## Pinned toolchain

| Component | Version |
|---|---:|
| JDK | 17 |
| Gradle wrapper | 8.13 |
| Android Gradle Plugin | 8.13.2 |
| Kotlin | 2.2.21 |
| Android compile SDK | 36 |
| Android target SDK | 36 |
| Android minimum SDK | 26 |
| Compose BOM | 2025.10.00 |

Gradle, Android Gradle Plugin, Kotlin, and library versions are pinned in the
wrapper properties and gradle/libs.versions.toml. Do not add dependency
versions directly to module build files; update the catalog instead.

## Prerequisites

- JDK 17 installed and selected as JAVA_HOME.
- Android SDK with Android SDK Platform 36.
- Android SDK command-line tools and platform tools.
- An Android emulator or a physical device for launch verification.

Set ANDROID_HOME (or ANDROID_SDK_ROOT) to the SDK directory. Android Studio
can install the required SDK platform and configure the SDK path. Keep the
machine-specific local.properties file untracked.

## Build and verification

From the repository root:

~~~sh
./gradlew --version
./gradlew verifySharedCoreNoAndroidImports
./gradlew assembleDebug
./gradlew :apps:android:lintDebug
./gradlew :apps:android:testDebugUnitTest
~~~

To install on a connected device or running emulator:

~~~sh
./gradlew :apps:android:installDebug
~~~

The first build downloads the pinned Gradle distribution and Android/Compose
dependencies. An internet connection is therefore required for initial setup.

## Scope and safety

This is only the Android application shell and architecture baseline. It contains
no game integration, overlay, screen capture, OCR, accessibility automation,
input automation, process-memory inspection, or packet inspection. The home
screen is a neutral entry point; match features will be implemented in their
separately scoped issues and must follow the policy boundary.
