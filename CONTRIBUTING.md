# Contributing to MIP

Thanks for helping improve MIP. MIP is an independent, Android-first,
local-first coaching and match-analysis project. Contributions must preserve
the product's documented safety, data-source, and intellectual-property
boundaries.

## Before opening an issue or pull request

1. Search existing issues and pull requests to avoid duplicates.
2. Describe one focused problem or change; split unrelated work.
3. Read [Policy and Compliance](docs/POLICY_AND_COMPLIANCE.md) and
   [Licensing scope](docs/LICENSING.md).
4. Do not include game assets, screenshots, recordings, replays, extracted
   datasets, or other third-party material unless the exact rights and
   permissions for the proposed use have been reviewed and documented.
5. Do not propose automated live-game capture/OCR, overlays, hidden-state
   tracking, process-memory or packet inspection, game modification, input
   automation, or bypasses. These remain blocked unless the project policy's
   written-authorization release gate is satisfied.

## Development setup

- JDK 17
- Android SDK Platform 36
- Gradle Wrapper (use `./gradlew`; do not install or commit a separate Gradle)
- Android Studio is recommended for Android-specific development.

Follow [Android setup](docs/ANDROID_SETUP.md). Do not commit
`local.properties`, signing keys, tokens, credentials, generated build output,
or private user/game data.

## Verification

Run the checks relevant to your change. For the full CI-equivalent check:

```sh
./gradlew --no-daemon verifySharedCoreNoAndroidImports \
  :shared:domain:allTests :shared:contracts:allTests :shared:engine:allTests \
  :apps:android:testDebugUnitTest :apps:android:lintDebug assembleDebug
```

See [CI documentation](docs/CI.md). If a check cannot be run, say exactly
which check was not run and why. Never report an unexecuted check as passing.

## Branch and commit conventions

Use short, descriptive branches with a type prefix:

- `feat/<topic>` — product feature
- `fix/<topic>` — bug fix
- `docs/<topic>` — documentation
- `ci/<topic>` — build or automation
- `test/<topic>` — tests and fixtures
- `refactor/<topic>` — internal restructuring
- `governance/<topic>` — project policy and contributor workflow

Use Conventional Commit-style messages:

```text
type(scope): imperative summary
```

Examples: `feat(timeline): add deterministic event ordering`,
`fix(cooldown): reject mismatched game versions`,
`docs(policy): clarify source review requirements`.

## Pull request expectations

- Keep each PR scoped to one issue and explain the user-visible or maintenance
  impact.
- Complete the PR template, including tests, data-source impact, and compliance.
- Update relevant documentation and tests.
- Preserve provenance and fail-closed behavior for missing or unverified data.
- Do not add dependencies or permissions without explaining why.
- Do not merge your own change unless the repository maintainer explicitly
  authorizes the merge. A request to implement work is not merge approval.

## Data, privacy, and intellectual property

Manual user-entered records and clearly labeled synthetic fixtures are the
default. Every new data source must be reviewed under the policy before
implementation. Public availability does not establish permission to scrape,
copy, redistribute, or train on content. MIP's code license does not grant
rights to MLBB/Moonton names, trademarks, game assets, footage, or third-party
datasets.

## Questions

Use GitHub Discussions or an issue for non-sensitive project questions. For
security concerns, follow [SECURITY.md](SECURITY.md); do not publish
vulnerability details in a public issue.
