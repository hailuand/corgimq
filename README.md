# Corgi Message Queue (CorgiMQ)

[![build](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml/badge.svg)](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml) [![codecov](https://codecov.io/github/hailuand/corgimq/graph/badge.svg?token=NYQYU42L1U)](https://codecov.io/github/hailuand/corgimq) [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

![mascot.jpg](mascot.jpg)

A lightweight message queue library built using Java's JDBC API. Similar in spirit to [AWS SQS](https://aws.amazon.com/sqs/)
and [Redis Simple Message Queue](https://github.com/smrchy/rsmq), but entirely on top of your DBMS.

---

### Features
- Lightweight: bring **just your DBMS.** 🚀
- Batteries included: sensible out-of-the-box defaults with a few optional knobs to get you dangerous _fast_. 🔋
- Transactional: shared access of JDBC `Connection` available to provide transactional message handling. 🤝
- Auditable: audit information in the queue captures if, when, and who read a message. 🔎
- Guaranteed **exactly-once delivery** of a message to a reader - if someone's currently reading it, no one else receives it.
- Messages remain in queue until removed.

Complexity will find you - until then, don't go looking for it 🐶   

---

### Index
* [DBMS compatability & testing](#dbms-compatability--testing)
* [Get started](#get-started)
  * [Creating a queue](#creating-a-queue)
  * [Pushing messages](#pushing-messages)
  * [Reading messages](#reading-messages)
* [Configuration](#configuration)
* [References](#references)

---

### DBMS compatability & testing

| DBMS     | Status |
|----------|--------|
| H2       | ✅      |
| MySQL    | ✅      |
| Postgres | ✅      |

---

### Get started
#### Creating a queue
A message queue is managed by an instance of `MessageQueue`:  

```java
DatabaseConfig databaseConfig = DatabaseConfig.of("jdbc:postgresql://...", "username", "password");
MessageQueueConfig messageQueueConfig = MessageQueueConfig.builder()
        .schemaName("fruits")
        .queueName("acquired")
        .build();
MessageQueue messageQueue = MessageQueue.of(databaseConfig, messageQueueConfig);
messageQueue.initialize();
```

This creates table `acquired_q` in the `fruits` schema - the given schema will be created if it does not already exist.

```
postgres=# \dt fruits.*
           List of relations
 Schema |    Name    | Type  |  Owner
--------+------------+-------+----------
 fruits | acquired_q | table | postgres
(1 row)
```

#### Pushing messages
Once a `MessageQueue` is created, messages can be pushed with the `push(List)` method. 

```java
Message message1 = Message.of(faker.onePiece().akumasNoMi());
Message message2 = Message.of(faker.onePiece().akumasNoMi());
messageQueue.push(List.of(message1, message2));
```

```
postgres=# select * from fruits.acquired_q;
                  id                  |      data       |        message_time        | read_count | read_by | processing_time
--------------------------------------+-----------------+----------------------------+------------+---------+-----------------
 9245867e-f7f1-40e4-9142-bb1457aff9ec | Nagi Nagi no Mi | 2024-02-27 10:12:57.486346 |          0 |         |
 a174f9d1-d3a9-4583-9396-d3ed575a4ebf | Mero Mero no Mi | 2024-02-27 10:12:57.486346 |          0 |         |
(2 rows)
```

#### Reading messages
Messages in the queue read in ascending `message_time` order. To read, we use an intsance of `MessageHandler`.
```java
MessageHandlerConfig messageHandlerConfig = MessageHandlerConfig.of(1);
MessageHandler messageHandler = MessageHandler.of(messageQueue, messageHandlerConfig);
messageHandler.listen(messageBatch -> {
    for(var message : messageBatch.messages()) {
        System.out.printf("Shanks - we found the %s!%n", message.data());
    }
    return messageBatch.messages();
});
// Prints out: "Shanks - we found the Nagi Nagi no Mi!"
```

`MessageHandler` reads unread messages i.e. messages without a `processing_time`

The above snippet's execution results in the following:
```
postgres=# select * from fruits.acquired_q;
                  id                  |      data       |        message_time        | read_count | read_by  |      processing_time
--------------------------------------+-----------------+----------------------------+------------+----------+----------------------------
 a174f9d1-d3a9-4583-9396-d3ed575a4ebf | Mero Mero no Mi | 2024-02-27 10:12:57.486346 |          0 |          |
 9245867e-f7f1-40e4-9142-bb1457aff9ec | Nagi Nagi no Mi | 2024-02-27 10:12:57.486346 |          1 | postgres | 2024-02-27 10:14:29.911197
(2 rows)
```
Note that the `read_count`, `read_by`, and `processing_time` have been updated for the message `9245867e-f7f1-40e4-9142-bb1457aff9ec` 
corresponding to `Nagi Nagi no Mi`.

Subsequent `listen()` invocations no longer receive this message as `processing_time` is set.

---

### Configuration
_TODO_

---

### References
- https://adriano.fyi/posts/2023-09-24-choose-postgres-queue-technology/
- https://dagster.io/blog/skip-kafka-use-postgres-message-queue
- https://mcfunley.com/choose-boring-technology
