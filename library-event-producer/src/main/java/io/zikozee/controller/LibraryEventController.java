package io.zikozee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.zikozee.domain.LibraryEvent;
import io.zikozee.domain.LibraryEventType;
import io.zikozee.producer.LibraryEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author: Ezekiel Eromosei
 * @created: 03 October 2022
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class LibraryEventController {

    private final LibraryEventProducer producer;

    @PostMapping(path = "v1/libraryEvent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        // invoke Kafka producer
        log.info(">>>>>>>> before sendLibrary event");
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        producer.sendLibraryEvent(libraryEvent);
        log.info(">>>>>>>> after sendLibrary event");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping(path = "v1/libraryEvent/using-producer-record")  // using ProducerRecord
    public ResponseEntity<LibraryEvent> postLibraryEventApproach2(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        producer.sendLibraryEvent_Approach2(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping(path = "v1/libraryEvent/synchronous")
    public ResponseEntity<LibraryEvent> postLibraryEventSynchronous(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        // invoke Kafka producer
        log.info(">>>>>>>> before sendLibrary event");
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        SendResult<Integer, String> sendResult = producer.sendLibraryEventSynchronous(libraryEvent);
        log.info("SendResult is {}", sendResult.toString());
        log.info(">>>>>>>> after sendLibrary event");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping(path = "v1/libraryEvent")
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {

        if(libraryEvent.getLibraryEventId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the library EventId");
        }
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
        producer.sendLibraryEvent_Approach2(libraryEvent);
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }
}
