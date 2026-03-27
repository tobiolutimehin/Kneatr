# MVP scope

This document defines what must exist before Kneatr can be considered a coherent MVP. It is both a
scope definition and a reality check against the current repository.

## Scope statement

The MVP should let a user import contacts, decide who matters most, see who needs attention, take
action, and record the interaction without needing a backend.

## MVP outcome

If the MVP is working, a user can:

1. Open the app and grant contact access.
2. See their imported contacts and organize them.
3. Open a daily dashboard that surfaces who needs outreach.
4. Reach out using their preferred communication channel.
5. Log that interaction and trust the app to update future follow-up timing.

## Scope status snapshot

| Capability                               | Status      | Notes                                                |
|------------------------------------------|-------------|------------------------------------------------------|
| Contacts permission and initial sync     | Implemented | Permission request and sync trigger exist            |
| Local database and contact import        | Implemented | Room + Contacts Provider sync exist                  |
| Home dashboard sections                  | Implemented | Overdue, random, upcoming, and due today are present |
| Contacts list and search                 | Implemented | Search and alphabetical grouping exist               |
| Contact detail with tier, tags, and logs | Implemented | Detail screen and bottom sheets exist                |
| Responsive navigation patterns           | Implemented | Navigation suite and list/detail patterns exist      |
| Reminder notifications                   | Not started | Mentioned in planning docs, not present in code yet  |
| Explicit onboarding flow                 | Partial     | Permission gate exists, full onboarding does not     |
| Dummy data mode                          | Not started | Mentioned historically, not present in code          |
| Settings and user controls               | Not started | No dedicated settings surface yet                    |

## Must-have capabilities

### 1. Permission and first-run flow

Acceptance criteria:

- On first launch, the app requests `READ_CONTACTS`.
- If permission is granted, an initial contact sync starts automatically.
- If permission is denied, the user sees a clear explanation of why the app needs contact access.
- If permission is permanently denied, the user can reach app settings from the blocked state.

### 2. Contact ingestion and local persistence

Acceptance criteria:

- Device contacts are imported into Room.
- Sync supports add, update, and delete behavior when the device contact list changes.
- The local database remains the app's working source of truth after sync.
- The app works without a backend.

### 3. Contact organization

Acceptance criteria:

- Each contact can have a tier.
- Each contact can optionally have tags.
- Default tiers are seeded into the database.
- The user can update tier and tag assignments from the contact detail screen.

### 4. Home dashboard

Acceptance criteria:

- The home screen surfaces contacts in at least four buckets:
  - overdue
  - due today
  - upcoming
  - random / curated
- Each card can open the contact detail view.
- The user can mark a contact complete directly from the home surface.
- Empty section states are explicit and calm, not broken-looking.

### 5. Contacts list

Acceptance criteria:

- The user can browse all synced contacts.
- Contacts are grouped alphabetically.
- The user can search by name, phone, or email.
- The user can trigger a manual re-sync from the list screen.

### 6. Contact detail and logging

Acceptance criteria:

- The contact detail screen shows the contact name, tier, tags, last communication, next
  communication, and communication history.
- The user can add a communication log entry.
- The user can edit or delete a communication log entry.
- The user can change tier and tags without leaving the detail screen.
- Quick reach-out actions are available when supported contact fields exist.

### 7. Scheduling model

Acceptance criteria:

- Due state is derived from the latest communication log plus either:
  - a custom frequency, when set
  - the tier cadence, otherwise
- The system can distinguish overdue, due today, and upcoming contacts.
- Random home contacts are refreshed on a daily cadence and cached locally.

### 8. Background behavior

Acceptance criteria:

- Periodic background sync is scheduled with WorkManager.
- Manual sync can be triggered on demand.
- Sync failures do not crash the app and can retry safely.

### 9. Layout and quality

Acceptance criteria:

- Phone layout uses bottom navigation or equivalent compact navigation behavior.
- Tablet and foldable layout uses larger-screen navigation patterns and supports list/detail
  presentation.
- Loading, error, empty, and permission-denied states exist on all primary flows.
- Core flows are testable and stable enough for internal feedback.

## Planned MVP-completion items

These still belong to the MVP blueprint, even though the current repo does not fully implement them:

- Reminder notifications for due and overdue contacts
- A more explicit onboarding path that explains tiers and the daily loop
- User-facing settings for notification cadence and behavior
- Finalized copy and polished empty-state guidance

## Explicitly out of scope

The following are not required for MVP launch:

- Cloud sync and account systems
- Team or shared relationship management
- Call/SMS/email ingestion
- Widgets and app shortcuts
- Analytics dashboards for the user
- AI features
- Export/import tools

## Launch checklist

Before calling the app an MVP:

- All must-have capabilities above are complete
- Notifications either ship or are deliberately removed from MVP language everywhere else
- UX copy is consistent across permission, empty, and error states
- The product brief, UX blueprint, architecture, and design spec all agree on the shipped scope

## Post-MVP directions

### Phase 2

- Custom frequencies surfaced more clearly in UI
- Better tagging and filtering
- Birthdays, anniversaries, and local analytics
- Shortcuts and widgets

### Phase 3

- Sync and backup
- Deeper integrations
- Smart prioritization
- Export and multi-device workflows
