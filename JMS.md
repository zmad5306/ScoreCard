# JMS Integration Guide (API Focus)

This project exposes a simple JMS contract so services can participate in Score Card–managed transactions.
Examples are excluded here; this is the contract surface for your own services.

## Broker & Payloads
- Broker: ActiveMQ.
- Payloads: JSON (text messages) with a `_type` field for polymorphic binding.
- Header: `SCORE_CARD` contains the score card context as JSON.

## Queues & Selectors
- **scorecard** (inbound to API)
  - `ACTION='CREATE'` with body `CreateRequest { score_card_id, transaction_id }` to start a score card.
  - `ACTION='UPDATE'` with body `UpdateRequest { score_card_id, action_id, status, metadata? }` to change status.
- **service queues** (outbound from API)
  - Named by service path (e.g., `account`, `service1`, etc.).
  - Messages carry `SCORE_CARD` header. Your service listens with a selector per action, e.g. `ACTION='debit'`.

## Message Shapes
- `SCORE_CARD` header (JSON):
  ```json
  {
    "_type": "dev.zachmaddox.scorecard.lib.domain.ScoreCardHeader",
    "score_card_id": 123,
    "action_id": 456,
    "path": "debit"
  }
  ```
- CreateRequest (body on `scorecard` queue, ACTION='CREATE'):
  ```json
  { "_type": "dev.zachmaddox.scorecard.common.domain.CreateRequest",
    "score_card_id": 123,
    "transaction_id": 42 }
  ```
- UpdateRequest (body on `scorecard` queue, ACTION='UPDATE'):
  ```json
  { "_type": "dev.zachmaddox.scorecard.common.domain.UpdateRequest",
    "score_card_id": 123,
    "action_id": 456,
    "status": "COMPLETED",
    "metadata": { "key": "value" } }
  ```

## Service-side Flow
1) Receive message on your service queue with selector for the action (e.g., `ACTION='debit'`).
2) Parse `SCORE_CARD` header to get `score_card_id` and `action_id`.
3) Optionally call the REST API to authorize (`POST /api/v1/scorecard` with `AuthorizationRequest`).
4) Do the work. On finish or failure, send `UpdateRequest` to the `scorecard` queue with `ACTION='UPDATE'`.
5) Include metadata if useful; it will be attached to the score card action.

## Status Values
`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, `CANCELLED`, `UNKNOWN`.
Use `PROCESSING` when you start work, `COMPLETED` or `FAILED` when done, `CANCELLED` if you skip, `UNKNOWN` for ambiguous outcomes.

## Converters & Trust
- The app uses `MappingJackson2MessageConverter` (TEXT, `_type` header). Make sure your producers and consumers also send/expect `_type`.
- ActiveMQ connection factories are set to trust packages under `dev.zachmaddox.scorecard`; align your payload types to those.

## Error Handling
- Messages that lack `_type` or use untrusted types will fail conversion.
- If you seed data with explicit IDs, sequences are reset by Liquibase to avoid identity collisions.

## Minimal Producer Example (pseudo)
```java
// send work to a service queue
jmsTemplate.convertAndSend("account", payload, m -> {
  m.setStringProperty("ACTION", "debit");
  m.setStringProperty("SCORE_CARD", scoreCardHeaderJson);
  return m;
});

// report status back
UpdateRequest update = new UpdateRequest(scoreCardId, actionId, ScoreCardActionStatus.COMPLETED, Map.of());
jmsTemplate.convertAndSend("scorecard", update, m -> { m.setStringProperty("ACTION", "UPDATE"); return m; });
```

Keep these conventions and your services will interoperate cleanly with the Score Card API.***
