# 🌟 PRODUCT_BRIEF.md

## Project Name

**Kneatr** – Relationships, brilliantly organized

---

## 📖 What It Is

Kneatr is a **relationship maintenance app** designed to help users—especially **neurodivergent
professionals**—stay in touch with the people who matter to them.  
Through a structured, tier-based contact system and personalized, non-intrusive reminders, users can
intentionally nurture their social connections.

The app’s core philosophy:  
**“Remove friction from maintaining meaningful connections, without overwhelming the user.”**

---

## 👥 Who It's For

- Neurodivergent professionals (e.g., ADHD, Autism)
- Busy individuals with intentional social habits
- People who want to nurture a small-to-mid-sized circle of important relationships
- Users who prefer **order, routine, and clarity** over chaos and guesswork

---

## 💡 Core Features (MVP Scope)

These are the non-negotiables that make Kneatr usable, purposeful, and differentiating from day one.

- Pull contacts from Android Contacts Provider
- Tier-based contact assignment (1–4)
- Reminder scheduling (based on assigned tier)
- Contact detail screen (name, tier, tags, last/next contact date)
- Mark communication as complete (auto-updates nextContactDate)
- Timeline / history of communications (manual log: call, text, email, in-person, other)
- Overdue contact list
- Randomized daily “people to reach out to” feed (3-4 contacts)
- Notifications for overdue contacts & daily check-in reminder
- Local Room database (offline-first, no backend)
- Onboarding flow (permission handling, dummy data mode)
- Canonical responsive layouts:
    - **Phones:** Bottom Navigation + Single-pane navigation
    - **Tablets / Foldables:** Navigation Rail + List/Detail split
    - **Uses:** Material3 Scaffold, WindowSizeClass

---

## 🎯 Phase 2 Goals (Power User Enhancements)

These features enhance flexibility, personalization, and daily usability.

- Custom communication frequencies per contact
- Tagging system (family, work, friends, etc.)
- Skip this cycle / skip future cycles
- Birthday / anniversary reminders (from Contacts)
- Prioritized daily task list
- Manual notes per interaction
- Local analytics (most contacted, longest overdue)
- Archive functionality for unused contacts
- Android App Shortcuts (Static/Dynamic)
- Basic Android Widgets (e.g., “Today’s People to Reach Out To”)

---

## 🔮 Phase 3 Goals (Advanced Systems)

These features improve intelligence, integration, and long-term stickiness.

- Contact Strength Score (recency/frequency)
- Weekly summaries of engagement
- Calendar integration (event previews)
- Smart rotation for contact suggestions
- Call/SMS history ingestion (with permission)
- Email integration (e.g., Gmail API)
- Cloud sync (Firebase / Google Drive)
- Multi-device support
- Export data (CSV/JSON)

---

## 🎨 Design Requirements

### Visual Identity

- **Framework:** Material3 for Android (required)
- **Icons:** Google Material Icons
- **Fonts:** Google Fonts (TBD via design collaboration)
- **Colors:** Electric blue primary, minimalist white/gray backgrounds
- **Accessibility:** Fully responsive, accessible color contrasts, dark mode ready

### UX Patterns

- Minimalist, neurodivergent-friendly interface
- Large tap targets
- Clear UI states (loading, empty, success, error)
- Simple, predictable animations only
- Canonical Layout adherence (per Android guidelines):
    - Bottom Navigation on phones
    - Navigation Rail on tablets / foldables
    - List / Supporting Pane / Detail layout
    - Material3 Scaffold + WindowSizeClass responsiveness

---

## 🎨 Assets Needed from Designer

These deliverables should be prepared within **Figma**, using the **Material Design Kit** and *
*Material Theme Builder**.

| Asset             | Purpose                                                                                    |
|-------------------|--------------------------------------------------------------------------------------------|
| Logo              | Brand identity, aligned with tagline                                                       |
| App Launcher Icon | Co-designed with developer                                                                 |
| Banners           | Onboarding, empty states, marketing                                                        |
| UI Loop Journey   | Visual flow from onboarding to daily use                                                   |
| Component Library | Standardized components page (inputs, lists, buttons, states)                              |
| Color System      | Primary, secondary, tertiary, error, neutral, neutral-variant tokens (Material3 compliant) |

---

### 🔧 Tooling Clarification

- All design work must be provided in **Figma**.
- Designer is encouraged to
  use [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/) to
  generate system-compliant tokens and themes.
- **Material3 Design Kit** must be used for components and spacing.

---

## 💼 Development Plan: Iterations

### **Phase 1: MVP Foundation**

- Core relationship management loop
- Offline-first, Android-native UX
- Ready for internal testing and feedback

### **Phase 2: Power User Features**

- Customization, tagging, enhanced organization
- Surface power features for obsessive users

### **Phase 3: Smart UX & Sync**

- Analytics, suggestions, syncing
- More automation, deeper integrations
- Ready for larger audiences

---

## 💰 Monetization Strategy

| Feature                   | Monetize? | Plan                                 |
|---------------------------|-----------|--------------------------------------|
| Cloud Sync / Backup       | ✅ Yes     | Anchor feature for "Pro"             |
| Advanced Analytics        | Optional  | Paid in Phase 4                      |
| Custom Tiers / Tags       | Maybe     | Allow limited free, expand in Pro    |
| AI Features               | ✅ Yes     | Locked behind Pro (costs to run GPT) |
| Export Data               | Optional  | Free or Pro, TBD                     |
| App Lock (PIN/Biometrics) | Optional  | Bundle with Pro                      |
| Tip Jar / Donations       | ✅ Yes     | One-time support option              |
| Ads                       | ❌ No      | Never included                       |

Monetization focuses on **respectful upsells, not exploitation**. No ads.

---

## 🚀 Summary for Collaborators / Designers

Kneatr is designed for a clear purpose: **organizing, maintaining, and protecting meaningful human
connections** through structured, flexible reminders and thoughtful UX.

It is **not a social network.**  
It is **not a gamified leaderboard.**  
It is a **quiet, assistive tool** for people who care about intentional relationships.

The MVP solves this first.  
Phases 2+ make it sustainable, extensible, and smart.
