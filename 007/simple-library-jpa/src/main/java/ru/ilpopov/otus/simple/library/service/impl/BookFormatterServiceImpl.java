package ru.ilpopov.otus.simple.library.service.impl;

import com.google.common.base.Strings;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.dto.BookDto;
import ru.ilpopov.otus.simple.library.service.FormatterService;

@Service
public class BookFormatterServiceImpl implements FormatterService<BookDto> {

    private static final String BOOK_TEMPLATE = "ID: %s\r\n"
            + "Title: %s\r\n"
            + "Authors: %s\r\n"
            + "Genres: %s\r\n"
            + "Description: %s\r\n";

    public String formatToString(BookDto book) {
        String authors = book.getAuthors()
                .stream()
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
        String genres = book.getGenres()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));
        return String.format(BOOK_TEMPLATE, book.getId(), book.getTitle(), authors, genres,
                Strings.nullToEmpty(book.getDescription()));
    }

}
