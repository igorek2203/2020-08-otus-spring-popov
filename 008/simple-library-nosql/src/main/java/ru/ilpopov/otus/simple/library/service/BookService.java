package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;
import ru.ilpopov.otus.simple.library.dto.BookDto;

@Validated
public interface BookService {

    @Validated(Create.class)
    BookDto create(@Valid @NotNull BookDto book);

    Optional<BookDto> getById(@NotNull String bookId);

    Optional<BookDto> getById(@NotNull String bookId, boolean withComments);

    @Validated(Update.class)
    BookDto update(@Valid @NotNull BookDto book);

    void deleteById(@NotNull String bookId);

    List<BookDto> findByTitle(@NotNull String title);

    List<BookDto> findByTitle(@NotNull String title, boolean withComments);

    List<BookDto> findByAuthorFullName(@NotNull String fullName);

    List<BookDto> findByAuthorFullName(@NotNull String fullName, boolean withComments);

    List<BookDto> findByGenreName(@NotNull String name);

    List<BookDto> findByGenreName(@NotNull String name, boolean withComments);

    BookDto removeGenreFromBook(@NotNull String bookId, @NotNull String genreName);

    BookDto addGenreToBook(@NotNull String bookId, @NotNull Genre genre);

    BookDto removeAuthorFromBook(@NotNull String bookId, @NotNull String authorName);

    BookDto addAuthorToBook(@NotNull String bookId, @Valid @NotNull Author author);

    List<BookDto> findAllByTitleAndAuthorFullNameAndGenreName(@NotNull String bookTitle,
            @NotNull String authorFullName, @NotNull String genreName);

    List<BookDto> findAllByTitleAndAuthorFullNameAndGenreName(@NotNull String bookTitle,
            @NotNull String authorFullName, @NotNull String genreName, boolean withComments);

    BookDto updateBookField(@NotNull String bookId, @NotNull String fieldName, String fieldValue);
}
