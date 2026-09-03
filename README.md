# Full Stack Finance Tracker

A full-stack personal finance application for recording income and expenses, reviewing balances, and understanding spending by category.

## Why this project

The project demonstrates how a modern frontend can communicate with a secured Java REST API backed by relational persistence. It also separates authentication concerns from application data and isolates transactions by authenticated user.

## Features

- Record, update, list, and delete income/expense transactions
- Dashboard showing income, expenses, and balance
- Category-based spending summary
- Recent transaction activity
- Firebase Authentication with Google sign-in
- Backend token verification with Firebase Admin SDK
- Per-user transaction isolation using the authenticated Firebase user ID
- JPA-based relational persistence
- H2 support for local development and PostgreSQL support for production
- Responsive React frontend

## Tech Stack

**Frontend**
- React.js
- Vite
- Firebase Authentication

**Backend**
- Java 17
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Data JPA
- Firebase Admin SDK

**Database**
- H2
- PostgreSQL

## Architecture

```text
React + Firebase Auth
        |
        | Bearer token
        v
Spring Boot REST API
        |
        +--> Firebase token verification
        |
        +--> Transaction / Summary services
        |
        v
Spring Data JPA
        |
        v
H2 / PostgreSQL
```

## REST API

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET | `/api/transactions` | List transactions for the authenticated user |
| GET | `/api/transactions/summary` | Return income, expenses, and balance |
| POST | `/api/transactions` | Create a transaction |
| PUT | `/api/transactions/{id}` | Update a transaction |
| DELETE | `/api/transactions/{id}` | Delete a transaction |

## Project Structure

```text
backend/
├── src/main/java/...
├── src/main/resources/
└── pom.xml

frontend/
├── src/
├── public/
└── package.json

database/
└── schema.sql
```

## Run Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

The default development configuration uses an in-memory H2 database and demo authentication mode. The API is available at `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The Vite development server normally runs at `http://localhost:5173`.

## Firebase Configuration

For real Firebase authentication, create a Firebase project, enable Google Authentication, and provide the required frontend configuration through environment variables. Configure the backend with Firebase Admin credentials through environment variables or another secure secret-management mechanism.

**Do not commit Firebase service-account JSON files, private keys, passwords, or other secrets to Git.**

## PostgreSQL Configuration

The repository includes a PostgreSQL-compatible schema under `database/schema.sql`. Configure the application with the database URL, username, password, and driver through environment variables before running against PostgreSQL.

## Example Request Body

```json
{
  "title": "Monthly Salary",
  "category": "Work",
  "amount": 3500.00,
  "type": "INCOME",
  "transactionDate": "2026-06-24",
  "notes": "Monthly paycheck"
}
```

## Key Engineering Concepts Demonstrated

- RESTful API design
- Authentication and authorization
- User-scoped data access
- Dependency injection with Spring Boot
- JPA entity and repository patterns
- Relational database persistence
- Frontend/backend separation
- Environment-based configuration

## Future Improvements

- Automated unit and integration tests
- Production deployment with managed PostgreSQL
- Budget limits and spending alerts
- Pagination and advanced transaction filtering
- CI/CD pipeline

## License

No license is currently specified for this repository.
