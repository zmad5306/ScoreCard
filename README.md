# 🧮 Score Card

Score Card is a Spring Boot–based reference implementation of the **Score Card pattern** for tracking long-running, multi-step transactions across services.

It combines:

* A **REST API** for configuring services, actions, and transactions
* A **JMS-based orchestration engine** (embedded ActiveMQ broker)
* A **monitoring UI** for live score card status
* A **configuration portal** for managing your model
* A set of **example flows** (hello world + bank transfer)

Everything runs as a single Spring Boot application with feature sets enabled via **Spring profiles**.

---

## 💡 Core Concepts

Score Card models a distributed transaction as a set of related “actions”:

* **Service**
  A logical system that owns one or more actions (e.g. `payments`, `ledger`, `notifications`).

* **Action**
  A unit of work performed by a service in the context of a transaction (e.g. `debit`, `credit`, `sendEmail`).

* **Transaction**
  An ordered set of actions that together implement a business process (e.g. “Bank Transfer” with `debit` and `credit`).

* **Score Card**
  A runtime record for **one execution** of a transaction.
  It tracks:

  * Overall status
  * Per-action status (`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, etc.)
  * Metadata captured during processing

The Score Card engine sends JMS messages for each action and keeps the score as work progresses.

---

## 🧱 Architecture & Profiles

This project is a single Gradle module but is organized into logical “modules” controlled by Spring profiles:

* `core` (always on)

  * JPA entities and repositories
  * JMS listeners and Score Card engine
  * REST controllers under `/api/v1/...`
  * In-memory **H2** database (`jdbc:h2:mem:testdb`), console at `/h2-console`

* `api`

  * Public REST API (e.g. `/api/v1/service`, `/api/v1/action`, `/api/v1/transaction`, `/api/v1/scorecard`)
  * Java **API wrapper** (`ScoreCardApiService`, `TransactionApiService`) for internal JVM clients

* `monitor`

  * Monitoring UI under `/monitor`
  * List and drill into live score cards

* `portal`

  * Configuration portal under `/portal`
  * WebFlow-based UI for managing services, actions, and transactions

* `example`

  * Sample JMS services (`example.messaging.Service1`, bank transfer example, etc.)
  * Demonstrates how to consume JMS messages and interact with Score Card via the Java API

* `dev`

  * Seeds base data into the database for local development
  * Useful together with `example` for a click-and-run demo

The Gradle `bootRun` task is preconfigured to enable the “full stack”:

```groovy
bootRun {
    systemProperty 'spring.profiles.active', 'api,monitor,portal,example,dev'
}
```

---

## 🚀 Getting Started

### Prerequisites

* Java 11+
* (Optional) Gradle installed – otherwise use the included `gradlew` wrapper
* No external JMS broker required – an **embedded ActiveMQ broker** is started by the app

### Clone & Run

```bash
git clone https://github.com/<your-org>/ScoreCard.git
cd ScoreCard

# Using the Gradle wrapper
./gradlew bootRun
```

By default this starts the app on:

* Application: `http://localhost:8080`
* H2 Console: `http://localhost:8080/h2-console`

  * URL: `jdbc:h2:mem:testdb`

To run from a fat JAR:

```bash
./gradlew build
java -jar build/libs/scorecard-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=api,monitor,portal,example,dev
```

---

## 🌐 HTTP Endpoints

### REST API (Core / API)

The primary REST API is versioned under `/api/v1`.

Some key resources:

* **Services**

  * `GET  /api/v1/service` – list services
  * `POST /api/v1/service` – create / update a service
  * `DELETE /api/v1/service/{service_id}` – delete a service

* **Actions**

  * `GET  /api/v1/action` – list actions
  * `POST /api/v1/action` – create / update an action
  * `DELETE /api/v1/action/{action_id}` – delete an action

* **Transactions**

  * `GET  /api/v1/transaction` – list transactions
  * `POST /api/v1/transaction` – create / update a transaction
  * `DELETE /api/v1/transaction/{transaction_id}` – delete a transaction

* **Score Cards**

  * `GET  /api/v1/scorecard` – paged list of score cards
  * `GET  /api/v1/scorecard/{score_card_id}` – detail view
  * `PUT  /api/v1/scorecard` – create a score card for a transaction
  * `POST /api/v1/scorecard` – authorize an action
  * `POST /api/v1/scorecard/{score_card_id}` – update action status

> A Postman collection is included at
> `ScoreCard.postman_collection.json` to explore these endpoints.

### Swagger UI

When the `api` profile is active, API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html#/
```

---

## 📊 UIs

### Monitor UI

* Base URL: `http://localhost:8080/monitor`
* Redirects to `http://localhost:8080/monitor/scorecard/list`
* Lets you:

  * See active / recent score cards
  * Drill into a score card’s actions and statuses

### Portal (Configuration UI)

* Base URL: `http://localhost:8080/portal`
* Redirects to `http://localhost:8080/portal/transaction/list`
* Features:

  * Manage **services**
  * Manage **actions** (and their service mapping)
  * Build **transactions** from actions
  * Navigate using Spring WebFlow-backed screens

---

## 📦 JMS Integration

Score Card uses JMS to orchestrate work:

* An **embedded ActiveMQ broker** is started with the app.
* Actions are dispatched to JMS queues/topics named by service (e.g. `"service1"`).
* A `SCORE_CARD` header is attached to each message; your handler uses it to:

  * Authorize the work
  * Report back completion / failure
* Example listener (`example` profile):

```java
@Component
public class Service1 {

    @Autowired
    private ScoreCardApiService scoreCardApiService;

    @JmsListener(destination = "service1",
                 selector = "ACTION='action1'")
    @Transactional
    public void action1(String message,
                        @Header("SCORE_CARD") String scoreCardHeader) {
        // 1. Ask Score Card if this action should run
        Authorization auth = scoreCardApiService.authorize(scoreCardHeader);

        // 2. Do work (or skip/cancel/wait based on auth)

        // 3. Update status and metadata
        scoreCardApiService.updateStatus(scoreCardHeader,
                                         ScoreCardActionStatus.COMPLETED,
                                         metadata);
    }
}
```

This pattern is what you’ll reuse to integrate your own services.

---

## 🔍 Example Flows

With `example` and `dev` profiles enabled, some demo flows are exposed:

* **Hello World Example**

  ```http
  GET http://localhost:8080/app/example
  ```

  * Looks up a predefined transaction
  * Creates a score card
  * Sends messages for each action
  * Example services process and mark the actions completed

* **Bank Transfer Example**

  ```http
  GET http://localhost:8080/app/bank/transfer \
      ?fromAccountId=123 \
      &toAccountId=456 \
      &amount=10.00
  ```

  * Uses a “Bank Transfer” transaction with `debit` and `credit` actions
  * Dispatches messages to debit and credit services
  * Uses Score Card to track status across both legs

You can watch these flows in real time via the **Monitor UI**.

---

## 🧪 Testing

Unit and integration tests are under `src/test/java`.

To run the test suite:

```bash
./gradlew test
```

---

## 📝 Developer Notes

* Spring Boot DevTools is configured for auto-reload in IDEs like IntelliJ.
* The default active profiles in `bootRun` (`api,monitor,portal,example,dev`) give you:

  * REST API
  * Swagger UI
  * Monitor UI
  * Portal UI
  * Seeded data and examples

---

## 💬 Next Steps

Some ideas for extending Score Card:

* Add persistence beyond the in-memory H2 database (e.g. Postgres/MySQL)
* Integrate with external JMS broker(s)
* Add authentication/authorization around the APIs and UIs
* Extend the example domain(s) with real-world workflows

