# Policy and Compliance Boundary

**Status:** Active engineering guardrail; not legal advice.  
**Authorization status:** No written Moonton authorization is currently recorded in this repository.  
**Last reviewed:** 2026-11-13 (milestone mapping updated; existing restrictions retained)

## Purpose and independence

MIP (Mobile Legends Intelligence Platform) is an independent, open-source coaching
and match-analysis project. It is not affiliated with, endorsed by, or sponsored
by Moonton or Mobile Legends: Bang Bang (MLBB).

This document defines the permitted data-acquisition boundary for the MVP and the
evidence required before that boundary can change. A feature being technically
possible on Android does not make its data source permitted.

## Current implementation boundary

Until explicit written authorization exists for a specific method, purpose,
distribution model, and data category:

- Do not automate collection of information from or interaction with MLBB.
- Do not implement live screen capture or OCR against the running game.
- Do not implement floating/live overlays, live enemy-position intelligence,
  hidden-state inference, or automated tactical prompts based on live game state.
- Do not inspect process memory, inject code, intercept/decode game packets,
  modify game files, automate input, or exploit bugs.
- Do not bypass technical restrictions, anti-cheat controls, access controls,
  or platform protections.
- Do not infer publisher permission from Android runtime permissions, public
  availability of data, community practice, app-store approval, or silence.
- Do not claim that Moonton has approved, partnered with, or endorsed MIP without
  verifiable written evidence supporting that exact claim.

## Milestone-specific interpretation under product plan v1.1.1

This policy remains an active engineering guardrail. The following mapping clarifies
how its existing restrictions relate to the M0–M5 roadmap; it does not weaken any
restriction or resolve a pending product-owner decision.

- **M2 — Android Companion:** v1.1.1 limits M2 updates to manual and event-driven
  methods, places automatic recognition in M4, and blocks M2 release pending its
  written-authorization and data-rights gates. Proposed decision 11.37 about the
  purpose of M2 screen capture remains pending. Do not treat the proposed
  capture-session-only interpretation as approved authorization or assume
  additional capture uses (including frame display or image analysis) are
  authorized. Automatic recognition remains M4 scope, not M2.
- **M3 — Desktop Analysis:** proposed decision 11.36 leaves the replay-ingestion
  boundary and applicability of the Section 9.1 release gates unresolved.
  Do not infer that the M2/M4 gates automatically apply to M3, and do not infer
  that M3 is exempt. Before using third-party recordings, complete the applicable
  source, terms, rights, privacy, and platform review. Automatic OCR/object
  detection/event recognition from replay video remains M4 scope, not M3.
- **M4 — Computer Vision:** automatic recognition begins in M4. M4 release remains
  blocked until the written-authorization and data-rights gates in v1.1.1 and
  this policy are satisfied for the exact method, purpose, data, platform, and
  distribution model.

The general restrictions above continue to apply unless exact written evidence
and an explicit, dated maintainer decision establish a permitted exception.
Decisions 11.35–11.40 remain open; this mapping records their current status,
not a resolution.

## MVP feature and data-source inventory

The inventory below classifies acquisition methods and policy boundaries; it is
not a current milestone scope list or implementation authorization. In
particular, the cooldown-estimate row does not authorize cooldown behavior:
pending decision 11.38 and the applicable milestone scope remain controlling.

The product is designed to remain usable without connecting to, observing, or
controlling the running game. The following table classifies acquisition and
policy boundaries for previously documented feature concepts; it does not
declare those features to be in current milestone scope or authorize their
implementation.

| MVP feature | Initial acquisition method | Source classification | Boundary |
|---|---|---|---|
| Create and manage a match session | User enters session details in MIP | Manual | No automatic game detection |
| Record match events | User manually enters event type and game-relative time | Manual | No live screen reading or automated event detection |
| Cooldown estimates | Deterministic calculation from a versioned mechanics dataset whose provenance and rights have been reviewed | Documented/versioned input; synthetic fixtures in tests | Missing, stale, or unverified values must return unavailable; never guess |
| Event timeline | Derived only from events manually entered by the user or permitted test fixtures | Derived from manual / synthetic | No hidden-state reconstruction |
| Local session history | Store user-entered MIP records locally | Manual | Local-first; no background game data collection |
| Basic reports | Deterministic summaries of MIP session records | Derived from manual | Clearly distinguish user-entered facts from estimates |
| Export | Explicit user action to export MIP-created records | User-controlled export | Review privacy and third-party content before sharing |
| Automated import of game data or recordings | None in MVP | Blocked pending authorization and rights review | No implementation until the release gate is met |
| Live capture, OCR, overlays, enemy tracking, memory/packet access, game modification, or input automation | None | Blocked | Prohibited from implementation pending explicit written authorization and review |

### Source classifications

Use one of these classifications for every data field and feature:

- **MANUAL:** Entered directly by the user into MIP. Manual entry must not
  quietly trigger automated observation or collection from the game.
- **SYNTHETIC:** Generated test/demo data that is fictional and clearly labeled.
  It must not be presented as real match evidence.
- **DOCUMENTED / VERSIONED:** A mechanics value or rule with recorded provenance,
  version, effective date, and rights/permission review. If provenance or
  permission is unclear, the value is unavailable for production use.
- **AUTHORIZED_IMPORT:** Data imported through a method and for a purpose
  explicitly covered by written authorization and a completed source review.
  No such source is currently approved or recorded.
- **BLOCKED:** Any source or acquisition method listed in the implementation
  boundary, or any source whose permission, terms, or rights remain unresolved.

A feature may combine sources, but every input must be classified independently.
Derived data inherits the restrictions and provenance of its inputs.

## Required source review

Before adding or changing a data source, record:

1. Source owner, publisher, origin, and exact data categories.
2. Acquisition method, including whether it is manual, synthetic, imported,
   scraped, captured, or otherwise automated.
3. Applicable terms of service, license, written permission, and the date/version
   reviewed.
4. Rights to use, modify, store, display, redistribute, and sublicense the data.
5. Separate rights for screenshots, footage, replays, game assets, names, logos,
   datasets, and AI/model training, where applicable.
6. Privacy implications, personal/player data, consent, retention, deletion,
   and export behavior.
7. Whether the method interacts with, observes, automates, or extracts data from
   the running game, and whether it could expose hidden or unavailable game state.
8. The exact MIP features, platforms, users, and distribution model covered by
   the permission.
9. Evidence location, reviewer, decision, review date, and any expiry or conditions.

If any required item is unknown, mark the source **BLOCKED** or unavailable until
resolved. Do not treat a source as approved merely because it is accessible.

## Publisher authorization questions and evidence register

No written authorization is currently recorded. Before proposing any live-game
integration, request written clarification through official Moonton channels.
Ask separately whether permission covers:

- Manual companion use while a match is running.
- Automated live screen capture and OCR.
- Floating overlays or contextual in-game assistance.
- Automated event or game-state collection.
- Enemy-position or other information not normally available to the player.
- Post-match processing of user-provided screenshots, recordings, or replays.
- Use and redistribution of game mechanics data, names, artwork, and other assets.
- Public open-source publication, commercial distribution, and third-party use.
- Storage, sharing, dataset creation, and AI/model training.

Authorization for one method, purpose, data category, platform, or distribution
model does not imply authorization for another.

Evidence register:

| Evidence ID | Scope / question | Evidence location | Status |
|---|---|---|---|
| AUTH-000 | General publisher authorization for MIP | None recorded | NOT OBTAINED |

Do not replace the status above without linking the actual written evidence and
recording its scope, date, issuer, and applicable conditions. Do not publish
private correspondence or personal contact details; retain a suitably redacted
audit record where necessary.

## Feature-review checklist

Complete this checklist for every new feature, data source, or material change:

- [ ] Feature purpose and user-visible behavior are documented.
- [ ] Every input field has a named source and one of the classifications above.
- [ ] Acquisition steps are documented, including permissions and automation.
- [ ] Source ownership, terms, licenses, and required written permissions are reviewed.
- [ ] Storage, retention, deletion, export, and privacy impacts are reviewed.
- [ ] No blocked method or hidden-state access is involved.
- [ ] Estimates and derived values retain source/version provenance and are labeled.
- [ ] Missing, stale, or unverified data fails closed as unavailable.
- [ ] Any required publisher authorization is written, archived, and explicitly
  covers the exact method, purpose, data, platform, and distribution.
- [ ] Android/platform and app-store policy reviews are complete where applicable.
- [ ] A maintainer decision and review date are recorded before implementation.

A reviewer must stop the change if any required answer is unknown. Passing this
checklist does not replace legal advice or third-party approval.

## Release gate for blocked functionality

A blocked feature cannot enter implementation, testing against the live game,
or release until all of the following are true:

1. Written authorization is archived and verified for the exact technical method,
   purpose, data category, platform, and distribution model.
2. The proposed implementation matches the authorization's scope and conditions.
3. Source terms, intellectual-property rights, privacy/security, and applicable
   platform/store policies have been reviewed.
4. Data provenance, retention/deletion, user disclosure, and failure behavior
   are documented.
5. The maintainer records an explicit, dated approval referencing the evidence.

If authorization is absent, ambiguous, withdrawn, or narrower than the proposed
use, the feature remains blocked. Store approval is not publisher authorization,
and publisher authorization is not store approval.

## Review cadence

Re-review this policy before each release and whenever a data source, acquisition
method, feature behavior, platform, or distribution model changes. Check the
current Google Play Developer Program Policies before each Android release.
