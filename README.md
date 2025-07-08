# Kneatr

**Tagline:** Relationships, brilliantly organized.  
**Package:** `com.hollowvyn.kneatr`

Kneatr is a minimalist Android app built to help users maintain meaningful relationships through
structured, personalized reminders. Built with neurodivergent users in mind, Kneatr helps you manage
your social life with clarity and intention—without feeling overwhelmed.

---

## 🌟 What It Does

- Organizes contacts by communication tiers (e.g., weekly, monthly)
- Surfaces 3–4 contacts daily for easy, low-friction reachouts
- Tracks when you last connected with someone
- Notifies you about overdue contacts
- Provides a simple, clear UI for managing your relationships

---

## 👥 Who It's For

- Neurodivergent professionals (ADHD, Autism, etc.)
- Busy people trying to be more intentional
- Anyone who struggles to keep in touch but wants to do better

---

## 🛠️ Built With

- **Kotlin** + **Jetpack Compose** for modern Android development
- **Room** for local offline data storage
- **Hilt** for dependency injection
- **WorkManager** for background task scheduling
- **Material3** design system

---

## 📦 Project Structure

- `app/` – Android app code
- `docs/` – Planning, product, and design documentation

---

## 🔄 Development Roadmap

### Phase 1 – MVP Foundation (Current)

- Pull contacts from Android Contacts Provider
- Manual tier assignment (1–4)
- Daily feed of 3–4 randomized overdue contacts
- View + filter contacts list
- Search contacts
- Contact detail screen with tier, tags, interaction log
- Add manual interaction
- Daily + overdue reminders
- Onboarding flow
- Local Room DB
- Material3 UI (dark mode + accessibility)

### Phase 2 – Power User Tools

- Custom communication frequencies
- Manual tagging
- Skip cycle options
- Birthday/anniversary reminders
- Daily task prioritization
- Interaction notes
- Local analytics
- Archive contacts

### Phase 3 – Smart UX & Feedback

- Contact Strength Score
- Weekly summaries
- Passive overdue logic
- Local calendar event sync
- Smart contact rotation logic

### Phase 4 – Monetization Infrastructure

- Kneatr Pro tier unlocks: advanced tags, analytics, export, app lock
- Tip jar support
- CSV/JSON export
- Feature gating via flags

### Phase 5 – AI, Sync, and Extensions

- GPT-powered message suggestions
- Smart tier reassignment
- Firebase / Google Drive sync
- Gmail + call/SMS integration

---

## 💰 Monetization Strategy

Offer a "Kneatr Pro" plan that unlocks:

- Sync and cloud backup
- Unlimited tags/frequencies
- Analytics dashboards
- Optional features (app lock, export)

---

## 🤝 Contributing

Kneatr is being built by a solo developer. While outside contributions are not currently open, feel
free to fork or explore the code. Contributions may be considered post-MVP.

---

## 📄 License

MIT License (to be finalized)
