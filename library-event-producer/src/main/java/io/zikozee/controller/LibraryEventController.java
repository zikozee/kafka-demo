package io.zikozee.controller;

import io.zikozee.domain.LibraryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Ezekiel Eromosei
 * @created: 03 October 2022
 */

@RestController
@RequiredArgsConstructor
public class LibraryEventController {

    @PostMapping(path = "v1/libraryEvent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent){
        // invoke Kafka producer

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

}
