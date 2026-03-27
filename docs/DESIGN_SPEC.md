# Design spec

This document is the designer-facing handoff spec for the Kneatr MVP. It should be used together
with `DESIGN_GUIDELINES.md` and `UX_BLUEPRINT.md`.

## Objective

Design the full MVP flow for a calm Android relationship-maintenance app that helps users decide who
to contact, act, and log the result with minimal friction.

## Deliverables

The Figma handoff should include:

- Foundations page
- Component library page
- Full screen set for all MVP flows
- Interactive prototype covering the main flows
- Responsive variants for phone and tablet/foldable layouts
- Export-ready launcher icon and any brand assets needed for implementation

## Recommended Figma file structure

1. Cover
2. Foundations
3. Components
4. Phone screens
5. Tablet and foldable screens
6. Prototype flow
7. Notes and handoff annotations

## Foundations

### Color system

Define:

- primary
- secondary
- tertiary
- error
- neutral
- neutral-variant
- surface and background roles
- due-state support colors for overdue, due today, and upcoming emphasis

Requirements:

- Must work in light and dark themes
- Must meet accessible contrast requirements
- Must not rely on color alone to communicate urgency

### Typography

Use a clear Android-friendly type system with:

- display or hero style only when needed
- strong section-header style
- clear card title and metadata hierarchy
- legible caption and helper text styles

The type system should feel calm and structured, not trendy or decorative.

### Spacing and shape

Define:

- spacing scale
- corner radius system
- elevation strategy
- list density rules

Preference:

- medium-soft shape language
- enough spacing to prevent cognitive crowding

### Iconography

- Use Material icons
- Keep icon usage semantically meaningful
- Avoid visual clutter from too many decorative icons

## Core components to design

The component library must include:

- app shell navigation item
- screen section header
- home contact card
- contacts list row
- search bar
- tier pill
- tag chip
- date / due-state row
- empty state
- loading state
- error state
- permission-required state
- floating action button
- bottom sheet shell
- communication log row
- destructive confirmation dialog

Each component should show:

- default state
- pressed / active state where relevant
- disabled state where relevant
- empty or placeholder behavior where relevant

## Screen specifications

### 1. Permission required / first-run screen

Purpose:

- Explain why contact access is needed
- Give the user a clear next action

Must include:

- concise headline
- brief explanation
- primary CTA to grant permission
- secondary action for later / exit behavior
- variant for "open settings" recovery path

### 2. Home screen

Purpose:

- Daily decision surface

Sections to design:

- Overdue
- Random
- Upcoming
- Contacts Due Today

Must include:

- section headers
- contact cards with clear hierarchy
- direct "mark complete" action
- tap target to open contact detail
- empty state per section

### 3. Contacts screen

Purpose:

- Browse and search the full contact set

Must include:

- search field
- grouped list behavior
- refresh action
- row states for name, tier, and summary metadata
- empty and no-results states

### 4. Contact detail screen

Purpose:

- Full management surface for one contact

Must include:

- top app bar and back behavior
- contact name
- tier section
- tags section
- last and next communication information
- quick reach-out actions
- communication history list
- floating action button for new log

State variants:

- no tags
- no communication history
- with multiple logs
- with long notes

### 5. Tier selector bottom sheet

Purpose:

- Pick or clear a tier

Must include:

- current selection state
- tier name
- cadence explanation, for example "every 7 days"

### 6. Tag selector bottom sheet

Purpose:

- Assign and remove tags

Must include:

- selected tags
- available tags
- save action
- clear affordance if appropriate

### 7. Add/edit communication log sheet

Purpose:

- Create or edit a communication event

Must include:

- communication type picker
- date input
- optional notes input
- save action
- edit mode variant

### 8. Error, loading, and empty states

Design dedicated system-consistent states for:

- empty contacts list
- empty home sections
- contact detail loading
- generic load failure
- permission denied

## Responsive requirements

### Phone

- Top-level navigation optimized for compact width
- Full-screen detail behavior
- Bottom sheet patterns for secondary editing flows

### Tablet and foldable

- Navigation rail or equivalent adaptive navigation
- Contacts list and contact detail shown as list/detail where appropriate
- Home can remain a single surface, but spacing and width should adapt intelligently

## Interaction and prototype requirements

Prototype the following flows:

1. First launch -> permission grant -> Home
2. Home -> Contact Detail -> add log -> updated detail
3. Contacts -> search -> Contact Detail
4. Contact Detail -> change tier
5. Contact Detail -> update tags
6. Permission denied -> settings recovery path

## Content guidance

Copy should be:

- short
- supportive
- non-judgmental
- practical

Avoid:

- shame language
- productivity-bro tone
- artificial urgency

## Open design questions to resolve in Figma

- How visually strong should due-state cues be before the UI feels stressful?
- Should the random section feel playful or equally utilitarian?
- How should tier meanings be explained without adding clutter?
- What is the best empty-state strategy for users with very few contacts?

## Handoff checklist

Before handoff is considered complete:

- All screens above exist in Figma
- Light and dark foundations are defined
- Components are reusable and named consistently
- The main prototype flows are linked
- Responsive variants are included
- Final copy suggestions are attached to ambiguous areas
