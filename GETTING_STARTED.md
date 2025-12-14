# Getting Started

## Prerequisites
- JDK 21 (matches Gradle `sourceCompatibility`).
- Docker + Docker Compose (Postgres + ActiveMQ).
- Optional: Postman/cURL for API calls.

## One-time setup
1) Start infrastructure:
   ```bash
   docker compose up -d postgres activemq
   ```
   - Postgres: `localhost:5432`, db/user/password: `scorecard`.
   - ActiveMQ: `localhost:61616` (console at `http://localhost:8161`), creds `admin` / `admin`.
2) (Optional) Clean database if re-running: `docker compose down -v` to drop volumes, then start again.

## Run the apps (separate terminals)
- API (port 8080)
  ```bash
  ./gradlew :api:bootRun
  ```
  Liquibase creates the `SCORE_CARD` schema and seeds services/actions/transactions. Root redirects to the API docs.

- Monitor add-on (port 8081)
  ```bash
  ./gradlew :monitor:bootRun
  ```
  Root redirects to the score card list.

- Portal add-on (port 8082)
  ```bash
  ./gradlew :portal:bootRun
  ```
  Root redirects to the transaction list; use this to manage services/actions/transactions.

- Example app (port 8083, for developers)
  ```bash
  ./gradlew :example:bootRun
  ```
  Liquibase creates the `BANK` schema and seeds demo accounts for the bank transfer flow.

## Configuration notes
- API base URL for integrations: `http://localhost:8080/api/v1`.
- Messaging: ActiveMQ; JSON messages with `_type` header; `SCORE_CARD` header accompanies outbound action messages.
- Database: Postgres via Liquibase; seed data uses explicit IDs and resets sequences to avoid identity clashes.

## Useful commands
- Stop infra: `docker compose down` (add `-v` to drop volumes).
- Reset from scratch: `docker compose down -v && docker compose up -d`, then restart the apps.
