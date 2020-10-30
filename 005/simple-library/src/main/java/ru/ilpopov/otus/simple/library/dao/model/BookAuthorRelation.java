package ru.ilpopov.otus.simple.library.dao.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookAuthorRelation {

    @NotNull
    private Long id;

    @NotNull
    private final Long bookId;

    @NotNull
    private final Long authorId;
}
