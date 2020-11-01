package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Book;

@Validated
public interface BookDao {

    Book create(@NotNull Book book);

    Optional<Book> get(long id);

    Book update(@NotNull Book book);

    void delete(long id);

    List<Book> findByName(@NotNull String name);

    List<Book> findByAuthorName(@NotNull String name);

    List<Book> findByGenreName(@NotNull String name);
}
