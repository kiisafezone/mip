# Match export format

**Format:** MIP Match Export JSON  
**Schema version:** 2  
**Direction:** Explicit, user-initiated export of one locally stored match.

## Purpose and boundaries

The export is a portable copy of one MIP-created match record, its manually
entered events, and event-correction history. It contains no unrelated device
data and does not import or read data from the running MLBB game.

Export is initiated from the selected match's review screen through Android's
document picker. The user chooses the destination and filename. MIP does not
upload the file or send it to a server.

## Top-level object

| Field | Type | Meaning |
|---|---|---|
| `schemaVersion` | Integer | Export envelope version; currently `1` |
| `exportedAtEpochMs` | Integer | UTC export time in epoch milliseconds |
| `session` | Object | One `MatchSession` record |
| `events` | Array | Match events in deterministic timeline order |
| `revisions` | Array | Event correction records for this match |
| `sessionRevisions` | Array | Immutable match-metadata revision records |

## Session object

The serialized session includes its stable ID, record creation time, optional
end time/status, optional game version/patch, optional notes, schema version,
hero, role, result, mode, optional patch, and optional match duration. Result values are `WIN`, `LOSS`, `DRAW`, or
`UNKNOWN`. Hero and role are required by the H1 capture UI.

## Event objects

Each event preserves its ID and match ID, event type, match-relative
`gameTimeMs`, wall-clock `recordedAtEpochMs`, source, confidence, validation
state, optional actor ID, JSON payload, and schema version.

H1 UI-created events use source `MANUAL`. Initial UI event types are:

- `HERO_DIED` — player-reported death; optional context may be included.
- `OBJECTIVE_OBSERVED` — manually recorded objective/contest marker; payload
  includes the player-entered `objectiveId`.
- `TACTICAL_NOTE` — player-authored note; payload includes `text`.

The event time is always match-relative. A missing or unknown time is not
fabricated; the player must enter a time to create an event. Event arrays are
ordered by `gameTimeMs`, then `recordedAtEpochMs`, then stable event ID.

## Correction revisions

Each revision contains a stable revision ID, event ID, match ID, complete
before/after event snapshots as JSON strings, a player-provided correction
reason, correction time, and revision schema version. Revisions allow a reviewer
to distinguish the original entry from the corrected event.

## Match metadata revisions

Each sessionRevisions item contains a stable revision ID, match ID, complete
before/after MatchSession snapshots as JSON strings, a player-provided reason,
change time, and revision schema version. The current session object represents
the latest saved metadata; the revision array preserves prior states in
chronological order. Revisions are append-only and are removed when the parent
match is deleted.

## Compatibility

Consumers must check `schemaVersion` before interpreting the envelope.
Unknown event type strings and unknown JSON payload keys should be preserved
where possible; they must not be reinterpreted as verified facts. The source,
confidence, validation state, and timestamps must be retained. Manual entries
remain player-reported and are not independently verified by MIP.

## Privacy

The export contains only the selected MIP match record, its events, and its
event-correction history. The user controls whether and where the file is
saved or shared. Review any third-party or personal content in notes before
sharing.
