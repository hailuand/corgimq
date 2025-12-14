# Corgi Message Queue (CorgiMQ)

![mascot.jpg](mascot.jpg)

| Workflow     | Status                                                                                                                                                                                                                                                               |
|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Build & test | ![build](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml/badge.svg) [![codecov](https://codecov.io/github/hailuand/corgimq/graph/badge.svg?token=NYQYU42L1U)](https://codecov.io/github/hailuand/corgimq)                                         |
| Quality      | [![OpenSSF Scorecard](https://api.securityscorecards.dev/projects/github.com/hailuand/corgimq/badge)](https://securityscorecards.dev/viewer/?uri=github.com/hailuand/corgimq) ![codeql](https://github.com/hailuand/corgimq/actions/workflows/codeql.yaml/badge.svg) |
| Packages     | [![Maven Central Version](https://img.shields.io/maven-central/v/io.github.hailuand/corgimq?color=blue)](https://central.sonatype.com/artifact/io.github.hailuand/corgimq)                                                                                           |
| Docs         | [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)  [![javadoc](https://javadoc.io/badge2/io.github.hailuand/corgimq/javadoc.svg)](https://javadoc.io/doc/io.github.hailuand/corgimq)                |


A lightweight message queue library built using Java's JDBC API. Similar in spirit to [AWS SQS](https://aws.amazon.com/sqs/)
and [Redis Simple Message Queue](https://github.com/smrchy/rsmq), but entirely on top of your DBMS.

---

### Features
- Lightweight: bring **just your DBMS.** :rocket:
- Batteries included: sensible out-of-the-box defaults with a few optional knobs to get you dangerous _fast_. :battery:
- Transactional: shared access of JDBC `Connection` available to provide transactional message handling. :handshake:
- Auditable: audit information in the queue captures if, when, and who has read a message. :detective:
- Guaranteed **exactly-once delivery** of a message to a reader - if someone's currently reading it, no one else receives it.
- Messages remain in queue until removed.

---

### Index
* [Compatibility](#compatibility)
* [Benchmarks](#-benchmarks)
* [Get started](#-get-started)
  * [Creating a queue](#creating-a-queue)
  * [Pushing messages](#pushing-messages)
  * [Reading messages](#reading-messages)
* [Configuration](#-configuration)
* [Notes](#-notes)
* [References](#references)

---

### Compatibility

#### Java
| CorgiMQ Version | JDK |
|---------------|-----|
| `0.1+`        | 21  |


#### RDBMS
RDBMS in this list have been tested for library compatibility and are included in the test suites.

| DBMS                 | Status             |
|----------------------|--------------------|
| H2                   | :white_check_mark: |
| CockroachDB          | :white_check_mark: |
| MySQL                | :white_check_mark: |
| Microsoft SQL Server | :white_check_mark: |
| Oracle DB            | :microscope:       |
| Postgres             | :white_check_mark: |

---

### 🧪 Benchmarks
Benchmarks are available here: [GitHub Pages - JMH Benchmark](https://hailuand.github.io/corgimq/dev/bench/)

Benchmarking is performed using [Java Microbenchmark Harness](https://github.com/openjdk/jmh), measuring throughput of public API methods.

While a benchmark suite is provided, it does not guarantee similar results when used in all environments. Users are encouraged to run their own benchmarks and test
for their own use cases.

### 🚀 Get started
#### Creating a queue
A message queue is managed by an instance of `MessageQueue`.

```java
Connection conn = // ...
MessageQueue messageQueue = MessageQueue.of(MessageQueueConfig.of("poneglyphs"), conn); // Name of queue, table will have '_q' suffix
messageQueue.createTableWithSchemaIfNotExists();
```

```
postgres=# \dt mq.*
           List of relations
 Schema |     Name     | Type  |  Owner
--------+--------------+-------+---------
 mq     | poneglyphs_q | table | shanks
(1 row)
```

#### Pushing messages
Once a `MessageQueue` is created, data to enqueue is wrapped as a `Message`. A `Message` is enqueued with `push(List)`:

```java
Message message1 = Message.of("Whole Cake Island");
Message message2 = Message.of("Zou");
messageQueue.push(List.of(message1, message2));
```

Table after push:

```
postgres=# select * from mq.poneglyphs_q;
                  id                  |        data       |        message_time        | read_count | read_by | processing_time
--------------------------------------+-----------------+----------------------------+------------+---------+-----------------
 9245867e-f7f1-40e4-9142-bb1457aff9ec | Whole Cake Island | 2024-02-27 10:12:57.486346 |          0 |         |
 a174f9d1-d3a9-4583-9396-d3ed575a4ebf | Zou               | 2024-02-27 10:12:57.486346 |          0 |         |
(2 rows)
```

#### Reading messages
Unread `Message`s in the queue are read in ascending `message_time` with an instance of `MessageHandler`:

```java
MessageHandler messageHandler = MessageHandler.of(messageQueue, MessageHandlerConfig.of(1)); // Read one message at a time
Supplier<Connection> connectionSupplier = // Code to acquire a connection to database
messageHandler.listen(connectionSupplier, messageHandlerBatch -> {
    for (Message message : messageHandlerBatch.messages()) {
        System.out.printf("Shanks - we found a road poneglyph at %s!%n", message.data());
    }
    return messageBatch.messages(); // List of Messages to be popped
    });
// "Shanks - we found a road poneglyph at Whole Cake Island!"
```

Table after handler execution:
```
postgres=# select * from mq.poneglyphs_q;
                  id                  |        data       |        message_time        | read_count | read_by  |      processing_time
--------------------------------------+-----------------+----------------------------+------------+----------+----------------------------
 a174f9d1-d3a9-4583-9396-d3ed575a4ebf | Zou               | 2024-02-27 10:12:57.486346 |          0 |          |
 9245867e-f7f1-40e4-9142-bb1457aff9ec | Whole Cake Island | 2024-02-27 10:12:57.486346 |          1 | beckman  | 2024-02-27 10:14:29.911197
(2 rows)
```
After read, `Messages` have their `read_count`, `read_by`, and `processing_time` updated. Subsequent 
calls to `listen()` no longer receive them.

---

### ⚙️ Configuration
#### Message queue

🔤 `queueName`

Name of message queue. Each queue has its own table within the `mq` schema, suffixed with `_q`.

#### Message handler

🔢 `messageBatchSize`

Maximum number of `Message`s to serve to a `MessageHandler` in a single batch. 

_Default:_ `10`

### ✏️ Notes
#### Locking
A `Message` currently being read by a `MessageHandler` has its row locked until the function completes. If multiple
`MessageHandler`s are operating on the same queue, locked rows are skipped to prevent the same message being received
by different handlers. This is achieved using the RDBMS's `SELECT FOR UPDATE` command. Thus, it's important to consider the
isolation level configured by the `Connection` provided - `READ COMMITTED` or stronger.

#### Transactional message handling
The transaction's `Connection` can be accessed through the `MessageHandlerBatch` argument passed to the
`Function`'s input:
```java
MessageHandler messageHandler = MessageHandler.of(messageQueue, MessageHandlerConfig.of(1));
Supplier<Connection> connectionSupplier = // ...
messageHandler.listen(connectionSupplier, messageHandlerBatch -> {
    try(Statement statement : messageHandlerBatch.transactionConnection()) {
        // Do something with Statement to participate in transaction
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    
    return messageBatch.messages();
    });
```

---

### References
- https://adriano.fyi/posts/2023-09-24-choose-postgres-queue-technology/
- https://dagster.io/blog/skip-kafka-use-postgres-message-queue
- https://mcfunley.com/choose-boring-technology
