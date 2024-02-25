# Corgi Message Queue (CorgiMQ) 
[![build](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml/badge.svg)](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml) [![codecov](https://codecov.io/github/hailuand/corgimq/graph/badge.svg?token=NYQYU42L1U)](https://codecov.io/github/hailuand/corgimq) [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

![mascot.jpg](mascot.jpg)

A lightweight message queue library built using Java's JDBC API. Similar in spirit to [AWS SQS](https://aws.amazon.com/sqs/)
and [Redis Simple Message Queue](https://github.com/smrchy/rsmq), but entirely on top of your DBMS.

## Features
- Lightweight: bring **just your DBMS.** 🚀
- Batteries included: sensible out-of-the-box defaults with a few optional knobs to get you dangerous _fast_. 🔋
- Transactional: shared access of JDBC `Connection` available to provide transactional message handling. 🤝
- Auditable: audit information in the queue captures if, when, and who read a message. 🔎
- Guaranteed **exactly-once delivery** of a message to a reader - if someone's currently reading it, no one else receives it.
- Messages remain in queue until removed.

Complexity will find you! Until then, don't go looking for it 🐶   

## DBMS Compatability & Testing
| DBMS     | Status |
|----------|--------|
| H2       | ✅      |
| MySQL    | ✅      |
| Postgres | ✅      |

## SDK Examples
### Creating a queue

```java
// todo
```

### Pushing messages
```java
// todo
```

### Reading messages
```java
// todo
```

## References
- https://adriano.fyi/posts/2023-09-24-choose-postgres-queue-technology/
- https://dagster.io/blog/skip-kafka-use-postgres-message-queue
- https://mcfunley.com/choose-boring-technology
