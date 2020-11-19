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
import java.util.Set;
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
import ru.ilpopov.otus.simple.library.exception.BookCreationException;
import ru.ilpopov.otus.simple.library.exception.BookModificationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;
import ru.ilpopov.otus.simple.library.service.AuthorService;
import ru.ilpopov.otus.simple.library.service.BookService;
import ru.ilpopov.otus.simple.library.service.CommentService;
import ru.ilpopov.otus.simple.library.service.GenreService;

@Transactional
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    @Override
    public BookDto create(BookDto bookDto) {
        resolveIds(bookDto);
        return new BookDto(bookDao.save(bookDto.toBook()));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> getById(long bookId) {
        return getById(bookId, false);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> getById(long bookId, boolean withComments) {
        Optional<BookDto> book = bookDao.findById(bookId)
                .map(BookDto::new);
        book.filter(b -> withComments)
                .ifPresent(b -> b.getComments().addAll(commentService.findByBookId(Set.of(b.getId()))));
        return book;
    }

    @Override
    public BookDto update(BookDto bookDto) {
        resolveIds(bookDto);
        return new BookDto(bookDao.save(bookDto.toBook()));
    }

    @Override
    public void deleteById(long bookId) {
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
        Set<Long> bookIds = books.stream().map(BookDto::getId).collect(Collectors.toSet());
        Map<Long, List<Comment>> bookCommentsMap = commentService.findByBookId(bookIds)
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
    public BookDto removeGenreFromBook(Long bookId, String genreName) {
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
    public BookDto addGenreToBook(Long bookId, String genreName) {
        List<Genre> genres = genreService.findByName(genreName);
        if (genres.isEmpty()) {
            throw new ObjectNotFound(String.format("The genre '%s' does not exists", genreName));
        }
        return bookDao.findById(bookId)
                .map(book -> {
                    book.getGenres().addAll(genres);
                    return book;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    @Override
    public BookDto removeAuthorFromBook(Long bookId, String authorName) {
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
    public BookDto addAuthorToBook(Long bookId, String authorName) {
        List<Author> authors = authorService.findByFullName(authorName);
        if (authors.isEmpty()) {
            throw new ObjectNotFound(String.format("The author '%s' does not exists", authorName));
        }
        return bookDao.findById(bookId)
                .map(book -> {
                    book.getAuthors().addAll(authors);
                    return book;
                })
                .map(bookDao::save)
                .map(BookDto::new)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    @Override
    public BookDto updateBookField(Long bookId, String fieldName, String fieldValue) {
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

    private void resolveIds(BookDto book) {
        Set<Author> authors = book.getAuthors()
                .stream()
                .map(a -> a.getId() != null ? a : findAuthorByName(a.getFullName()))
                .collect(Collectors.toSet());
        Set<Genre> genres = book.getGenres()
                .stream()
                .map(g -> g.getId() != null ? g : findGenreByName(g.getName()))
                .collect(Collectors.toSet());
        book.setAuthors(authors);
        book.setGenres(genres);
    }

    private Author findAuthorByName(String fullName) {
        return authorService.findByFullName(fullName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookCreationException(
                        String.format("The author '%s' does not exists", fullName)));
    }

    private Genre findGenreByName(String name) {
        return genreService.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookCreationException(
                        String.format("The genre '%s' does not exists", name)));
    }
}
