# Product Requirements Document (PRD)

**Product:** MIP — Mobile Legends Intelligence Platform  
**Version:** 0.1.0-draft | **Status:** Sprint 0 baseline  
**Ownership:** Solo developer | **Distribution:** Open source | **Platform:** Android

## 1. Vision
Help MLBB players understand and improve tactical decisions through structured
match notes, transparent cooldown calculations, event timelines, and post-session
coaching insights. The first release is a player-controlled, local-first coaching
tool—not live-game automation or an information-advantage tool.

## 2. Target user
A player who wants to record important match events, review tactical decisions,
and build better habits without a server account or always-on network.

## 3. MVP outcomes
1. Create and manage match sessions.
2. Manually record supported events with timestamps.
3. Calculate estimated cooldown-ready times from versioned, configurable data.
4. Review a chronological event timeline.
5. Correct or delete manually entered events.
6. Generate a basic session summary and retain history locally.

## 4. Functional requirements
- FR-01: Start, pause, resume, and finish a session.
- FR-02: Store mode, patch/data version, start/end time, and notes.
- FR-03: Record manually entered events, including legitimately observed spell use,
  observed deaths/objectives, and free-form tactical notes.
- FR-04: Each event stores source, confidence, match-relative time, creation time,
  and validation status.
- FR-05: Cooldown engine computes ready time from trigger time plus configured duration.
- FR-06: Estimates expose their source data and are visibly labeled as estimates.
- FR-07: Users can correct event time and remove mistaken events.
- FR-08: Timeline ordering is deterministic.
- FR-09: Session data is available offline.
- FR-10: User can export a local report on demand.

## 5. Non-functional requirements
- No account required; local-first; minimal personal-data collection.
- Versioned, migratable persistence.
- Domain logic independent of Compose and Android framework.
- Deterministic, unit-tested calculations.
- UI distinguishes observed facts, user-entered data, and estimates.
- App must not disrupt or automate gameplay.

## 6. MVP non-goals
No live screen capture/OCR, floating overlay, memory reading, code injection,
packet interception, game modification, input automation, bot behavior, hidden
enemy tracking, matchmaking, social network, cloud account, or paid service.
No claim of official MLBB affiliation or endorsement.

## 7. Success metrics
- Saved events survive app restart in test scenarios.
- Cooldown arithmetic matches configured fixtures exactly.
- Equal-time timeline ordering is stable.
- Core session flows work without network.
- Every estimate is distinguished from an observed event.

## 8. Release gates
A: Sprint 0 scope and policy review. B: Android shell and persistence tests.
C: Event engine tests. D: MVP usability review. Any future live-data adapter
requires a separate written authorization review.