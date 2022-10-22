## Kafka Consumer [Documentation](https://docs.spring.io/spring-kafka/reference/html/#receiving-messages)
- MessageListenerContainer  :  an interface
  - KafkaMessageListenerContainer
    - implements the MessageListenerContainer
    - Polls the records
    - Commits the offsets
  - ConcurrentMessageListenerContainer
    - multiple of **KafkaMessageListenerContainer**

  - @KafkaListener Annotation
    - Uses ConcurrentMessageListenerContainer behind the scenes
    - easiest way to build kafka consumer
    - ```java
        @KafkaListener(topics={"${spring.kafka.topic}"})
        public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
          log.info("OnMessage Record: {}", consumer record);
        }
        
        @Configuration
        @EnableKafka
        @Slf4j
        public class LibraryEventsConsumerConfig {
        }
      ```
    - properties
      - bootstrap-servers: localhost:9092,localhost:9093,localhost:9094
      - key-serializer: org.apache.kafka.common.serialization.IntegerDeserializer  ## note its Deserializer
      - value-serializer: org.apache.kafka.common.serialization.StringDeserializer   ## note its Deserializer
      - group-id: library-events-listener-group


- look out for <group-id>: partition assigned: [bootstrap-servers]
  - e.g library-events-listener-group: partitions assigned: [library-events-1, library-events-2, library-events-0] 
  - this indicates it was started successfully connecting to the brokers