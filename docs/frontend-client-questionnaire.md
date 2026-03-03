# Frontend Client Questionnaire

Last update: 2026-03-03

Use this checklist before adding frontend stories to a sprint.

## 1. Product Goal

- What business outcome should the UI improve?
- What is the primary KPI (speed, error rate, adoption, throughput)?
- Is this interface internal (ops/traders) or external (customers)?

## 2. Users and Permissions

- Who are the user roles?
- Which role can create orders?
- Which role can cancel/fill orders?
- Do we need authentication and role-based authorization in MVP?

## 3. Core User Flows

- What are the top 3 must-have workflows for V1?
- Preferred default landing screen:
  - order list
  - create order form
  - dashboard summary
- Is the expected flow:
  - list -> open detail -> action
  - list -> inline action
  - create -> return to list

## 4. Order List Requirements

- Required columns (id, symbol, side, type, qty, limit price, status, created at, actions)?
- Need sorting?
- Need filtering (status/symbol/side/type/date)?
- Need text search?
- Need pagination from day 1?
- Need CSV export?

## 5. Create Order Form Requirements

- Required fields visible by default.
- Should `limitPrice` appear only for `LIMIT` type?
- Validation behavior:
  - inline validation
  - on submit only
- Confirm before submit?

## 6. Cancel/Fill UX

- Are cancel/fill buttons displayed for all rows, or only `NEW` orders?
- Need confirmation modal for destructive actions?
- Expected message style for business errors (toast/banner/inline)?

## 7. UI States and Feedback

- Loading behavior for list and actions.
- Empty state text and CTA.
- Error state text and retry action.
- Success notification requirements.

## 8. Non-Functional Requirements

- Desktop-only, mobile-first, or fully responsive?
- Browser support constraints.
- Accessibility level (keyboard support, contrast, labels).
- Performance target (e.g., list load under N seconds for N rows).

## 9. Design and Branding

- Existing design system or component library to follow?
- Branding constraints (colors, typography, logos)?
- Any reference screens or competitor examples?

## 10. Delivery Scope

- What is mandatory for MVP sprint?
- What is explicitly out of scope?
- Acceptance criteria per flow (Given/When/Then).
- UAT reviewer and sign-off process.
