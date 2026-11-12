# MIP v1.1.1 repository adoption record

**Date:** 2026-11-13  
**Status:** Documentation synchronization proposal; pending PR review  
**Approved product authority:** `docs/PRODUCT_PLAN_V.1.1.1_LOCKED_SOT.md`  
**Historical baseline:** `docs/archive/PRODUCT_PLAN_V.1.1.0_LOCKED_SOT.md`

## Authority and status

The product owner approved v1.1.1 on 2026-11-13. The v1.1.1 locked plan is the current source of truth. The v1.1.0 file is preserved unchanged for historical traceability.

The approved current milestone status remains: **M0 status unverified; Sprint 0 complete; M1 not started.** This documentation synchronization does not mark M0 complete, authorize M1, authorize implementation, change architecture, advance a milestone, authorize a release, or approve a merge.

## Repository documentation alignment

The following existing documents remain in the repository but are explicitly marked subordinate/historical so their old authority claims do not conflict with v1.1.1:

- `PLAN.md` — historical H1 execution plan; not current milestone authority.
- `BUILD_PLAN.md` — historical H1/v1.0.0 checklist; not current roadmap authority.
- `docs/PRODUCT_PLAN.md` — v1.0.0 product vision; historical context only.
- `README.md` — prior project-decision summary; descriptive only and not implementation authorization.

Their substantive historical content is retained rather than silently rewritten or deleted.

## Editorial normalization in the repository copy of v1.1.1

The source artifact contains stale draft-era wording that contradicts its explicit product-owner approval record and locked status. The repository copy makes only these authority/metadata corrections:

1. Section 2 names v1.1.1, rather than v1.1.0, as the current governing source.
2. The conflict-resolution paragraph states that v1.1.1 is locked and approved on 2026-11-13, while v1.1.0 remains the unchanged historical baseline.
3. The first-release milestone boundary is described as unresolved, without calling the approved plan a draft.
4. Section 10 identifies v1.1.1 as the governing Scope Guard.
5. The final status line states that the locked plan is authoritative following approval.

No product scope, milestone, technical decision, priority, acceptance criterion, or unresolved product-owner decision is changed by these editorial corrections.

## Outstanding decisions remain open

Proposed decisions 11.35–11.40 remain unresolved/deferred exactly as recorded in the approved plan. In particular, this adoption record does not decide the first externally distributed Android milestone boundary, M3 replay-ingestion authorization/gate applicability, M2 capture-purpose approval, cooldown treatment, desktop persistence/shared-core details, or M4 evaluation dataset readiness.

## Repository work boundary

This PR is documentation-only. It does not modify application code, tests, dependencies, CI behavior, architecture, branches other than this documentation branch, or any existing PR disposition beyond the separately approved closure of PR #21.
