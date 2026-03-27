# Kneatr documentation

This folder is the blueprint for the product. `INFRA_SETUP.md` stays as the reusable repo and
setup handbook; the rest of the files describe Kneatr itself.

## Core project docs

| Document                    | Audience                     | Purpose                                                                                   |
|-----------------------------|------------------------------|-------------------------------------------------------------------------------------------|
| `PRODUCT_BRIEF.md`          | Product, engineering, design | Explains the problem, users, value proposition, principles, and product direction         |
| `MVP_SCOPE.md`              | Product, engineering         | Defines the launch slice, acceptance criteria, out-of-scope items, and current status     |
| `UX_BLUEPRINT.md`           | Product, design, engineering | Defines app structure, flows, screens, states, and interaction rules                      |
| `ARCHITECTURE.md`           | Engineering                  | Explains the codebase structure, technical stack, data model, sync rules, and key systems |
| `ARCHITECTURE_DECISIONS.md` | Engineering                  | Records the major architectural decisions and their consequences                          |
| `DESIGN_GUIDELINES.md`      | Design, engineering          | Brand and UI guardrails that should stay stable across screens                            |
| `DESIGN_SPEC.md`            | Design                       | Designer-facing screen, component, state, and handoff specification                       |

## Supporting docs

| Document                    | Purpose                                                            |
|-----------------------------|--------------------------------------------------------------------|
| `INFRA_SETUP.md`            | Reusable project bootstrap and repo operations handbook            |
| `ARCHITECTURE_DESICIONS.md` | Compatibility pointer to the canonical architecture decisions file |

## Suggested reading order

1. Product brief
2. MVP scope
3. UX blueprint
4. Design spec
5. Architecture
6. Architecture decisions

## Source-of-truth rule

If two docs disagree:

1. `PRODUCT_BRIEF.md` wins for product intent.
2. `MVP_SCOPE.md` wins for launch scope.
3. `UX_BLUEPRINT.md` wins for flow and screen behavior.
4. `DESIGN_SPEC.md` wins for Figma deliverables and UI handoff details.
5. `ARCHITECTURE.md` wins for technical implementation direction.
