package io.zikozee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zikozee.domain.Book;
import io.zikozee.domain.LibraryEvent;
import io.zikozee.domain.LibraryEventType;
import io.zikozee.producer.LibraryEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author: Ezekiel Eromosei
 * @created: 08 October 2022
 */

@WebMvcTest(value = LibraryEventController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    LibraryEventProducer libraryEventProducer;


    @Test
    void postLibraryEvent() throws Exception{
        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventType(LibraryEventType.NEW)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));

        //when
        mockMvc.perform(post("/v1/libraryEvent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //then
    }

    @Test
    void updateLibraryEvent() throws Exception{
        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(999)
                .libraryEventType(LibraryEventType.UPDATE)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));

        //when
        mockMvc.perform(put("/v1/libraryEvent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
    }

    @DisplayName("update library event with null library event id")
    @Test
    void updateLibraryEvent_withNullLibraryEventId() throws Exception{
        //given
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.UPDATE)
                .book(Book.builder()
                        .bookId(123)
                        .bookAuthor("Ziko")
                        .bookName("kafka using springboot").build())
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));

        //when
        mockMvc.perform(put("/v1/libraryEvent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //then
    }


    @Test
    void postLibraryEvent_4xx() throws Exception{
        //given
        Book book = Book.builder()
                .bookId(null)
                .bookAuthor(null)
                .bookName("kafka using springboot").build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventType(LibraryEventType.NEW)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));

        //when
        String errorMessage= "book.bookAuthor - must not be blank, book.bookId - must not be null";
        mockMvc.perform(post("/v1/libraryEvent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(errorMessage));

        //then
    }
}
