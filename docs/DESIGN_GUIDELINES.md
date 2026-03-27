# Design guidelines

This document captures the design rules that should remain stable even as the product evolves. For
screen-by-screen handoff details, use `DESIGN_SPEC.md`.

## Brand promise

Kneatr should feel like a calm personal system for maintaining relationships, not a social app and
not a corporate CRM.

## Brand attributes

- Precise
- Calm
- Organized
- Gentle
- Trustworthy
- Neurodivergent-friendly

## Visual direction

- Base the UI on Material 3 for Android
- Keep layouts open, breathable, and low-noise
- Use color to support clarity, not decoration overload
- Prefer neutral surfaces with a confident accent color
- Preserve a sense of warmth even inside structured productivity flows

## UX principles

### Reduce decision fatigue

The interface should narrow choices. Users should not need to think hard about what to do next.

### Make urgency legible

Overdue, due today, upcoming, and random contacts should feel meaningfully different at a glance.

### Keep actions close to context

Users should be able to log communication or take the next step from the screen where they discover
the need.

### Avoid shame mechanics

No streak language, guilt-driven messaging, or manipulative notifications.

## Accessibility requirements

- Support large tap targets throughout
- Maintain strong color contrast in both light and dark themes
- Do not rely on color alone to communicate due state
- Respect reduced-motion expectations
- Use clear typography hierarchy and avoid dense information blocks

## Motion

- Keep motion subtle and functional
- Use transitions to reinforce continuity between list, detail, and sheet states
- Avoid decorative motion that competes with task completion

## Iconography

- Use Material icons or a closely aligned icon set only
- Prefer simple, line-based icons
- Use iconography to reinforce action categories, not for ornament

## Copy tone

- Clear
- Brief
- Supportive
- Non-judgmental

Avoid language that sounds like nagging, scoring, or surveillance.

## Layout guidance

- Phones: compact navigation, single primary pane, clear top-level section switching
- Tablets and foldables: larger-screen navigation with list/detail behavior where appropriate
- Maintain strong hierarchy between section headers, contact cards, and detail content

## Visual assets still required

- App launcher icon
- Brand mark or wordmark
- Empty-state illustrations or banners, if used
- Foundations page in Figma
- Component library page in Figma
- Full app flow and prototype links in Figma
