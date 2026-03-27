# Product brief

## Product summary

Kneatr is an Android app that helps people maintain meaningful relationships through structure,
visibility, and low-friction follow-through. It imports contacts from the device, lets users
organize them by importance, highlights who needs attention, and records outreach so the next check
in is always clear.

Tagline: **Relationships, brilliantly organized.**

## Problem

Many people care deeply about staying in touch, but struggle to do it consistently. Existing tools
usually fail in one of two ways:

- Generic contacts apps store information but do not create a repeatable maintenance loop.
- Social, CRM, or task tools feel noisy, transactional, or too heavy for personal relationships.

For neurodivergent users in particular, the real problem is not desire. It is remembering,
prioritizing, and acting without cognitive overload.

## Target users

### Primary audience

- Neurodivergent professionals, especially people with ADHD or executive function challenges
- Users who prefer routine, clarity, and predictable systems over social spontaneity
- People maintaining a deliberate circle of important relationships rather than managing a massive
  address book

### Secondary audience

- Busy professionals with family, mentor, colleague, and friendship circles
- People rebuilding relationship habits after burnout, relocation, or major life changes

## Jobs to be done

Users hire Kneatr to help them:

- Know who they should reach out to without mentally scanning their whole social graph
- Turn "I should check in on them" into a concrete next action
- Keep a lightweight record of communication history
- Build a sustainable relationship habit without guilt, noise, or gamification

## Product principles

### 1. Calm over noisy

The product should feel assistive, not nagging. Every screen should reduce decision fatigue.

### 2. Structure without shame

Kneatr supports follow-through through organization and visibility, not guilt or streak pressure.

### 3. Local-first and private by default

Personal relationship data should stay on device for the MVP unless the user explicitly opts into
future sync features.

### 4. One clear action at a time

Each screen should make the next step obvious: review, reach out, log, or organize.

### 5. Useful on bad days

The app must remain usable when the user has low energy, limited attention, or little time.

## Core product loop

1. Import device contacts.
2. Assign tiers and optional tags.
3. Surface overdue, due soon, and curated daily contacts.
4. Reach out using the user's normal tools.
5. Log the communication.
6. Recalculate when that contact should appear again.

## MVP vision

The MVP is an Android-only, offline-first relationship maintenance tool with:

- Contact import from Android Contacts Provider
- A simple prioritization system using default tiers
- A daily dashboard that helps the user decide who to contact
- A contact detail view that supports quick actions and communication logging
- Local persistence with no backend dependency
- Responsive Android layouts for phones and larger screens

## MVP feature set

The launch blueprint includes:

- Permission handling for contact access
- Contact sync from the device into a local Room database
- Tier assignment and basic tag support
- Home dashboard with overdue, due today, upcoming, and random contact sections
- Searchable contacts list
- Contact detail view with tier, tags, last contact, next contact, and communication history
- Quick reach-out affordances for phone, text, and email actions where data is available
- Manual communication logging
- Background contact sync
- Reminder and notification support as a planned MVP-completion item

## Non-goals

Kneatr is not intended to be:

- A social network
- A team CRM or shared workspace
- A messaging client
- A habit streak game
- An AI conversation generator
- A cloud-first product at launch

## Success metrics

The initial product should optimize for:

- Activation: user grants contacts permission and completes initial sync
- Organization: user assigns tiers to a meaningful subset of contacts
- Engagement: user logs communication from the app repeatedly over time
- Usefulness: users can identify who to contact in under a minute
- Retention signal: users return for weekly or daily check-ins

## Design and experience requirements

- Material 3 visual language
- Calm, neurodivergent-friendly interface
- Large tap targets and predictable interactions
- Clear loading, empty, success, and error states
- Responsive layout behavior for phone and tablet/foldable patterns
- Dark-mode-ready color and contrast choices

## Constraints

- Platform: Android only
- Package: `com.hollowvyn.kneatr`
- Local-first MVP, no backend required
- Contacts permission is required for the main value loop
- Current codebase is a single `app` module
- Minimum SDK is Android 11 (`minSdk = 30`)

## Current repo status summary

As of this doc update, the repository already includes:

- Adaptive app shell with Home, Contacts, and Contact Detail routes
- Room persistence, tags, tiers, and communication logs
- DataStore-backed daily random contact selection
- WorkManager-based contact sync

The biggest remaining gaps between the intended MVP and the implemented app are:

- Reminder notifications
- A more explicit first-run onboarding flow
- Settings and user-facing explanation of tier behavior
- Finalized design handoff artifacts

## Future phases

### Phase 2

- Custom frequencies and skips
- Birthdays and anniversaries
- Better task prioritization
- Widgets and shortcuts
- Local analytics

### Phase 3

- Smart suggestions
- Calendar and communication integrations
- Cloud backup and sync
- Multi-device support
- Export and advanced insights
