# MVP_SCOPE.md

This document outlines the phased rollout of features for Kneatr, starting with a focused MVP and
expanding toward more intelligent, flexible, and integrated relationship management functionality.

---

## Phase 1 – MVP Foundation

Goal: Core loop working, local-first, fast to iterate.

- Pull contacts from Android Contacts Provider
- Manual tier assignment (1–4)
- Daily feed of 3–4 randomized overdue contacts
- Contact list with filter by tier or tag
- Contact search
- Contact detail screen with:
  - Info display (name, number, etc.)
  - Tier + tags
  - Interaction log (type + date)
  - Add manual interaction (call, text, email, in-person, other)
- Simple notification system (daily + overdue)
- Local Room DB (no backend)
- Onboarding: permission request + dummy data mode
- Material3 UI with dark mode & accessibility

## Phase 2 – Power User Tools

Goal: Give obsessive organizers the power they want.

- Custom communication frequencies
- Manual tagging (family, work, friends, etc.)
- "Skip this cycle" and "skip X future cycles"
- Birthday/anniversary reminders (from contact fields)
- Daily task list (prioritized suggestions)
- Manual interaction notes
- Local-only analytics:
  - Most contacted
  - Longest overdue
- Archive contact functionality

## Phase 3 – Smart UX & Feedback

Goal: Add richer insight, still fully local.

- Contact Strength Score (recency/frequency-based)
- Weekly summaries of activity
- Passive overdue queue behavior
- Calendar event preview (local device calendar only)
- Smart “contact rotation” logic

## Phase 4 – Monetization Infrastructure

Goal: Lay the foundation for revenue, make app sustainable.

- Add “Kneatr Pro” tier:
  - Unlock advanced tags
  - Unlock custom frequencies (if gated)
  - Unlock analytics
- Tip Jar / Donate screen
- Feature flags for Pro content
- Respectful upsell UX (never naggy, never spammy)
- Export data to CSV/JSON
- App lock (PIN/Biometric) for privacy

## Phase 5 – AI, Sync, and Extension

Goal: Only if app has traction and revenue. Build smart.

- GPT-powered message suggestions
- AI-based tier reassignment
- Contact Strength smart analysis
- Firebase / Google Drive sync and multi-device backup
- Gmail integration (for reachouts)
- Call/SMS history ingestion (with permission)
- Auto-tiering via behavioral pattern detection
