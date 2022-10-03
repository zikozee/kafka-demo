package io.zikozee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.zikozee.domain.LibraryEvent;
import io.zikozee.producer.LibraryEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        // invoke Kafka producer
        log.info(">>>>>>>> before sendLibrary event");
        producer.sendLibraryEvent(libraryEvent);
        log.info(">>>>>>>> after sendLibrary event");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

}
