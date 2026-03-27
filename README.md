# Kneatr

[![Android CI](https://github.com/tobiolutimehin/Kneatr/actions/workflows/android-ci.yml/badge.svg)](https://github.com/tobiolutimehin/Kneatr/actions/workflows/android-ci.yml)

**Tagline:** Relationships, brilliantly organized.  
**Package:** `com.hollowvyn.kneatr`

Kneatr is an Android app for people who want a calmer, more intentional way to maintain
relationships. It imports contacts, helps users organize them by importance, surfaces who needs
attention, and makes it easy to log outreach without turning relationships into a noisy social CRM.

## Core idea

The product is built around a simple loop:

1. Import and organize important contacts.
2. Let Kneatr surface who is overdue, due soon, or worth checking in on.
3. Reach out and log the interaction.
4. Recalculate the next time that person should come back into view.

## Who it is for

- People with ADHD or other executive function challenges
- Busy professionals who want structure without social noise
- Users who prefer calm routines, clear prioritization, and low-friction task completion

## Built with

- Kotlin
- Jetpack Compose + Material 3
- Navigation 3 + adaptive layouts for phone and tablet/foldable patterns
- Room for local persistence
- DataStore for lightweight app preferences
- WorkManager for background sync work
- Hilt for dependency injection

## Project structure

- `app/` - Android application module
- `docs/` - Product, UX, architecture, and design documentation
- `.github/` - CI and GitHub project configuration

## Documentation

Start with the docs index: [`docs/README.md`](docs/README.md)

Recommended reading order:

1. [`docs/PRODUCT_BRIEF.md`](docs/PRODUCT_BRIEF.md)
2. [`docs/MVP_SCOPE.md`](docs/MVP_SCOPE.md)
3. [`docs/UX_BLUEPRINT.md`](docs/UX_BLUEPRINT.md)
4. [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
5. [`docs/DESIGN_SPEC.md`](docs/DESIGN_SPEC.md)
