# Getting Started

## Prerequisites
- JDK 21 (matches Gradle `sourceCompatibility`).
- Docker + Docker Compose (used for Postgres + ActiveMQ).
- Optional: Postman/cURL for API calls.

## One-time setup
1) Start infrastructure:
   ```bash
   docker compose up -d postgres activemq
   ```
   - Postgres: `localhost:5432`, db/user/password: `scorecard`.
   - ActiveMQ: `localhost:61616` (console at `http://localhost:8161`).
2) (Optional) Clean database if re-running from scratch: drop the `scorecard` DB or `SCORE_CARD`/`BANK` schemas before restarting apps.

## Run the apps (in separate terminals)
- API (port 8080, Swagger UI at `/swagger-ui/index.html`):
  ```bash
  ./gradlew :api:bootRun
  ```
  Liquibase auto-runs: creates `SCORE_CARD` schema + seed data.

- Portal (port 8082):
  ```bash
  ./gradlew :portal:bootRun
  ```
  Web UI under `/portal` (e.g., `/portal/transaction/list`).

- Monitor (port 8081):
  ```bash
  ./gradlew :monitor:bootRun
  ```
  UI under `/monitor/scorecard/list`.

- Example app (port 8083):
  ```bash
  ./gradlew :example:bootRun
  ```
  Liquibase auto-creates `BANK` schema and seeds demo accounts.

## Configuration notes
- Broker creds: `admin` / `admin` (from `docker-compose.yml`).
- API base URL used by other services: `http://localhost:8080/api/v1`.
- Postgres is used instead of H2; schemas are created by Liquibase on app startup.

## Useful commands
- Tail logs: `./gradlew :api:bootRun --quiet` (add `--info` for debug).
- Stop infra: `docker compose down` (add `-v` to drop volumes if you want a clean DB).
- Re-run schema from scratch: stop apps, `docker compose down -v`, `docker compose up -d`, then restart apps.
