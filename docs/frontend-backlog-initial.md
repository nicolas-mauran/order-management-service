# Frontend Backlog (Initial)

Last update: 2026-03-03

## Epic FE-01 - Order Desk Web UI

Build a web UI to create and monitor orders using existing API endpoints.

## Story FE-01 - View order list

As a trader, I want to see all orders in a table so that I can monitor current activity.

Acceptance criteria:

- Given orders exist, when I open the app, then I see a table with id, symbol, side, type, quantity, limitPrice, status, createdAt.
- Given no orders exist, when I open the app, then I see a clear empty state.
- Given API failure, when list loading fails, then I see an error state with retry action.

## Story FE-02 - Create MARKET order

As a trader, I want to create a MARKET order from a form so that I can submit quickly.

Acceptance criteria:

- Given I fill symbol, side, quantity, and type `MARKET`, when I submit, then a new order is created.
- Given type is `MARKET`, when form is shown, then limitPrice is hidden or disabled.
- Given invalid input, when I submit, then validation errors are displayed clearly.

## Story FE-03 - Create LIMIT order

As a trader, I want to create a LIMIT order with limitPrice so that I can define execution price.

Acceptance criteria:

- Given type is `LIMIT`, when form is shown, then limitPrice field is required.
- Given a positive limitPrice, when I submit, then order is created successfully.
- Given missing/invalid limitPrice, when I submit, then the form blocks submission and explains why.

## Story FE-04 - Cancel order from list

As a trader, I want to cancel a `NEW` order from the list so that I can stop it.

Acceptance criteria:

- Given an order status is `NEW`, when I click cancel, then API `/orders/{id}/cancel` is called and row status updates to `CANCELLED`.
- Given status is not `NEW`, when I attempt cancel, then UI prevents action or shows business error.
- Given cancel fails, when response is error, then error message is visible and row remains unchanged.

## Story FE-05 - Fill order from list

As a trader, I want to fill a `NEW` order from the list so that I can simulate execution.

Acceptance criteria:

- Given an order status is `NEW`, when I click fill, then API `/orders/{id}/fill` is called and row status updates to `FILLED`.
- Given status is not `NEW`, when I attempt fill, then UI prevents action or shows business error.
- Given fill fails, when response is error, then error message is visible and row remains unchanged.

## Story FE-06 - Basic UX quality for MVP

As a user, I want clear loading/success/error feedback so that the UI feels reliable.

Acceptance criteria:

- Loading indicator appears for initial list load and action buttons during requests.
- Success feedback appears after create/cancel/fill.
- Error feedback appears with actionable text.

## Story FE-07 - Frontend integration tests

As a maintainer, I want automated tests for critical frontend flows so that regressions are detected in CI.

Acceptance criteria:

- Tests cover: list rendering, create MARKET, create LIMIT, cancel flow, fill flow.
- CI runs frontend tests on each push/PR.

## Proposed MVP Sprint Scope

- In scope: FE-01, FE-02, FE-03, FE-04, FE-05, FE-06
- Out of scope: pagination, advanced filters, auth, exports, realtime updates

## Dependencies

- Stable backend endpoints:
  - `POST /orders`
  - `GET /orders`
  - `POST /orders/{id}/cancel`
  - `POST /orders/{id}/fill`
