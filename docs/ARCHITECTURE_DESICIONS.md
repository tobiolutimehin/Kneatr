# Architecture Decisions - Kneatr

## Module Structure

- Single monolithic module (`app`)

## Tech Stack

- Kotlin
- Jetpack Compose
- Room Database
- WorkManager for reminders
- ViewModel + StateFlow

## Decisions

- Room used for local persistence (no sync yet)
- WorkManager handles recurring reminders
- Jetpack Compose used across the entire UI layer
