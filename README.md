# Corgi Message Queue (CorgiMQ)

[![build](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml/badge.svg)](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml) [![codecov](https://codecov.io/github/hailuand/corgimq/graph/badge.svg?token=NYQYU42L1U)](https://codecov.io/github/hailuand/corgimq) [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central Version](https://img.shields.io/maven-central/v/io.github.hailuand/corgimq?color=blue)](https://central.sonatype.com/artifact/io.github.hailuand/corgimq) [![javadoc](https://javadoc.io/badge2/io.github.hailuand/corgimq/javadoc.svg)](https://javadoc.io/doc/io.github.hailuand/corgimq)

![mascot.jpg](mascot.jpg)

A lightweight message queue library built using Java's JDBC API. Similar in spirit to [AWS SQS](https://aws.amazon.com/sqs/)
and [Redis Simple Message Queue](https://github.com/smrchy/rsmq), but entirely on top of your DBMS.

---

### Features
- Lightweight: bring **just your DBMS.** 🚀
- Batteries included: sensible out-of-the-box defaults with a few optional knobs to get you dangerous _fast_. 🔋
- Transactional: shared access of JDBC `Connection` available to provide transactional message handling. 🤝
- Auditable: audit information in the queue captures if, when, and who has read a message. 🔎
- Guaranteed **exactly-once delivery** of a message to a reader - if someone's currently reading it, no one else receives it.
- Messages remain in queue until removed.

---

### Index
* [DBMS compatability & testing](#dbms-compatability)
* [Get started](#get-started)
  * [Creating a queue](#creating-a-queue)
  * [Pushing messages](#pushing-messages)
  * [Reading messages](#reading-messages)
* [Configuration](#-configuration)
* [Notes](#-notes)
* [References](#references)

---

### DBMS compatability
RDBMS in this list have been tested for library compatability and are included in the test suites.

| DBMS        | Status |
|-------------|--------|
| H2          | ✅      |
| MySQL       | ✅      |
| Postgres    | ✅      |
| CockroachDB | ✅      |

---

### Get started
#### Creating a queue
A message queue is managed by an instance of `MessageQueue`.

```java
MessageQueue messageQueue = MessageQueue.of(MessageQueueConfig.of("poneglyphs")); // Name of queue, table will have '_q' suffix
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
A `Message` currently being read by a `MessageHandler` has its row locked until the function completes. If multiple
`MessageHandler`s are operating on the same queue, locked rows are skipped to prevent the same message being received
by different handlers.

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
