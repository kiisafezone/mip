# Continuous integration

The Android CI workflow is `.github/workflows/android-ci.yml`. It runs for
pull requests targeting `main` or `develop`, pushes to those branches, and
manual dispatches.

## Pinned build toolchain

The workflow uses JDK 17 (Temurin), the Gradle Wrapper distribution pinned to
Gradle 8.13, Android Gradle Plugin 8.13.2, Kotlin 2.2.21, and Android SDK
Platform 36. Dependency/plugin versions belong in
`gradle/libs.versions.toml`; do not add unpinned versions to module build files.

The workflow grants only `contents: read`. It does not receive repository
secrets, upload artifacts, publish packages, or deploy. Third-party GitHub
Actions are referenced by maintained major-version tags; review action changes
when updating those references.

## What CI checks

- Shared-core Android-import boundary.
- Common tests for shared domain, contracts, and engine modules.
- Android debug unit tests.
- Android Lint.
- Android debug APK assembly.

A failed command fails the job and therefore produces a failed CI check. To make
that check a merge requirement, repository administrators must enable branch
protection/rulesets for `main` (and `develop`, if used) and require the
`Build and test (JDK 17)` status check. GitHub Actions alone cannot enforce
branch protection settings.

## Run the same checks locally

Install JDK 17 and Android SDK Platform 36, then run from the repository root:

```sh
./gradlew --version
./gradlew --no-daemon \
  verifySharedCoreNoAndroidImports \
  :shared:domain:allTests \
  :shared:contracts:allTests \
  :shared:engine:allTests \
  :apps:android:testDebugUnitTest \
  :apps:android:lintDebug \
  assembleDebug
```

The workflow does not yet enforce a formatter. Android Lint is the selected
static-analysis step; a formatter will be added only after the project chooses
and documents one.
