# 🎨 DESIGN_GUIDELINES.md

This document outlines the visual and interaction design principles for the Kneatr Android app. All
contributors and designers should refer to this to ensure consistency and brand fidelity.

---

## 🧠 Brand Keywords

- Relationships, brilliantly organized
- Intentional
- Clean
- Neurodivergent-friendly
- Calm & accessible
- Precise, but human

---

## 🎨 Color System

Generated
via [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/)

- **Primary:** Electric Blue (`#007aff`)
- **Secondary:** Soft Gray
- **Tertiary:** Light Jade or Warm Yellow (TBD)
- **Error:** Red (`#ff3b30`)
- **Neutral / Background:** White (`#ffffff`)
- **Neutral Variant:** Light Gray (`#f4f4f4`)

Colors must support dark theme variants as well.

---

## 🧩 Components

Use Material3 components only.

- **Buttons:** Elevated + tonal
- **Chips:** Assist + filter (used for tags and tiers)
- **Cards:** Contain contact data, interaction history
- **TextFields:** Outlined by default
- **Navigation:** Bottom nav or floating FAB as needed

All components should follow:

- Minimum 48x48dp tap targets
- Clear state changes (loading, empty, error, success)
- Avoid nested scrolling or excessive stacking

---

## 🖼️ Visual Assets

### Required from Designer

- App logo
- App launcher icon (in collaboration with developer)
- Onboarding and empty state banners
- Components spec sheet (all reused elements)
- App journey map (loop interaction flow)

---

## 🖋️ Typography

- **Font Family:** Google Fonts (TBD — e.g., Inter, Roboto, or Noto)
- Font sizes:
    - H1: 24sp
    - H2: 20sp
    - Body: 16sp
    - Caption: 12sp

Accessibility:

- Minimum 4.5:1 contrast ratio
- System font scaling support (SP, not DP)
- Font size options for users with visual needs

---

## 🌓 Dark Mode

Required.

- Must support full dark theme (M3 theming)
- Test across AMOLED and LCD screens
- Avoid pure black, prefer soft charcoal backgrounds

---

## 🧠 Accessibility

Kneatr aims to be neurodivergent-friendly:

- Reduce cognitive overload
- Clear, minimal, focused layouts
- Consistent gestures
- Optional animations only (respect system preferences)

---

## 🛠 Tooling

- All design work should be delivered in **Figma**
- Use the official **Material3 Design Kit**
- Color palettes should be tokenized (primary, secondary, etc.)
- Component variants documented in Figma
