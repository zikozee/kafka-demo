# kafka-demo
Run in WSL2 
edit Configuration

### sending process:

- batch --- same as partition must be full before its sends to cluster
- if the above is not so, then the linger.ms (time it takes to forward to cluster)
- if the batch_record is not full

### Configuring
- Mandatory Values
- application.yml
```yaml
spring:
  profiles: local
  kafka:
    producer:
      bootstrap-servers: localhost:9092, localhost:9093, localhost:9094    #(Broker address)
      key-serializer: org.apache.kafka.common.serialization.IntegerSerializer
      value-serializer:  org.apache.kafka.common.serialization.String.Serializer
```

## Producer
- in producer package, LibraryEventProducer
- we use sendDefault, since we already hardcoded the topic to be used in the yaml property


### KAFKA ADMIN
- setting up admin for kafka is not recommended
- i.e spring.kafka.admin. ..

### USing embeddedKafka for Integration test
- @TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
- where **spring.kafka.producer.bootstrap-servers** is the property for the broker's host and port
- **spring.embedded.kafka.brokers** is that of the  embeddedKafka's host and port
- do shift shift and type **EnbeddedKafkaBroker** to see property

- also we override the admin.properties

- we can also wire in a consumer using KafkaUtils to do an assertion

### Ordering
- ordering always happens at partition level hence if you want ordering, send a unique key per request


## KAFKA PRODUCER CONFIGURATION - [Documentation](https://kafka.apache.org/documentation/#producerconfigs)
- acks
- possible values: 0, 1 and all(-1)
- acks = 1 -> guarantees message is written to leader (Default)
- acks = all -> guarantees message is written to a leader and to all replicas
- acks = 0 -> no guarantee (Not recommended)

- retries
- possible Integer values = [0 - 2147483647]
- in Spring Kafka, the default value is -> 2147483647 (max)

- retry.backoff.ms
- possible Integer value in millisecond (ms)
- Default value -> 100ms