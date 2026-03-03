# Frontend Client Questionnaire

Last update: 2026-03-03

Use this checklist before adding frontend stories to a sprint.

## 1. Product Goal

- Business outcome:
  - Provide a recruiter-facing demo UI that proves end-to-end product delivery (API + UX + quality).
- Primary KPI:
  - A complete demo run (create -> list -> cancel/fill) in under 5 minutes with 0 blocking errors.
- Interface type:
  - Internal demo interface (not customer-facing production UI).

## 2. Users and Permissions

- User roles:
  - Single role for MVP: `Demo Trader`.
- Create permission:
  - `Demo Trader` can create orders.
- Cancel/fill permission:
  - `Demo Trader` can cancel and fill orders.
- Auth/RBAC in MVP:
  - No authentication for this demo iteration.
  - Add a visible `Demo mode` label in the UI.

## 3. Core User Flows

- Top 3 workflows for V1:
  - Create an order (MARKET and LIMIT).
  - View all orders with status and key fields.
  - Cancel/fill a `NEW` order directly from the list.
- Preferred default landing screen:
  - `order list`.
- Expected flow:
  - `list -> inline action` for cancel/fill.
  - `create -> return to list` with success feedback and visible new row.

## 4. Order List Requirements

- Required columns:
  - `id`, `symbol`, `side`, `type`, `quantity`, `limitPrice`, `status`, `createdAt`, `actions`.
- Sorting:
  - Default by `createdAt desc`.
  - User can sort by `createdAt`, `symbol`, `status`.
- Filtering:
  - Filters for `status`, `symbol`, `side`, `type`.
- Text search:
  - Yes, by symbol and order id prefix.
- Pagination from day 1:
  - No. Keep simple for MVP demo.
- CSV export:
  - No for MVP.

## 5. Create Order Form Requirements

- Required fields by default:
  - `symbol`, `side`, `type`, `quantity`.
- `limitPrice` behavior:
  - Show and require only when `type = LIMIT`.
- Validation behavior:
  - Inline validation + final validation on submit.
- Confirm before submit:
  - No confirmation modal (keep flow fast for demo).

## 6. Cancel/Fill UX

- Cancel/fill button behavior:
  - Actions are visible but disabled for non-`NEW` rows.
- Confirmation modal:
  - Confirmation for `cancel`.
  - No confirmation for `fill` (single click for demo speed).
- Error messages:
  - Toast for action-level errors and top banner for global API errors.

## 7. UI States and Feedback

- Loading behavior:
  - Skeleton for initial list load.
  - Spinner + disabled button during row action.
- Empty state:
  - Text: `No orders yet`.
  - CTA: `Create first order`.
- Error state:
  - Text: `Unable to load orders`.
  - Action: `Retry`.
- Success notifications:
  - Toast on create/cancel/fill success.

## 8. Non-Functional Requirements

- Responsiveness:
  - Fully responsive, desktop-first.
- Browser support:
  - Latest Chrome, Firefox, Edge.
- Accessibility:
  - Keyboard navigable form/actions.
  - Proper labels and visible focus states.
  - Sufficient color contrast for status badges.
- Performance target:
  - List load under 1.5s for up to 200 orders on local demo environment.

## 9. Design and Branding

- Existing design system:
  - No mandatory design system.
- Branding constraints:
  - Clean and professional style suitable for technical interview.
  - Avoid default bootstrap look.
- Visual references:
  - Inspired by trading desk clarity + modern SaaS admin simplicity.

## 10. Delivery Scope

- Mandatory for MVP sprint:
  - Order list with filters/search.
  - Create order form (MARKET/LIMIT).
  - Cancel/fill actions from list.
  - Clear loading/empty/error/success states.
- Explicitly out of scope:
  - Authentication and RBAC.
  - Pagination/export.
  - Realtime updates (websocket/polling).
  - Advanced analytics dashboard.
- Acceptance criteria per flow (summary):
  - Given valid create input, when submit, then order appears in list with `NEW` status.
  - Given `NEW` order, when cancel, then status becomes `CANCELLED`.
  - Given `NEW` order, when fill, then status becomes `FILLED`.
  - Given API failure, when action triggers, then user sees actionable error feedback.
- UAT and sign-off:
  - Reviewer: recruiter/interviewer acting as product stakeholder.
  - Sign-off after live demo script passes end-to-end without blocking issue.
