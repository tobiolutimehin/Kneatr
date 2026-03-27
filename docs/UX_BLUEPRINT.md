# UX blueprint

This document defines the information architecture, user flows, screen set, and interaction rules
for Kneatr's MVP.

## UX goal

The app should help a user answer one question quickly: **Who should I reach out to, and what is
the easiest next step?**

## Information architecture

### Top-level destinations

- Home
- Contacts

### Secondary destinations

- Contact Detail

### Supporting surfaces

- Permission required screen
- Tier picker bottom sheet
- Tag editor bottom sheet
- Add/edit communication log bottom sheet
- Delete / confirm dialogs where needed
- Error, loading, and empty states

## App map

```text
App launch
|
+-- Permission gate
|   +-- Grant permission -> Initial sync -> Home
|   +-- Deny temporarily -> Permission required screen
|   +-- Deny permanently -> App settings path
|
+-- Home
|   +-- Overdue section -> Contact Detail
|   +-- Due Today section -> Contact Detail
|   +-- Upcoming section -> Contact Detail
|   +-- Random section -> Contact Detail
|
+-- Contacts
    +-- Search / browse -> Contact Detail
    +-- Manual sync action

Contact Detail
+-- Edit tier
+-- Edit tags
+-- Quick reach-out actions
+-- Add communication log
+-- Edit or delete communication log
```

## Core flows

### 1. First launch and permission flow

1. User opens Kneatr.
2. The app requests contacts permission.
3. If granted, the app triggers contact sync and lands the user in the main app shell.
4. If denied, the app explains why contact access is needed.
5. If the system dialog can no longer be shown, the app routes the user toward system settings.

Design note: the first-run experience should feel like a clear invitation, not a wall of legal
copy.

### 2. Daily review flow

1. User opens Home.
2. The app shows four priority buckets:
    - Overdue
    - Random
    - Upcoming
    - Contacts Due Today
3. User either:
    - taps a card to inspect a contact, or
    - marks the contact as complete from the card
4. The app updates the schedule after logging the interaction.

### 3. Browse and search flow

1. User opens Contacts.
2. User scrolls alphabetically or searches by name, phone, or email.
3. User selects a contact.
4. Contact detail opens.

### 4. Contact maintenance flow

1. User opens Contact Detail.
2. User reviews name, tier, tags, last contact, next contact, and history.
3. User optionally changes tier or tags.
4. User logs a communication with date, type, and optional notes.
5. The UI updates the contact's due state automatically.

### 5. Manual refresh flow

1. User opens Contacts.
2. User triggers the refresh action.
3. WorkManager performs a one-time contact sync.
4. The list updates once local data changes.

## Screen inventory

| Screen                  | Purpose                                 | Must-have content                                       | Required states                       |
|-------------------------|-----------------------------------------|---------------------------------------------------------|---------------------------------------|
| Permission Required     | Explain blocked access and recover flow | headline, explanation, primary CTA, secondary CTA       | initial denied, permanently denied    |
| Home                    | Daily decision surface                  | section headers, contact cards, mark complete action    | loading, empty sections, error        |
| Contacts                | Full browse and search                  | search field, grouped list, refresh action              | loading, empty, error, search results |
| Contact Detail          | Full contact management                 | name, tier, tags, due info, history, quick actions, FAB | loading, error, no logs               |
| Tier Selector           | Edit cadence tier                       | list of available tiers and current selection           | empty should never happen             |
| Tag Editor              | Edit classification tags                | selected tags, available tags, save action              | empty, populated                      |
| Communication Log Sheet | Create or edit interaction              | date, type, notes, save action                          | create, edit, validation              |

## Interaction rules

### Navigation

- Phones should use compact top-level navigation behavior.
- Larger screens should use a navigation rail or navigation suite pattern.
- Contact Detail should behave as the detail pane on large layouts where possible.

### Home behavior

- Section headers should be easy to scan.
- Empty sections should remain visible, but visually quieter than populated sections.
- "Mark complete" should be the fastest action on the card.
- Random contacts may be refreshed, but that action should never feel destructive.

### Contact list behavior

- Search should filter in place and feel instant.
- Alphabetical grouping should remain visible during browse mode.
- Refresh should be accessible but secondary to search and open-contact actions.

### Contact detail behavior

- Tier, tags, and logs should be editable without navigating away.
- Editing interactions should happen in bottom sheets or dialogs, not full-screen detours.
- Destructive actions should require confirmation or offer clear recovery feedback.

## State design requirements

Every primary screen must support:

- Loading
- Empty
- Error
- Permission denied, where relevant

Additional state requirements:

- Home must support empty per-section messaging
- Contacts must support "no contacts yet" and "no search results"
- Contact Detail must support "no communication history yet"
- Sheets must support both create and edit modes cleanly

## Responsive behavior

### Phones

- Single primary pane
- Fast switching between top-level destinations
- Detail usually occupies the full content area

### Tablets and foldables

- Top-level navigation should move to a larger-screen pattern
- Contacts should support list/detail presentation
- Detail panes should feel information-rich but not crowded

## UX gaps to resolve before public MVP

- A proper onboarding sequence beyond raw permission handling
- Clear user-facing explanation of what tiers mean
- Final decision on whether notifications are mandatory for launch
- A settings surface for notification and behavior preferences
