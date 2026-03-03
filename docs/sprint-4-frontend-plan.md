# Sprint 4 Plan - Frontend Order Desk MVP

Last update: 2026-03-03
Sprint status: Proposed

## Sprint Goal

Deliver a recruiter-ready UI that supports full core workflow:

- create order
- view order list
- cancel/fill `NEW` orders

## Sprint Input

- Epic:
  - `docs/frontend-epic-orders-ui.md`
- Client expectations:
  - `docs/frontend-client-questionnaire.md`
- Story catalog:
  - `docs/frontend-backlog-initial.md`

## Committed Scope

| Story | Description | Estimate (SP) |
|---|---|---|
| FE-01 | View order list | 3 |
| FE-02 | Create MARKET order | 3 |
| FE-03 | Create LIMIT order | 3 |
| FE-04 | Cancel NEW order | 2 |
| FE-05 | Fill NEW order | 2 |
| FE-06 | UX states and feedback | 3 |

Total proposed: 16 SP

Stretch:

- FE-07 frontend integration tests

## Delivery Sequence Inside Sprint

1. Setup frontend app shell, API client, environment config.
2. Implement FE-01 (list first).
3. Implement FE-02/FE-03 (create flows).
4. Implement FE-04/FE-05 (inline actions).
5. Implement FE-06 (polish, feedback, edge states).
6. Add FE-07 if capacity remains.

## Technical Notes

- UI technology: to be decided at sprint planning (React + Vite recommended for speed).
- API base URL must target existing backend service.
- Keep adapter-style separation in frontend (API client isolated from UI components).

## Sprint Definition of Done

- All committed stories satisfy acceptance criteria.
- Frontend build passes locally.
- Lint/test checks configured in CI for frontend scope.
- Demo script can be executed end-to-end without manual data patching.

## Demo Script (Sprint Review)

1. Open app -> list loads.
2. Create MARKET order -> appears as `NEW`.
3. Create LIMIT order -> appears as `NEW` with limit price.
4. Cancel first order -> status becomes `CANCELLED`.
5. Fill second order -> status becomes `FILLED`.
6. Show error handling on an invalid action attempt.

## Risks and Mitigations

- Risk: unclear UX expectations late in sprint.
  - Mitigation: freeze questionnaire answers before sprint start.
- Risk: frontend scope too broad for one sprint.
  - Mitigation: strict MVP scope, FE-07 as stretch.
- Risk: API contract mismatch.
  - Mitigation: validate with OpenAPI and early smoke tests.
