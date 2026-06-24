# Full Stack Finance Tracker

A complete starter finance tracker built with **Java Spring Boot**, **React.js**, **Firebase Authentication**, **REST APIs**, and **SQL**. It lets users record income and expenses, view totals, inspect category spending, and delete transactions.

## Features

- React dashboard with responsive cards, transaction form, category summary, and recent activity list.
- Java Spring Boot REST API with CRUD endpoints for transactions.
- Firebase Authentication support using Google sign-in and Firebase Admin token verification.
- SQL persistence through Spring Data JPA. H2 is enabled for quick local demos, and PostgreSQL can be used in production.
- Per-user transaction isolation through the authenticated Firebase user id.

## Project structure

```text
backend/    Spring Boot API, Firebase security, JPA models, REST controllers
frontend/   Vite React application and Firebase web client
database/   SQL schema for PostgreSQL-compatible databases
```

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/transactions` | List current user's transactions |
| GET | `/api/transactions/summary` | Return income, expenses, and balance |
| POST | `/api/transactions` | Create a transaction |
| PUT | `/api/transactions/{id}` | Update a transaction |
| DELETE | `/api/transactions/{id}` | Delete a transaction |

## Run locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

By default the API runs at `http://localhost:8080`, uses an in-memory H2 database, and runs in demo auth mode with user id `demo-user`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The app runs at `http://localhost:5173` and calls `http://localhost:8080/api` by default.

## Firebase setup

1. Create a Firebase project and enable Google Authentication.
2. Add a web app and copy its config values into `frontend/.env.local`:

```env
VITE_API_URL=http://localhost:8080/api
VITE_FIREBASE_API_KEY=your-key
VITE_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=your-project-id
VITE_FIREBASE_APP_ID=your-app-id
```

3. Create a Firebase service account JSON and provide it to the backend:

```bash
export FIREBASE_ENABLED=true
export FIREBASE_SERVICE_ACCOUNT='{"type":"service_account", ... }'
```

If Firebase variables are omitted, the app still works in demo mode for local development.

## PostgreSQL configuration

Run `database/schema.sql` in your PostgreSQL database, then start the backend with:

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/finance_tracker
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
export DATABASE_DRIVER=org.postgresql.Driver
cd backend && mvn spring-boot:run
```

## Example transaction JSON

```json
{
  "title": "Salary",
  "category": "Work",
  "amount": 3500.00,
  "type": "INCOME",
  "transactionDate": "2026-06-24",
  "notes": "Monthly paycheck"
}
```
