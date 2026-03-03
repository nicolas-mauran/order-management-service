# Frontend (FE-01)

This folder contains the frontend scaffold for Sprint 4.

Implemented scope:

- FE-01 order list screen
- loading, empty, and error states
- filter/search/sort controls
- recruiter-oriented UI styling

## Run locally

```bash
cd frontend
npm install
npm run dev
```

The app starts on `http://localhost:5173`.

API calls to `/orders` are proxied to `http://localhost:8080` (configured in `vite.config.ts`).
