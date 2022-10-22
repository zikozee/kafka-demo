package io.zikozee.controller;

import io.zikozee.domain.Book;
import io.zikozee.domain.LibraryEvent;
import io.zikozee.domain.LibraryEventType;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: Ezekiel Eromosei
 * @created: 05 October 2022
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {LibraryEventControllerIT.TOPIC}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventControllerIT {

    public static final String TOPIC = "library-events";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<Integer, String> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    @Timeout(5)
    void postLibraryEvent() {

        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventType(LibraryEventType.NEW)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<LibraryEvent> httpEntity = new HttpEntity<>(libraryEvent, httpHeaders);

        // when
        ResponseEntity<LibraryEvent> exchange = restTemplate.exchange("/v1/libraryEvent", HttpMethod.POST, httpEntity, LibraryEvent.class);

        //then
        assertEquals(HttpStatus.CREATED, exchange.getStatusCode());

        ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, LibraryEventControllerIT.TOPIC);
        String value = consumerRecord.value();
        String expectedValue = "{\"libraryEventId\":null,\"libraryEventType\":\"NEW\",\"book\":{\"bookId\":123,\"bookName\":\"kafka using springboot\",\"bookAuthor\":\"Ziko\"}}";
        assertEquals(expectedValue, value);
    }

    @Test
    @Timeout(5)
    void updateLibraryEvent() {

        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(999)
                .libraryEventType(LibraryEventType.UPDATE)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<LibraryEvent> httpEntity = new HttpEntity<>(libraryEvent, httpHeaders);

        // when
        ResponseEntity<LibraryEvent> exchange = restTemplate.exchange("/v1/libraryEvent", HttpMethod.PUT, httpEntity, LibraryEvent.class);

        //then
        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, LibraryEventControllerIT.TOPIC);
        String value = consumerRecord.value();
        String expectedValue = "{\"libraryEventId\":999,\"libraryEventType\":\"UPDATE\",\"book\":{\"bookId\":123,\"bookName\":\"kafka using springboot\",\"bookAuthor\":\"Ziko\"}}";
        assertEquals(expectedValue, value);
    }
}
