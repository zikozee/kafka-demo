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


### KAFKA ADMIN
- setting up admin for kafka is not recommended
- i.e spring.kafka.admin. ...