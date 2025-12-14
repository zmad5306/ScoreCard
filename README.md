# Score Card

Score Card is a sample implementation of the “score card” pattern: track long‑running, multi‑step transactions, record every action, and surface live status through REST and UI.

## What’s Inside
- **Core API**: the engine for defining services, actions, transactions, and score cards—built for product teams to integrate quickly.
- **Monitor (optional add‑on)**: a live scoreboard to visualize every transaction as it unfolds, with action-level drilldowns.
- **Portal (optional add‑on)**: a guided workspace to design services, wire actions, and build transaction blueprints without touching code.
- **Example services**: ready-made demos that show the end-to-end flow (e.g., a bank transfer) so teams can learn by running.
- **Integration library**: reusable domain models and client helpers (HTTP/JMS) to plug Score Card into your services with minimal effort.

## Data & Messaging
- **Database**: Postgres with Liquibase migrations; seed data for services, actions, transactions, and example accounts.
- **Messaging**: ActiveMQ; messages carry a `SCORE_CARD` header, and producers/consumers use JSON with a `_type` hint for polymorphic payloads.

## How It Works (functionally)
1) Define services and actions (REST or Portal).  
2) Assemble actions into transactions.  
3) Start a score card for a transaction (API or example flow).  
4) JMS messages are dispatched to the service queues; listeners call back to authorize and update status.  
5) Monitor UI shows progress; API exposes the same data for automation.

## Try It
- Use the **Portal** to browse and edit the model.
- Use the **Monitor** to watch score cards update in real time.
- Run the **bank transfer** example to see debit/credit actions execute under one score card.

## Need Setup Details?
See `GETTING_STARTED.md` for environment, Docker Compose, profiles, and run commands. This README stays focused on what the system does; the setup guide covers how to run it.***
