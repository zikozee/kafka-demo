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