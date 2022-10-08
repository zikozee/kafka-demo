package io.zikozee.domain;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author: Ezekiel Eromosei
 * @created: 03 October 2022
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryEvent {

    private Integer libraryEventId;
    private LibraryEventType libraryEventType;
    @NotNull
    @Valid
    private Book book;
}
