package io.zikozee.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: Ezekiel Eromosei
 * @created: 22 October 2022
 */

@Component
@Slf4j
public class LibraryEventConsumer {

    @KafkaListener(topics = {"library-events"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord){  // remember we were publishing ProducerRecord
        log.info("consumer record: {}", consumerRecord);
    }
}
