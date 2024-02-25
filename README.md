# CorgiMQ 
[![build](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml/badge.svg)](https://github.com/hailuand/corgio-mq/actions/workflows/maven.yaml) [![codecov](https://codecov.io/github/hailuand/corgimq/graph/badge.svg?token=NYQYU42L1U)](https://codecov.io/github/hailuand/corgimq)

![mascot.jpg](mascot.jpg)

A lightweight message queue library built using Java's JDBC API. Similar in spirit to [AWS SQS](https://aws.amazon.com/sqs/)
and [Redis Simple Message Queue](https://github.com/smrchy/rsmq), but built entirely on your DBMS.

## Features
- Lightweight: bring **just your DBMS.** 🚀
- Batteries included: sensible out-of-the-box defaults with a few optional knobs to get you dangerous _fast_. 🔋
- Transactional: shared access of JDBC `Connection` available to provide transactional message handling.  
- Guaranteed **exactly-once delivery** of a message to a reader.

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
- https://dagster.io/blog/skip-kafka-use-postgres-message-queue
- https://mcfunley.com/choose-boring-technology
