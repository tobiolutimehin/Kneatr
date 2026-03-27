# Architecture

This document explains how Kneatr is built today and what technical direction should remain stable
through the MVP.

## System overview

Kneatr is a local-first Android app. The device contacts provider is the external data source, Room
is the app's persistent working store, and UI state is derived reactively from repository flows.

At a high level:

1. Contacts are read from Android's contacts provider.
2. The repository syncs them into Room.
3. ViewModels combine Room-backed flows into screen state.
4. Users update tiers, tags, and communication logs locally.
5. Due state is derived from interaction history plus tier or custom cadence rules.

## Technical stack

| Area                 | Choice                                   | Why                                                           |
|----------------------|------------------------------------------|---------------------------------------------------------------|
| Language             | Kotlin                                   | Standard Android-first choice with good coroutine support     |
| UI                   | Jetpack Compose                          | Declarative UI and state-driven rendering                     |
| Design system        | Material 3                               | Native Android guidance and adaptive patterns                 |
| Navigation           | Navigation 3 + adaptive navigation suite | Supports compact and large-screen behavior                    |
| DI                   | Hilt                                     | Simple dependency wiring across app, ViewModels, and workers  |
| Persistence          | Room                                     | Structured local storage with relations and flows             |
| Preferences          | DataStore                                | Lightweight persistence for app-level settings and cached IDs |
| Background work      | WorkManager                              | Safe deferred and periodic background jobs                    |
| Time handling        | kotlinx-datetime                         | Consistent date calculations                                  |
| External device data | Android Contacts Provider                | Source for imported contacts                                  |

## Current module and package structure

The codebase is currently a single application module:

- `data/`
- `domain/`
- `ui/`
- `di/`

Within that single module, the architecture is still layered:

- `data` handles persistence, fetchers, workers, and repository implementation
- `domain` holds models, repository contracts, mappers, and utility logic
- `ui` holds Compose screens, components, navigation, and ViewModels
- `di` wires dependencies

## Key architectural choices

- Local-first by default
- Single-module until feature boundaries stabilize
- Repository pattern between UI/domain and data sources
- Reactive UI state using `Flow` and `StateFlow`
- Adaptive navigation from the start
- Background sync handled by WorkManager, not foreground-only code paths

## Data model

### Contact

The domain `Contact` model contains:

- id
- name
- phone number
- optional email
- tags
- communication logs
- tier
- optional custom frequency

Important derived fields:

- `isOverdue`
- `isDueToday`
- `lastCommunicationDateRelative`
- `nextCommunicationDateRelative`
- `reachedOutToday`

Those are computed from the latest communication log and the cadence rules rather than stored as
separate source-of-truth fields.

### Contact tier

Tiers define default cadence via `daysBetweenContact`. The database seeds default tiers on first
creation. The current implementation seeds five tiers.

### Communication log

Communication logs represent a dated interaction for a contact. They store:

- type
- date
- contact id
- optional notes

The latest log becomes the anchor for future due-date calculations.

### Tags

Tags classify contacts and are modeled through a many-to-many relationship between contacts and tag
entities.

## Persistence architecture

### Room

`KneatrDatabase` currently stores:

- contacts
- contact tiers
- contact tags
- contact-tag cross references
- communication logs

Room is the main persistent store used by the UI.

### DataStore

DataStore currently holds lightweight state:

- first-run flag
- cached IDs for the daily random contact selection
- timestamp for when that random selection was generated

## Sync architecture

### External source

The only external data source today is the Android contacts provider.

### Sync behavior

The repository sync flow:

1. Fetch contacts from the phone.
2. Read current contacts from Room.
3. Upsert changed or new contacts.
4. Delete contacts that no longer exist on the phone.

This keeps local app data aligned with device contacts while still enriching those contacts locally
with tiers, tags, and communication logs.

### Trigger points

- App startup schedules periodic sync work
- First-run permission grant triggers immediate sync
- Manual refresh from the contacts screen triggers one-time sync

### WorkManager setup

Current background jobs:

- periodic contact sync every 14 days
- one-time manual refresh work

Current implementation note:

- manual sync currently requires a network-connected constraint even though contact sync is based on
  local device data. That is likely an implementation cleanup item rather than a desired product
  rule.

## Scheduling and prioritization logic

The app's core scheduling behavior is derived, not manually maintained.

### Due-state calculation

For a contact with communication history:

1. Find the latest communication log date.
2. Use `customFrequencyDays` when present.
3. Otherwise, use the selected tier cadence.
4. Compare the derived next date with today.
5. Classify the contact as overdue, due today, or upcoming.

### Home random selection

The home screen also includes a daily random set of contacts. The repository:

- excludes urgent contacts already covered elsewhere
- avoids contacts reached recently
- prefers contacts that already have tiers or tags when there is enough data
- caches the result for the day in DataStore

## UI architecture

### App shell

The app shell uses `NavigationSuiteScaffold` with top-level destinations for:

- Home
- Contacts

`ContactDetail` acts as the detail destination and participates in larger-screen list/detail
layouts.

### State management

Each primary screen has a ViewModel that exposes `StateFlow` UI state:

- `HomeViewModel`
- `ContactsListViewModel`
- `ContactDetailViewModel`

These ViewModels combine repository flows and user input into screen states such as loading, empty,
error, and success.

### Screen patterns

- Home: sectioned dashboard
- Contacts: searchable alphabetical list
- Contact Detail: editable structured detail with bottom sheets for subflows

## Permissions and privacy

- The main app value depends on `READ_CONTACTS`
- Contact data is stored locally on device
- There is no backend or account system in the current MVP architecture
- The product should continue to minimize unnecessary data movement

## Testing and quality posture

The build already includes dependencies for:

- unit testing
- Room testing
- coroutine testing
- Robolectric
- Compose UI testing

The architecture should keep business rules testable without UI dependencies, especially for:

- due-date derivation
- random home contact selection
- sync behavior
- tier and tag updates

## Known gaps and next technical steps

- Reminder notifications are referenced in product planning but not implemented
- There is no dedicated settings or onboarding architecture yet
- Single-module structure is fine for MVP, but feature modularization may be useful later if the
  screen set or team size grows
- The repo should keep product docs and implementation aligned on tier count and MVP notification
  expectations
