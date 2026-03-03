# Epic FE-01 - Frontend Orders UI

Last update: 2026-03-03

## Epic Statement

Build a recruiter-ready web interface that demonstrates the full order workflow on top of the existing API:

- create orders (MARKET and LIMIT)
- visualize current orders
- trigger cancel/fill transitions
- handle success and error states clearly

## Business Value

- Shows end-to-end delivery capability (backend + frontend + CI).
- Makes demo sessions shorter and more convincing.
- Improves project readability for non-backend interviewers.

## Scope

- In scope:
  - order list view
  - create order form
  - cancel/fill actions
  - UX states (loading, empty, success, error)
- Out of scope:
  - auth and RBAC
  - realtime updates
  - exports and advanced analytics
  - complex pagination strategy

## Increment Plan

### Increment I1 - Read flow foundation

Objective: make data visible and trustworthy quickly.

- FE-01 view order list
- baseline layout and API client setup
- empty/error/loading states for list

Definition of done:

- list renders from `/orders`
- recruiter can see statuses and key fields

### Increment I2 - Create flow

Objective: enable full command entry from UI.

- FE-02 create MARKET order
- FE-03 create LIMIT order
- inline and submit validation

Definition of done:

- new orders appear immediately after submit
- business errors are understandable

### Increment I3 - Lifecycle actions

Objective: demonstrate state transitions in one screen.

- FE-04 cancel order
- FE-05 fill order
- action controls based on status

Definition of done:

- `NEW -> CANCELLED` and `NEW -> FILLED` work from UI
- non-NEW rows cannot trigger invalid actions

### Increment I4 - Demo hardening

Objective: make the demo robust and interview-ready.

- FE-06 UX feedback polish
- FE-07 frontend tests in CI
- scripted demo flow documentation

Definition of done:

- no blocking UX gap in demo flow
- core frontend flows tested automatically

## Story Mapping

- FE-01, FE-02, FE-03, FE-04, FE-05, FE-06, FE-07:
  - see `docs/frontend-backlog-initial.md`

## Exit Criteria For Epic

- Recruiter can complete create/list/cancel/fill demo in under 5 minutes.
- No blocking bug on main flow.
- CI is green with backend and frontend checks.
