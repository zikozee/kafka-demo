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
public class Book {

    private Integer bookId;
    private String bookName;
    private String bookAuthor;
}
