package io.zikozee.domain;

import lombok.*;

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
    private Book book;
}
