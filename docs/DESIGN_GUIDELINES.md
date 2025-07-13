# 🎨 DESIGN\_GUIDELINES.md

These are the design standards for Kneatr to ensure consistency and brand clarity across all
components.

## Brand Keywords

"Relationships, brilliantly organized"

* Precise
* Clean
* Neurodivergent-friendly
* Minimalist

## Color Palette

* **Primary:** Electric Blue (`#007aff`)
* **Background:** White (`#ffffff`)
* **Surface:** Light Gray (`#f4f4f4`)
* **Accent/Success:** Jade Green
* **Error:** Red (`#ff3b30`)

## Typography

* Google Fonts (TBD with designer)
* Vary font size by tier (headline, body, caption)

## Component Principles

* Large tap targets
* No crowded screens
* Clear states: empty, loading, success, error
* Task-focused UI

## Motion

* Simple transitions only (fade/slide in)
* Reduce motion/animations for accessibility

## Interaction

- Primary action always visible
- Undo/snackbar for destructive actions

## Iconography

* Use Google Material Icons only
* Geometric, line-based, consistent

## Design Framework

* **Material3 for Android**
* All UI must adhere to M3 components, shapes, and accessibility

## Visual Assets Needed

* Launcher icon (to be co-decided with developer)
* Logo (aligned with "Kneatr" brand and tagline)
* Banners (for onboarding, empty states, and marketing)
* Components page (standardized visual spec sheet for UI components)
* Full UI loop journey (from onboarding to usage cycle)

## Canonical Layout Strategy (For Responsive Design)

- Phones: Bottom Navigation + Single-Pane Navigation (List → Detail)
- Tablets / Foldables (Unfolded): Navigation Rail + List-Detail Split Pane
- Leverage Material3 Scaffold, Navigation Components, and WindowSizeClass for responsiveness.

This ensures Kneatr looks intentional and modern across screen sizes, and aligns with Android's
canonical layout guidelines for productivity-focused apps.

---
