# Release 1 — Functional Match Review Foundation

**Status:** In progress
**Execution authority:** [root PLAN.md](../../PLAN.md)
**Product direction:** [Product Plan](../PRODUCT_PLAN.md)

## Delivery checklist

### 1. Repository alignment
- [x] Create a dedicated release branch from the reviewed main commit.
- [x] Preserve the previous H1 execution plan under docs/archive.
- [ ] Align README and technical/product documentation.
- [ ] Keep every pre-existing file accounted for; do not remove unrelated files.

### 2. Domain and contracts
- [ ] Match metadata: hero, role, result, mode, notes, and timing.
- [ ] Explicit session lifecycle operations.
- [ ] Event repository port and deterministic ordering.
- [ ] Auditable event correction history (before/after snapshots, reason, timestamp).
- [ ] Stable versioned export contract.
- [ ] Validation and unknown-data behavior covered by tests.

### 3. Local persistence
- [ ] Room entities, DAOs, database, and repository adapters.
- [ ] Schema export and migration policy.
- [ ] Transactional session/event writes and cascading deletion.
- [ ] Active/paused session recovery after process death.
- [ ] DAO, mapping, repository, and migration tests.

### 4. Android workflow
- [ ] Recent match list.
- [ ] New/edit match form.
- [ ] Event capture form for death, objective/contest, and tactical note.
- [ ] Match detail and chronological timeline.
- [ ] Event correction/deletion with confirmation and correction reason.
- [ ] Factual match summary; no tactical verdicts.
- [ ] Offline operation and accessibility checks.

### 5. Export and verification
- [ ] User-initiated versioned export.
- [ ] Export includes provenance and correction history.
- [ ] Unit and instrumentation tests.
- [ ] Android build, lint, and CI pass.
- [ ] Release notes and known limitations documented.

## Scope guard

Release 1 does not implement tactical analysis, cooldown intelligence, cross-match intelligence, coaching, live capture, OCR, overlays, hidden-state access, or automation. Those capabilities remain governed by the later release gates in the Product Plan.
