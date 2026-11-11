# Architecture

## High-level flow
Compose UI -> presentation state/ViewModels -> shared use cases/domain
interfaces <- Android data adapters (Room + local versioned JSON).
The deterministic shared engine is called through domain/application boundaries.

## Dependency direction
Presentation depends inward on domain contracts. Data adapters implement domain
repository interfaces. Domain and engine never depend on Android, Compose, Room,
network, or UI classes.

## Acquisition boundary
MVP permits manual user-entered events and synthetic fixtures. A future import
adapter is possible only after source rights, terms, and publisher policy are reviewed.

Not enabled: automated live screen capture/OCR, overlays, hidden enemy tracking,
process-memory inspection, packet inspection, game modification, or input automation.

## Trust model
Classify information as USER_ENTERED, OBSERVED, IMPORTED, DERIVED, or PREDICTED.
Imported data must identify its approved source. Derived estimates link to inputs
and calculation version. Predictions must never be presented as facts.

## Future extension
Desktop may be a JVM client after Android foundations stabilize. Cloud sync is
deferred. Every new acquisition source requires data-source and policy review
plus explicit opt-in design.