package io.zikozee.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zikozee.domain.Book;
import io.zikozee.domain.LibraryEvent;
import io.zikozee.domain.LibraryEventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

/**
 * @author: Ezekiel Eromosei
 * @created: 08 October 2022
 */

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();  //mocking the behaviour of ObjectMappr rather than injecting

    @InjectMocks
    LibraryEventProducer libraryEventProducer;

    @Test
    void sendLibraryEvent_Approach2_failure() throws Exception {

        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventType(LibraryEventType.NEW)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();


        SettableListenableFuture future = new SettableListenableFuture();

        future.setException(new RuntimeException("Exception calling kafka"));
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
        //when
        assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEvent_Approach2(libraryEvent).get());

        //then
    }

    @Test
    void sendLibraryEvent_Approach2_success() throws Exception {

        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventType(LibraryEventType.NEW)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();


        String json = objectMapper.writeValueAsString(libraryEvent);
        SettableListenableFuture future = new SettableListenableFuture();

        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>("library-event",
                libraryEvent.getLibraryEventId(), json);

        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("library-event", 1), 1, 1, System.currentTimeMillis(), 1, 2);
        SendResult<Integer, String> sendResult = new SendResult<>(producerRecord, recordMetadata);

        future.set(sendResult);
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
        //when
        ListenableFuture<SendResult<Integer, String>> listenableFuture = libraryEventProducer.sendLibraryEvent_Approach2(libraryEvent);

        //then
        SendResult<Integer, String> sendResult1 = listenableFuture.get();
        assertEquals(1, sendResult1.getRecordMetadata().partition());
    }
}
