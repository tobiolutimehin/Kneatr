# MVP Scope - Kneatr

This document outlines the phased rollout of features for Kneatr, starting with a focused MVP and
expanding toward more intelligent, flexible, and integrated relationship management functionality.

---

## Phase 1 - MVP Scope

Core functionality required to make the app usable:
- Contact management with tier assignment
- Contact detail page (name, tier, next/last contact date)
- Mark communications as complete (for today)
- Basic notification system (WorkManager + Notification)
- Communication timeline tracking (next and last contact date updates)
- Overdue contact list (contacts with nextContactDate < today)
- Canonical, Responsive Layout:
    - Use Material3 Scaffold + WindowSizeClass to adapt to screen sizes.
    - Phones: Bottom Navigation for sectioning.
    - Tablets / Foldables: Navigation Rail + List-Detail Pane.
    - Support canonical layouts for Feed / Supporting Pane / Detail where space allows.
    - Follow Google’s canonical layout recommendations for productivity apps.

---

## Phase 2 - Enhancements

Nice-to-haves for after MVP is working:

- Custom communication frequencies per contact
- Tag system for organizing contacts (e.g., “family”, “work”)
- Daily task list view
- Communication history per contact
- Backdated communication logging (log a reach-out from a previous date)
- Randomized selection of due/overdue contacts (e.g., show 3-4 per day to reduce decision fatigue)
- Android App Shortcuts (Static/Dynamic)
- Basic Android Widgets (Home Screen)

---

## Phase 3 - Advanced Features

Stretch goals / high-effort features to improve intelligence, automation, and integration:

- Analytics and insights (e.g., missed reach-outs, longest streaks, etc.)
- Smart scheduling suggestions (e.g., based on calendar availability)
- Phone/message integration (e.g., log activity from call/SMS intent)
- Backup and sync (e.g., Firebase, Google Drive)
- Multi-device support / cloud-based persistence
