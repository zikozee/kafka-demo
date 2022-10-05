package io.zikozee.controller;

import io.zikozee.domain.Book;
import io.zikozee.domain.LibraryEvent;
import io.zikozee.domain.LibraryEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: Ezekiel Eromosei
 * @created: 05 October 2022
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryEventControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
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

    }
}
