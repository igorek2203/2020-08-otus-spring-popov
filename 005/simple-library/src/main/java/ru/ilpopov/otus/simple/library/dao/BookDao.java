package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Book;

@Validated
public interface BookDao {

    Book create(@NotNull Book book);

    Optional<Book> getById(long id);

    Book update(@NotNull Book book);

    void deleteById(long id);

    List<Book> findByTitle(@NotNull String title);

    List<Book> findByAuthorFullName(@NotNull String fullName);

    List<Book> findByGenreName(@NotNull String name);
}
