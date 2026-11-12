# MIP — Fixed Product Plan

**Version:** 1.0.0
**Status:** Approved product direction
**Platform:** Android-first
**Product:** Mobile Legends Intelligence Platform

## 1. Product vision

MIP helps Mobile Legends: Bang Bang players understand their match decisions
through structured review, evidence-grounded tactical intelligence, and
measurable personal improvement.

MIP is a player-controlled companion. It is not a gameplay automation tool,
a source of hidden information, or a substitute for player judgment.

## 2. Product principles

1. **Evidence before advice:** every conclusion must be traceable to its inputs.
2. **Player-controlled capture:** players choose what to record and when.
3. **Transparent uncertainty:** missing, stale, contradictory, or insufficient
   evidence must remain explicit.
4. **Local-first:** core review works offline and does not require an account.
5. **Explainable intelligence:** calculations and rules are deterministic,
   versioned, and testable where practical.
6. **No hidden advantage:** never expose information unavailable through
   permitted play.
7. **Measurable improvement:** distinguish recorded activity, stated intention,
   observed behavior change, and retention.

## 3. Evidence classifications

| Classification | Meaning |
|---|---|
| OBSERVED | A fact directly observed and recorded with a traceable source |
| PLAYER-REPORTED | Information entered or asserted by the player |
| IMPORTED | Data supplied through a separately reviewed and authorized import |
| SYNTHETIC | Fictional data used for tests or demonstrations |
| CALCULATED | Deterministic output computed from recorded inputs and a documented rule |
| INFERRED | Interpretation that depends on context and is not directly observed |
| RECOMMENDED | A proposed action based on evidence and an explicitly identified rule |

A derived conclusion must link to its supporting inputs and rule/version.
Correlation is not causation. Estimates and inferences must never be presented
as observed facts. When evidence is insufficient, report that limitation.

## 4. Release roadmap

### Release 1 — Functional Match Review Foundation

Establish reliable manual match capture and review:
- Structured match metadata: hero, role, result, mode, timing, and notes.
- Structured match events with match-relative timestamps and provenance.
- Deterministic chronological timeline.
- Match/event editing and deletion; corrections retain an audit trail.
- Local persistence, offline operation, and recovery after app restart.
- Basic factual match summary and explicit versioned export.

Release 1 is a foundation. It does not include tactical verdicts or coaching.

### Release 2 — Tactical Intelligence

Introduce narrowly scoped, evidence-based tactical analysis:
- Analyze only supported situations with defined input requirements.
- Use deterministic, versioned rules and mechanics data where appropriate.
- Explain supporting evidence, uncertainty, and limitations.
- Avoid unsupported causal claims and broad player ratings.

### Release 3 — Cross-Match Intelligence

Build player-specific understanding across multiple recorded matches:
- Compare comparable situations and events.
- Identify recurring strengths and mistakes only when evidence is sufficient.
- Show sample size, confidence, and limitations.
- Track changes without presenting correlation as proof of causation.

### Release 4 — Personal Improvement System

Turn validated insights into a measured improvement workflow:
- Prioritize a small number of evidence-backed improvement areas.
- Define practice objectives and measurable behaviors.
- Reassess later matches against a clear baseline.
- Distinguish intention, practice completion, behavior change, and sustained progress.

## 5. Technical direction

- Android-first application using Kotlin and Jetpack Compose.
- Kotlin Multiplatform shared domain/contracts/engine where it provides clear
  reuse and testability.
- Room for local Android persistence.
- Offline-first operation; no backend or account required for the initial release.
- Deterministic domain behavior and automated tests.
- Versioned data contracts and explicit schema migrations.
- Keep UI, domain rules, and persistence responsibilities separated.

## 6. Privacy and compliance

Initial data acquisition is player-controlled manual entry and clearly labeled
synthetic test data. Imported sources require separate written authorization,
rights review, provenance, and maintainer approval.

Do not implement automated live capture/OCR, overlays, process-memory inspection,
packet interception, game modification, input automation, hidden-state inference,
or automated enemy tracking without exact written authorization and a separate
policy review covering method, purpose, data, platform, and distribution.

Minimize personal data. Explain local storage, export, and deletion. Accounts,
cloud sync, telemetry, and sharing are not Release 1 requirements.

## 7. Governance

- Root [PLAN.md](../PLAN.md) is the current execution authority.
- This document describes the product roadmap; it does not authorize future
  release implementation.
- Changes remain small, reviewable, and traceable to approved scope.
- Do not merge a pull request without explicit maintainer approval.
- A completed feature is not evidence of tactical effectiveness or player
  improvement; those claims require separate evaluation.

## 8. Success criteria by release

- **Release 1:** records are reliable, editable, recoverable, reviewable, and exportable.
- **Release 2:** supported tactical rules are accurate and contextually useful.
- **Release 3:** recurring patterns are supported by adequate player-specific data.
- **Release 4:** recommendations lead to measurable behavior change and sustained
  improvement, not merely stated intent.
