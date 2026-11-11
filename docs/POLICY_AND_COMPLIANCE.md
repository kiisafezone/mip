# Policy and Compliance Boundary

**Status:** Conservative engineering guardrail; not legal advice.

## Purpose
MIP is an independent open-source coaching and analysis project. It is not
affiliated with, endorsed by, or sponsored by Moonton or Mobile Legends: Bang Bang.

## Current implementation boundary
Until explicit written authorization exists for a specific method and use:
- Do not automate collection of information from or interaction with MLBB.
- Do not implement live screen capture/OCR against the running game.
- Do not implement floating overlays or live enemy-position intelligence.
- Do not inspect process memory, inject code, intercept packets, modify game files,
  automate input, or exploit bugs.
- Do not infer permission from Android runtime permissions.

## Allowed initial scope
Standalone coaching UI, manual note-taking, synthetic fixtures, generic
deterministic calculations from documented/versioned inputs, local storage,
and user-controlled export. Review rights and terms before processing any
footage, screenshots, replays, assets, extracted data, or player data.

## Required source review
Record source/ownership, terms/license/permission, redistribution rights,
training/dataset rights, retention/deletion rules, and whether use automates
collection from or interaction with the game.

## Publisher authorization
Seek written clarification through official Moonton channels before implementing
live companion behavior, overlays, automated OCR, or automated data collection.
Ask separately about manual companion use, automated capture/OCR, overlays,
post-match processing of user-provided recordings/replays, commercial distribution,
open-source publication, and game assets/data in datasets or AI training.
Authorization for one use does not imply authorization for another.

## Store review
Re-check current Google Play Developer Program Policies before each release.
Store approval is not publisher authorization, and vice versa.

## Release gate
A blocked feature cannot enter implementation until written authorization is
archived; the technical method matches its scope; privacy/security and store
policy reviews are complete; and the solo maintainer records explicit approval.