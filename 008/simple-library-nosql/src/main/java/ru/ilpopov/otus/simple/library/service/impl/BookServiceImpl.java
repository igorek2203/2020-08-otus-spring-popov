package ru.ilpopov.otus.simple.library.service.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.dto.BookDto;
import ru.ilpopov.otus.simple.library.exception.BookModificationException;
import ru.ilpopov.otus.simple.library.service.BookService;
import ru.ilpopov.otus.simple.library.service.CommentService;

@Transactional
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final CommentService commentService;

    @Override
    public BookDto create(BookDto bookDto) {
        return new BookDto(bookDao.save(bookDto.toBook()));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> getById(String bookId) {
        return getById(bookId, false);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> getById(String bookId, boolean withComments) {
        Optional<BookDto> book = bookDao.findById(bookId)
                .map(BookDto::new);
        book.filter(b -> withComments)
                .ifPresent(b -> b.getComments().addAll(commentService.findByBookId(List.of(b.getId()))));
        return book;
    }

    @Override
    public BookDto update(BookDto bookDto) {
        return new BookDto(bookDao.save(bookDto.toBook()));
    }

    @Override
    public void deleteById(String bookId) {
        bookDao.deleteById(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findByTitle(String title) {
        return findByTitle(title, false);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findByTitle(@NotNull String title, boolean withComments) {
        List<BookDto> books = bookDao.findByTitleContaining(title)
                .stream()
                .map(BookDto::new)
                .collect(toList());
        if (withComments) {
            addComments(books);
        }
        return books;
    }

    private void addComments(List<BookDto> books) {
        List<String> bookIds = books.stream().map(BookDto::getId).collect(Collectors.toList());
        Map<String, List<Comment>> bookCommentsMap = commentService.findByBookId(bookIds)
                .stream()
                .collect(groupingBy(
                        c -> c.getBook().getId(),
                        mapping(Function.identity(), toList())));
        books.forEach(b ->
                b.getComments()
                        .addAll(bookCommentsMap.getOrDefault(b.getId(), Lists.newArrayList())));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findByAuthorFullName(@NotNull String fullName) {
        return findByAuthorFullName(fullName, false);
    }

    @Override
    public List<BookDto> findByAuthorFullName(@NotNull String fullName, boolean withComments) {
        List<BookDto> books = bookDao.findByAuthors_fullName(fullName)
                .stream()
                .map(BookDto::new)
                .collect(toList());
        if (withComments) {
            addComments(books);
        }
        return books;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findByGenreName(@NotNull String name) {
        return findByGenreName(name, false);
    }

    @Override
    public List<BookDto> findByGenreName(@NotNull String name, boolean withComments) {
        List<BookDto> books = bookDao.findByGenres_name(name)
                .stream()
                .map(BookDto::new)
                .collect(toList());
        if (withComments) {
            addComments(books);
        }
        return books;
    }

    @Override
    public BookDto removeGenreFromBook(String bookId, String genreName) {
        return bookDao.findById(bookId)
                .map(book -> {
                    book.getGenres().removeIf(a -> a.getName().equalsIgnoreCase(genreName));
                    return book;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    @Override
    public BookDto addGenreToBook(String bookId, Genre genre) {
        return bookDao.findById(bookId)
                .map(book -> {
                    book.getGenres().add(genre);
                    return book;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    @Override
    public BookDto removeAuthorFromBook(String bookId, String authorName) {
        return bookDao.findById(bookId)
                .map(book -> {
                    book.getAuthors().removeIf(a -> a.getFullName().equalsIgnoreCase(authorName));
                    return book;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    @Override
    public BookDto addAuthorToBook(String bookId, Author author) {
        return bookDao.findById(bookId)
                .map(book -> {
                    book.getAuthors().add(author);
                    return book;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    @Override
    public BookDto updateBookField(String bookId, String fieldName, String fieldValue) {
        return bookDao.findById(bookId)
                .map(b -> {
                    switch (fieldName) {
                        case "name":
                            b.setTitle(fieldValue);
                            break;
                        case "description":
                            b.setDescription(fieldValue);
                    }
                    return b;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("The book was not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAllByTitleAndAuthorFullNameAndGenreName(String bookTitle, String authorFullName,
            String genreName) {
        return findAllByTitleAndAuthorFullNameAndGenreName(bookTitle, authorFullName, genreName, false);
    }

    @Override
    public List<BookDto> findAllByTitleAndAuthorFullNameAndGenreName(@NotNull String bookTitle,
            @NotNull String authorFullName, @NotNull String genreName, boolean withComments) {
        List<BookDto> books = new ArrayList<>();
        if (!Strings.isNullOrEmpty(bookTitle)) {
            bookDao.findByTitleContaining(bookTitle)
                    .stream()
                    .map(BookDto::new)
                    .filter(b -> !books.contains(b))
                    .forEach(books::add);
        }
        if (!Strings.isNullOrEmpty(authorFullName)) {
            bookDao.findByAuthors_fullName(authorFullName)
                    .stream()
                    .map(BookDto::new)
                    .filter(b -> !books.contains(b))
                    .forEach(books::add);
        }
        if (!Strings.isNullOrEmpty(genreName)) {
            bookDao.findByGenres_name(genreName)
                    .stream()
                    .map(BookDto::new)
                    .filter(b -> !books.contains(b))
                    .forEach(books::add);
        }
        if (withComments) {
            addComments(books);
        }
        return books;
    }

}
