# Architecture decisions

This file records the major architecture decisions that shape Kneatr's MVP.

## ADR-001: Start with a single application module

Status: Accepted

Decision:

- Keep the project in a single `app` module while the product scope and screen set are still
  changing.

Why:

- The current team and scope do not justify the overhead of early modularization.
- The app is still clarifying its MVP boundaries.

Consequence:

- Feature boundaries must still be kept clean at the package and layer level.
- Modularization can be introduced later if build time, ownership, or feature isolation becomes a
  problem.

## ADR-002: Use a local-first architecture

Status: Accepted

Decision:

- Make on-device persistence the default operating mode for the MVP.

Why:

- Privacy matters for relationship data.
- The app should work without backend availability.
- MVP scope should stay lightweight and low-maintenance.

Consequence:

- Room is the primary source of truth for app data after sync.
- Cloud sync is deferred to a later phase.

## ADR-003: Import contacts from the device instead of maintaining a separate manual address book

Status: Accepted

Decision:

- Use Android Contacts Provider as the source for identity fields such as name, phone, and email.

Why:

- Reduces data entry friction.
- Lets users start from real contacts rather than building a new list from scratch.

Consequence:

- The app depends on contacts permission for its main loop.
- Sync logic must handle adds, updates, and removals safely.

## ADR-004: Derive due state from interaction history

Status: Accepted

Decision:

- Compute due and overdue state from the latest communication log and cadence rules instead of
  storing a separate mutable schedule field as primary state.

Why:

- Reduces state drift.
- Keeps the scheduling model explainable.

Consequence:

- Communication log integrity becomes central to user trust.
- Date-derivation logic needs dedicated tests.

## ADR-005: Use adaptive Compose navigation patterns from the start

Status: Accepted

Decision:

- Use Material 3 adaptive navigation and list/detail patterns rather than designing only for phone
  layouts.

Why:

- The app concept fits both compact and expanded productivity patterns well.
- It avoids a later redesign for tablets and foldables.

Consequence:

- Screens must be designed with pane-aware layouts in mind.
- Navigation complexity is slightly higher early on.

## ADR-006: Use WorkManager for background sync

Status: Accepted

Decision:

- Schedule sync through WorkManager instead of keeping it tied only to foreground app sessions.

Why:

- It is the correct Android-native mechanism for deferred and periodic work.
- It keeps sync resilient across process death and app restarts.

Consequence:

- Work constraints must be chosen carefully.
- The difference between sync work and user reminder notifications should remain explicit.

## ADR-007: Use Hilt, ViewModel, Flow, and StateFlow for app state

Status: Accepted

Decision:

- Standardize on Hilt for DI and Flow/StateFlow-driven ViewModel state for screen rendering.

Why:

- It fits Compose well.
- It keeps reactive state centralized and testable.

Consequence:

- Repository APIs should stay flow-friendly.
- ViewModels should remain thin coordinators, not become data warehouses.
