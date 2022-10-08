package io.zikozee.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;
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
public class Book {

    @NotNull
    private Integer bookId;
    @NotBlank
    private String bookName;
    @NotBlank
    private String bookAuthor;
}
