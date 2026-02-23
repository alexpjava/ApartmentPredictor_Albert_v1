# Top 9 System Integration Patterns

> These patterns solve common challenges in connecting components, managing data consistency, enabling scalability, and supporting real-time or batch workloads.

![](https://ci3.googleusercontent.com/mail-img-att/AGAZnRp7X90mTKf62AYC844M086-2nzJ5a-h_TES91zJq5JKCwmURoxZxzDV_YH9RjS7V5HLn-ggwMWACDEKPJG4v_EXGn60cSf2db_PnkXR5xcaDQRICNZDgR7jKMpH1dEQmRUZfMIy9786OJSOUuT5YBRFtSnT1Qs1mbxgvRfCNv9yQlwVpBqu_93foK_mRd4A0blINW2ksYuzkGUlTnPWdIJrmtdinQWa-pYwLLhxocQylo-2q4IeUEQs_xvHwrFPZML-aRwkT268inqASnvL32us42v_rS82xXP4fFbkF7kVJqPqSyu10nV68Fyv64jSQG0GSIYiw722gjnx5Pi9P2cE4zo_PhT53wumugHijL5nhf9Y84kkDul4ed2wnBZ26eetN5zlWcWLNUvxd2ymXMhcmu9Bk0pLrg6F_dpqnrb7jN0HJQvS89kHvLqlKvexiaurtmoc_cU6CrJMs-gIdMm8yK93bMvx46avzVFi75YjCHYVqyVhWCs6YYC5fSX-MrwouJHA33W-rlHyhvfdoFL-j8E0QLBKoT1u0EXq61RpEuOOrzXunuXekcZvAnchV4c-vNOvKVBM69-ewg1oOcN3ZDHFreOatD7vYV1kdBeJ-2NtQxEwW4SVa2PPHyKiNRKdFwJfpWvDiUKgCOtR7AeJ3fpHlSy3hquDKHTVuIXooRg5VdFq3LIPsRsSYUOOMKKOPhkIJNlrc5Z77p9KNCcs8tFYpPsOJKc0zRma_dkqa_iBCq76d08oJXcZ51eJgUUUCdFkyX-Tr1zlF-6ydnMsFMw384U8TodNWRnU1sEgqvXFznYrFN-yRYUEBA0pv4tO1A1ZnBxFPMplqWqDOLKJkyQYHrhrAdtVM1VhxXX3znWXgNvIagMMVwNHqnqg26d6RIIdN9oA8K6CF2Kt4ajWe8ZSlaJD4sviXiHnW8E9gAQg73eyqTYjhJg46I_76AjsLw2W-abCQBOwQ3cke1WC8sSR6OB3BcPIycd6amUEu4ssvQbTn3J5tIVwzr6kKrwN6FqSvNO2-_h_Fv5xsOX4qZKIXV4zlH8z36JXchG16A=s0-l75-ft)

## 1. Peer-to-Peer (P2P)

Direct communication between services without a central coordinator or intermediary.

- **How it works** — Services talk straight to each other (e.g., Order Service calls Payment Service directly via HTTP/gRPC).
- **Key characteristics** — Decentralized, point-to-point connections.
- **Use cases** — Simple integrations like order → payment, or service-to-service calls in small systems.
- **Pros** — Low latency, no single point of failure from middleware.
- **Cons** — Coupling increases quickly; hard to manage authentication, rate limiting, or monitoring as the number of services grows.
- **Real-world example** — Early-stage startups or small-scale internal tools often use direct HTTP/gRPC calls between services (e.g., a basic e-commerce backend where Order Service directly invokes Inventory Service). For larger scale, many shift away, but simple P2P is common in monolithic-to-microservices transitions.

## 2. API Gateway

A single entry point that routes, secures, and manages all incoming client requests to multiple backend services.

- **How it works** — Clients send requests to one URL; the gateway handles **authentication**, **rate limiting**, **request routing**, **protocol translation** (e.g., REST to gRPC), and load balancing.
- **Key characteristics** — Centralized control layer (e.g., Kong, AWS API Gateway, Apigee).
- **Use cases** — Exposing microservices to web/mobile clients, unifying APIs.
- **Pros** — Simplifies client code, adds cross-cutting concerns in one place.
- **Cons** — Can become a bottleneck or single point of failure if not scaled properly.
- **Real-world example** — Netflix uses its Zuul-based API Gateway (now evolved) as the front door for all client requests, handling routing, security, and device-specific adaptations for millions of streaming users.  
  [Netflix API Gateway Overview](https://netflixtechblog.com/optimizing-the-netflix-api-5a3a6a0b8b0f)

## 3. Pub-Sub (Publish-Subscribe)

Decouples producers (publishers) from consumers (subscribers) using a message broker or event bus.

- **How it works** — Publishers send messages to a **topic**; subscribers receive messages they are interested in (one-to-many fan-out).
- **Key characteristics** — Asynchronous, event-driven; uses brokers like Kafka, RabbitMQ, AWS SNS/SQS, Google Pub/Sub.
- **Use cases** — Notifications, real-time updates, broadcasting changes (e.g., "OrderPlaced" event sent to inventory, shipping, analytics).
- **Pros** — Loose coupling, easy to add new subscribers.
- **Cons** — Eventual consistency; need to handle message ordering, duplicates, or dead-letter queues.
- **Real-world example** — Uber uses pub-sub (via Apache Kafka) to broadcast real-time events like ride requests, location updates, and driver assignments to multiple downstream systems (matching, ETA calculation, notifications).  
  [Uber's Kafka Usage](https://www.confluent.io/blog/kafka-at-uber/)

## 4. Request-Response

The classic synchronous pattern where a client sends a request and waits for a response.

- **How it works** — Client → Server (HTTP/gRPC), server processes and returns response immediately.
- **Key characteristics** — Blocking call, immediate result.
- **Use cases** — Standard REST/GraphQL APIs, user queries, form submissions.
- **Pros** — Simple to understand and implement; clear error handling.
- **Cons** — Tight coupling, cascading failures if downstream services are slow or down; poor for long-running tasks.
- **Real-world example** — Most web applications, such as Amazon's product detail page, use request-response via REST APIs where the frontend synchronously calls backend services for price, reviews, and availability.  
  [Amazon API Patterns](https://aws.amazon.com/blogs/architecture/)

## 5. Event Sourcing

Stores the state of an entity as a sequence of immutable events rather than updating records directly.

- **How it works** — Every change (e.g., OrderCreated, ItemAdded, PaymentReceived) is appended to an **event store**. Current state is rebuilt by replaying events.
- **Key characteristics** — Append-only log (often with Kafka or EventStoreDB); usually paired with projections or CQRS.
- **Use cases** — Audit-heavy domains (finance, e-commerce orders), systems needing temporal queries or "what-if" replays.
- **Pros** — Full history, easy auditing, flexible read models.
- **Cons** — Complexity in querying current state; event schema evolution required.
- **Real-world example** — Banking and fintech platforms (e.g., account balance systems) use event sourcing to record every transaction as an immutable event, enabling full audit trails and balance reconstruction.  
  [Event Sourcing in Fintech Example](https://www.tinybird.co/blog/event-sourcing-with-kafka)

## 6. ETL (Extract, Transform, Load)

Moves and prepares data from source systems to a target (usually a data warehouse or analytics store).

- **How it works** — **Extract** data from databases/files/apps → **Transform** (clean, enrich, aggregate) → **Load** into destination.
- **Key characteristics** — Batch-oriented, periodic jobs (Airflow, dbt, Talend, Spark).
- **Use cases** — Data warehousing, BI reporting, migrating data between systems.
- **Pros** — Handles complex transformations; good for historical/analytical workloads.
- **Cons** — Latency (not real-time); resource-heavy for large volumes.
- **Real-world example** — Many enterprises use ETL pipelines (e.g., via Apache Airflow or AWS Glue) to extract sales data from transactional databases, transform it, and load into Snowflake or BigQuery for business intelligence reporting.  
  [ETL in Modern Data Stacks](https://www.servicenow.com/community/developer-forum/top-9-architectural-patterns-for-data-and-communication-flow/m-p/2901430)

## 7. Batching

Accumulates multiple inputs over time or until a threshold, then processes them together as one group.

- **How it works** — Collect items → wait (time/volume) → process in bulk → output.
- **Key characteristics** — Reduces overhead of individual operations.
- **Use cases** — Bulk email sending, payment processing, nightly reports, database inserts.
- **Pros** — Higher throughput, lower cost (fewer API calls/transactions).
- **Cons** — Delayed processing; increased memory usage during accumulation.
- **Real-world example** — Payroll systems (e.g., ADP or many enterprise HR platforms) batch employee time entries throughout the pay period and process them together at the end for salary calculation and direct deposits.

## 8. Streaming Processing

Handles continuous, unbounded data streams in real time with low latency.

- **How it works** — Ingest events → process item-by-item or in micro-batches → output immediately (e.g., Kafka Streams, Flink, Spark Streaming).
- **Key characteristics** — Event/time-based windows, exactly-once semantics.
- **Use cases** — Fraud detection, real-time analytics, live dashboards, IoT sensor data.
- **Pros** — Near-real-time results, handles high velocity.
- **Cons** — Complex state management, harder debugging than batch.
- **Real-world example** — Netflix uses streaming processing (via Kafka and tools like Apache Flink/Spark) to handle real-time viewing events, personalize recommendations, and monitor playback metrics for millions of concurrent streams.  
  [Netflix Real-Time Data Processing](https://netflixtechblog.com/)

## 9. Orchestration

A central component coordinates the execution of multiple services or steps in a workflow.

- **How it works** — Orchestrator calls Service A → waits → calls Service B → handles branching/errors → etc. (e.g., Temporal, Camunda, AWS Step Functions).
- **Key characteristics** — Centralized control, sagas for distributed transactions.
- **Use cases** — Order fulfillment (payment → inventory → shipping), approval workflows.
- **Pros** — Clear sequence and error handling; easy to monitor.
- **Cons** — Single point of failure; can create tight coupling if overused.
- **Real-world example** — Many AWS-based companies use AWS Step Functions to orchestrate serverless workflows like image processing pipelines or order fulfillment sequences (e.g., payment verification → inventory check → shipping notification).  
  [AWS Step Functions Examples](https://aws.amazon.com/step-functions/)

## Reference

- [ByteByteGo](https://bytebytego.com/)-style inspiration — keep learning system design fundamentals!
