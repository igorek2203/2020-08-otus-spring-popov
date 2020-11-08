package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface BookService {

    @Validated(Insert.class)
    Book create(@Valid @NotNull Book book);

    Optional<Book> get(long id);

    @Validated(Update.class)
    Book update(@Valid @NotNull Book book);

    void delete(long id);

    List<Book> findByTitle(@NotNull String title);

    List<Book> findByAuthorFullName(@NotNull String fullName);

    List<Book> findByGenreName(@NotNull String name);
}
