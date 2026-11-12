# Policy and Compliance Boundary

**Status:** Active engineering guardrail; not legal advice.
**Authorization status:** No written Moonton authorization is recorded in this repository.
**Applies to:** All releases and contributors.

## Purpose and independence

MIP is an independent, open-source player-controlled match review project. It is
not affiliated with, endorsed by, or sponsored by Moonton or Mobile Legends:
Bang Bang (MLBB). A feature being technically possible on Android does not
make its data source or acquisition method permitted.

## Current implementation boundary

Release 1 uses manual player entry and synthetic test data only. Until explicit
written authorization exists for a specific method, purpose, distribution
model, and data category:

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
- Do not claim Moonton has approved, partnered with, or endorsed MIP without
  verifiable written evidence supporting that exact claim.

## Release 1 feature and data-source inventory

| Feature | Acquisition method | Classification | Boundary |
|---|---|---|---|
| Create/edit/delete match | User enters match details in MIP | PLAYER-REPORTED | No automatic game detection |
| Record events | User enters event type and match-relative time | PLAYER-REPORTED | No live screen reading or automated event detection |
| Event timeline | Ordered from manually entered events | CALCULATED from PLAYER-REPORTED | No hidden-state reconstruction |
| Local match history | Store MIP-created records on device | PLAYER-REPORTED | No background game data collection |
| Basic summary | Display stored match metadata and event counts | CALCULATED from PLAYER-REPORTED | No tactical verdicts in Release 1 |
| Export | Explicit user action exports MIP-created records | PLAYER-CONTROLLED | Review privacy and third-party content before sharing |
| Synthetic fixtures | Fictional test/demo data | SYNTHETIC | Must never be presented as real match evidence |
| Mechanics datasets / cooldown intelligence | Not used by Release 1 | Future-release input | Requires provenance, rights, and review before production use |
| Automated import, live capture, OCR, overlays, enemy tracking, memory/packet access, game modification, or input automation | None | BLOCKED | No implementation pending exact written authorization and policy review |

Derived data inherits the restrictions and provenance of its inputs. Release 1
does not import data from MLBB or connect to the running game.

## Source classifications

Use the product evidence categories where relevant:

- **OBSERVED:** directly observed information with a traceable source.
- **PLAYER-REPORTED:** information entered or asserted by the player.
- **IMPORTED:** data supplied through a separately reviewed and authorized import.
- **SYNTHETIC:** fictional data used for tests or demonstrations.
- **CALCULATED:** deterministic output computed from recorded inputs and a
  documented rule.
- **INFERRED:** interpretation that depends on context and is not directly observed.
- **RECOMMENDED:** a proposed action based on evidence and an explicitly identified rule.
- **BLOCKED / UNAVAILABLE:** a source or output that cannot be used because
  permission, provenance, freshness, or evidence is insufficient.

## Required source review

Before adding or changing a data source, record:

1. Source owner, publisher, origin, and exact data categories.
2. Acquisition method, including whether it is manual, synthetic, imported,
   scraped, captured, or otherwise automated.
3. Applicable terms of service, license, written permission, and date/version reviewed.
4. Rights to use, modify, store, display, redistribute, and sublicense the data.
5. Separate rights for screenshots, footage, game assets, names, logos, datasets,
   and AI/model training, where applicable.
6. Privacy implications, personal/player data, consent, retention, deletion,
   and export behavior.
7. Whether the method observes, automates, or extracts data from the running
   game, or could expose hidden/unavailable game state.
8. Exact features, platforms, users, and distribution model covered by permission.
9. Evidence location, reviewer, decision, review date, and any conditions.

If any required item is unknown, mark the source BLOCKED or unavailable until
resolved. Accessibility of a source is not proof of permission.

## Publisher authorization register

No written authorization is currently recorded.

| Evidence ID | Scope / question | Evidence location | Status |
|---|---|---|---|
| AUTH-000 | General publisher authorization for MIP | None recorded | NOT OBTAINED |

Do not change this status without linking actual written evidence and recording
its scope, date, issuer, and conditions. Authorization for one method, purpose,
data category, platform, or distribution model does not imply authorization for
another.

## Feature-review checklist

Complete this checklist for every new feature, data source, or material change:

- [ ] Feature purpose and user-visible behavior are documented.
- [ ] Every input field has a named source and evidence classification.
- [ ] Acquisition steps and required permissions are documented.
- [ ] Source ownership, terms, licenses, and written permissions are reviewed.
- [ ] Storage, retention, deletion, export, and privacy impacts are reviewed.
- [ ] No blocked method or hidden-state access is involved.
- [ ] Estimates and derived values retain source/version provenance and are labeled.
- [ ] Missing, stale, or unverified data fails closed as unavailable.
- [ ] Required publisher authorization is archived and covers the exact proposed use.
- [ ] Android/platform and app-store policy reviews are complete where applicable.
- [ ] A maintainer decision and review date are recorded before implementation.

A reviewer must stop a change if any required answer is unknown. This checklist
does not replace legal advice or third-party approval.

## Release gate for blocked functionality

A blocked feature cannot enter implementation, live-game testing, or release
until all of the following are true:

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
method, feature behavior, platform, or distribution model changes. Check current
Google Play Developer Program Policies before each Android release.
