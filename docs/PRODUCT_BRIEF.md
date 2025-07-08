# 🌟 PRODUCT_BRIEF.md

## Project Name

**Kneatr** – Relationships, brilliantly organized

## What It Is

A relationship maintenance app designed to help users—especially neurodivergent professionals—keep
in touch with the people they care about. Through tiered contact structures and smart reminders,
users can better manage their communication.

## Who It's For

- Neurodivergent professionals (e.g., ADHD)
- Busy but intentional people
- Users with a small-to-mid-sized contact circle they want to nurture

## Development Plan

The app will be developed across **five major phases**:

---

### Phase 1 – MVP Foundation

**Goal:** Core loop working, local-first, fast to iterate.

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

---

### Phase 2 – Power User Tools

**Goal:** Give obsessive organizers the power they want.

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

---

### Phase 3 – Smart UX & Feedback

**Goal:** Add richer insight, still fully local.

- Contact Strength Score (recency/frequency-based)
- Weekly summaries of activity
- Passive overdue queue behavior
- Calendar event preview (local device calendar only)
- Smart “contact rotation” logic

---

### Phase 4 – Monetization Infrastructure

**Goal:** Lay the foundation for revenue, make app sustainable.

- Add “Kneatr Pro” tier:
    - Unlock advanced tags
    - Unlock custom frequencies (if gated)
    - Unlock analytics
    - Tip Jar / Donate screen
    - Feature flags for Pro content
    - Respectful upsell UX (never naggy, never spammy)
- Export data to CSV/JSON
- App lock (PIN/Biometric) for privacy

---

### Phase 5 – AI, Sync, and Extension

**Goal:** Only if app has traction and revenue. Build smart.

- GPT-powered message suggestions
- AI-based tier reassignment
- Contact Strength smart analysis
- Firebase / Google Drive sync and multi-device backup
- Gmail integration (for reachouts)
- Call/SMS history ingestion (with permission)
- Auto-tiering via behavioral pattern detection

---

## Design Requirements

- **Framework:** Material3 components only
- **Icons:** Google Material Icons
- **Fonts:** Google Fonts (to be finalized)
- **Colors:** Electric blue primary with minimalist white/gray backgrounds
- **Design Tool:** Figma
- **Theme Tokens:**
  Use [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/) to
  generate theme tokens

## Assets Needed from Designer

- Logo (brand-aligned)
- App launcher icon (collaboratively defined)
- Banners (onboarding, empty states)
- UI loop wireframes (daily check-in, contact selection, post-action confirmation)
- Standardized components page for UI consistency
- Color theme sheet (primary, secondary, tertiary, error, neutral, neutral variant)

## Monetization Targeting Strategy

### Ideal Users

1. **Power Users** – obsessive Personal CRM types
    - Pay for customization, analytics, backup
    - Highly engaged, expect feature depth

2. **Neurodivergent Professionals** – minimalist, routine-focused
    - Will pay for things that reduce friction (e.g. backup, reminders)

3. **Casual Users** – try and drop off
    - Free tier only

### Features & Monetization Plan

| Feature                   | Monetize? | Tier / Notes                          |
|---------------------------|-----------|---------------------------------------|
| Cloud Sync / Backup       | ✅         | Core Pro plan anchor                  |
| Advanced Analytics        | ✅         | Optional Pro add-on                   |
| Custom Tags / Tiers       | ⚠️        | Gated after 3 free                    |
| AI Features               | ✅         | GPT = high cost, charge appropriately |
| Export Data               | ✅         | CSV/JSON exports for Pro              |
| App Lock (PIN/Biometrics) | ✅         | Privacy-conscious users               |
| Tip Jar                   | ✅         | One-time contribution, no unlock      |
| Ads                       | ❌         | Avoid completely to maintain trust    |
