# Continuous integration

The Android CI workflow is `.github/workflows/android-ci.yml`. It runs for
pull requests targeting `main` or `develop`, pushes to those branches, and
manual dispatches.

## Pinned build toolchain

The workflow uses JDK 17 (Temurin), the Gradle Wrapper distribution pinned to
Gradle 8.13, Android Gradle Plugin 8.13.2, Kotlin 2.2.21, and Android SDK
Platform 36. Dependency/plugin versions belong in
`gradle/libs.versions.toml`; do not add unpinned versions to module build files.

## What CI checks

- Shared-core Android-import boundary.
- Common tests for shared domain, contracts, and engine modules.
- Local data/export unit tests and Android debug unit tests.
- Android Lint and debug APK assembly, including Room KSP compilation.
- Room persistence instrumentation tests on an API 36 Google APIs emulator.

A failed command fails the job and therefore produces a failed CI check. To make
that check a merge requirement, repository administrators must enable branch
protection/rulesets for `main` (and `develop`, if used) and require the
`Build and test (JDK 17)` status check. GitHub Actions alone cannot enforce
branch protection settings.

## Run the same checks locally

Install JDK 17, Android SDK Platform 36, and an API 36 emulator, then run from
the repository root:

~~~sh
./gradlew --no-daemon \
  verifySharedCoreNoAndroidImports \
  :shared:domain:allTests \
  :shared:contracts:allTests \
  :shared:engine:allTests \
  :data:local:testDebugUnitTest \
  :apps:android:testDebugUnitTest \
  :apps:android:lintDebug \
  assembleDebug \
  :data:local:connectedDebugAndroidTest
~~~

The workflow does not yet enforce a formatter. Android Lint is the selected
static-analysis step; a formatter will be added only after the project chooses
and documents one.
