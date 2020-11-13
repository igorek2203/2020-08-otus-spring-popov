package ru.ilpopov.otus.simple.library.dao.impl;

import com.google.common.base.Strings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;

@RequiredArgsConstructor
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcTemplate jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public Book create(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("INSERT INTO BOOKS(TITLE, DESCRIPTION) VALUES(:name, :desc)",
                new MapSqlParameterSource()
                        .addValue("name", book.getTitle())
                        .addValue("desc", book.getDescription()),
                keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey());
        book.setId(id.longValue());
        createRelations(book);
        return getById(id.longValue())
                .orElseThrow(
                        () -> new BookCreationException(
                                String.format("The book with id '%s' was not created", id)));
    }

    @Override
    public Optional<Book> getById(long id) {
        return getBooksByListId(Set.of(id)).stream()
                .findFirst();
    }

    @Override
    public Book update(Book book) {
        Book oldBook = getById(book.getId()).orElseThrow(() -> new ObjectNotFound("The book does not exists"));
        jdbc.update("UPDATE BOOKS SET TITLE = :name, DESCRIPTION = :desc WHERE ID = :id",
                Map.of("id", book.getId(),
                        "name", book.getTitle(),
                        "desc", Strings.nullToEmpty(book.getDescription())));
        updateRelations(oldBook, book);
        return getById(book.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The book with id '%s' was found", book.getId())));
    }

    private void updateRelations(Book oldBook, Book book) {
        updateBookAuthorsRelations(oldBook, book);
        updateBookGenresRelations(oldBook, book);
    }

    private void updateBookAuthorsRelations(Book oldBook, Book book) {
        List<Long> authorIds = book.getAuthors().stream()
                .map(this::resolveAuthorId)
                .collect(Collectors.toList());
        List<Long> oldAuthorIds = oldBook.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toList());
        List<Long> detachAuthorIds = oldAuthorIds.stream()
                .filter(id -> !authorIds.contains(id))
                .collect(Collectors.toList());
        detachAuthors(oldBook.getId(), detachAuthorIds);
        Set<Long> newAuthorIds = authorIds.stream()
                .filter(id -> !oldAuthorIds.contains(id))
                .collect(Collectors.toSet());
        relateAuthor(Map.of(book.getId(), newAuthorIds));
    }

    private void updateBookGenresRelations(Book oldBook, Book book) {
        List<Long> genreIds = book.getGenres().stream()
                .map(this::resolveGenreId)
                .collect(Collectors.toList());
        List<Long> oldGenreIds = oldBook.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        List<Long> detachGenreIds = oldGenreIds.stream()
                .filter(id -> !genreIds.contains(id))
                .collect(Collectors.toList());
        detachGenres(oldBook.getId(), detachGenreIds);
        Set<Long> newGenreIds = genreIds.stream()
                .filter(id -> !oldGenreIds.contains(id))
                .collect(Collectors.toSet());
        relateGenre(Map.of(book.getId(), newGenreIds));
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("DELETE BOOKS WHERE ID = :id", Map.of("id", id));
    }

    private void detachAuthors(long bookId, List<Long> authorIds) {
        jdbc.update("DELETE BOOKS_AUTHORS_RELATIONS WHERE BOOK_ID = :bookId AND AUTHOR_ID IN (:authorIds)",
                Map.of("bookId", bookId, "authorIds", authorIds));
    }

    private void detachGenres(long bookId, List<Long> genreIds) {
        jdbc.update("DELETE BOOKS_GENRES_RELATIONS WHERE BOOK_ID = :bookId AND GENRE_ID IN (:genreIds)",
                Map.of("bookId", bookId, "genreIds", genreIds));
    }

    @Override
    public List<Book> findByTitle(String title) {
        return findBooksByName(title);
    }

    private void createRelations(Book book) {
        createBookAuthorsRelations(book);
        createBookGenresRelations(book);
    }

    private void createBookAuthorsRelations(Book book) {
        Set<Long> authorIds = book.getAuthors()
                .stream()
                .map(this::resolveAuthorId)
                .collect(Collectors.toSet());
        relateAuthor(Map.of(book.getId(), authorIds));
    }

    private Long resolveAuthorId(Author a) {
        return Optional.ofNullable(a.getId())
                .orElseGet(() -> authorDao.findByFullName(a.getFullName())
                        .stream()
                        .mapToLong(Author::getId)
                        .findFirst()
                        .orElseThrow(() -> new BookCreationException(
                                String.format("The author '%s' must be existed before the book", a.getFullName()))));
    }

    private void createBookGenresRelations(Book book) {
        Set<Long> genreIds = book.getGenres()
                .stream()
                .map(this::resolveGenreId)
                .collect(Collectors.toSet());
        relateGenre(Map.of(book.getId(), genreIds));
    }

    private Long resolveGenreId(Genre g) {
        return Optional.ofNullable(g.getId())
                .orElseGet(() -> genreDao.findByName(g.getName())
                        .stream()
                        .mapToLong(Genre::getId)
                        .findFirst()
                        .orElseThrow(() -> new BookCreationException(
                                String.format("The genre '%s' must be existed before the book", g.getName()))));
    }

    private void relateAuthor(Map<Long, Set<Long>> bookAuthorsMap) {
        MapSqlParameterSource[] params = bookAuthorsMap.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .stream()
                        .map(authorId ->
                                new MapSqlParameterSource()
                                        .addValue("bookId", e.getKey())
                                        .addValue("authorId", authorId)))
                .toArray(MapSqlParameterSource[]::new);
        jdbc.batchUpdate("INSERT INTO BOOKS_AUTHORS_RELATIONS(BOOK_ID, AUTHOR_ID) VALUES(:bookId, :authorId)", params);
    }

    private void relateGenre(Map<Long, Set<Long>> bookGenresMap) {
        MapSqlParameterSource[] params = bookGenresMap.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .stream()
                        .map(genreId ->
                                new MapSqlParameterSource()
                                        .addValue("bookId", e.getKey())
                                        .addValue("genreId", genreId)))
                .toArray(MapSqlParameterSource[]::new);
        jdbc.batchUpdate("INSERT INTO BOOKS_GENRES_RELATIONS(BOOK_ID, GENRE_ID) VALUES(:bookId, :genreId)", params);
    }

    @Override
    public List<Book> findByAuthorFullName(@NotNull String name) {
        Set<Long> bookIds = new HashSet<>(jdbc.queryForList("SELECT bar.BOOK_ID \n"
                        + "FROM AUTHORS a \n"
                        + "JOIN BOOKS_AUTHORS_RELATIONS bar ON bar.AUTHOR_ID = a.ID \n"
                        + "WHERE a.FULL_NAME = :name",
                new MapSqlParameterSource().addValue("name", name),
                Long.class));
        return getBooksByListId(bookIds);
    }

    @Override
    public List<Book> findByGenreName(@NotNull String name) {
        Set<Long> bookIds = new HashSet<>(jdbc.queryForList("SELECT bgr.BOOK_ID \n"
                        + "FROM GENRES g \n"
                        + "JOIN BOOKS_GENRES_RELATIONS bgr ON bgr.GENRE_ID = g.ID \n"
                        + "WHERE g.NAME = :name",
                new MapSqlParameterSource().addValue("name", name),
                Long.class));
        return getBooksByListId(bookIds);
    }

    private List<Book> findBooksByName(String name) {
        Set<Long> bookIds = new HashSet<>(jdbc.queryForList("SELECT b.ID BOOK_ID \r\n"
                        + "FROM BOOKS b \r\n"
                        + "WHERE b.TITLE = :name",
                new MapSqlParameterSource().addValue("name", name),
                Long.class));
        return getBooksByListId(bookIds);
    }

    private List<Book> getBooksByListId(Set<Long> ids) {
        List<Book> books = jdbc.query("SELECT b.ID BOOK_ID, b.TITLE BOOK_TITLE, b.DESCRIPTION BOOK_DESC \r\n"
                        + "FROM BOOKS b \r\n"
                        + "WHERE b.ID in (:ids)",
                new MapSqlParameterSource().addValue("ids", ids),
                new BookRowMapper());
        Map<Long, Set<Author>> bookAuthorsMap = getBooksAuthorsMap(ids);
        Map<Long, Set<Genre>> bookGenresMap = getBooksGenresMap(ids);
        return books
                .parallelStream()
                .peek(b -> {
                    b.getAuthors()
                            .addAll(bookAuthorsMap.getOrDefault(b.getId(), Set.of()));
                    b.getGenres()
                            .addAll(bookGenresMap.getOrDefault(b.getId(), Set.of()));
                }).collect(Collectors.toList());
    }

    private Map<Long, Set<Author>> getBooksAuthorsMap(Set<Long> bookIds) {
        return jdbc.query("SELECT bar.BOOK_ID, a.ID AUTHOR_ID, \r\n"
                        + "a.FULL_NAME AUTHOR_FULL_NAME, a.DESCRIPTION AUTHOR_DESC \r\n"
                        + "FROM BOOKS_AUTHORS_RELATIONS bar \r\n"
                        + "    JOIN AUTHORS a ON bar.AUTHOR_ID = a.ID \r\n"
                        + "WHERE bar.BOOK_ID in (:bookIds)",
                Map.of("bookIds", bookIds),
                (rs, rowNum) -> Map.of(
                        new Author(rs.getLong("AUTHOR_ID"), rs.getString("AUTHOR_FULL_NAME"),
                                rs.getString("AUTHOR_DESC")),
                        rs.getLong("BOOK_ID")))
                .stream()
                .flatMap(e -> e.entrySet().stream())
                .collect(Collectors.groupingBy(Entry::getValue,
                        Collectors.mapping(Entry::getKey, Collectors.toSet())));
    }

    private Map<Long, Set<Genre>> getBooksGenresMap(Set<Long> bookIds) {
        return jdbc.query("SELECT bgr.BOOK_ID, g.ID GENRE_ID, g.NAME GENRE_NAME \r\n"
                        + "FROM BOOKS_GENRES_RELATIONS bgr \r\n"
                        + "    JOIN GENRES g ON bgr.GENRE_ID = g.ID \r\n"
                        + "WHERE bgr.BOOK_ID in (:bookIds)",
                Map.of("bookIds", bookIds),
                (rs, rowNum) -> Map.of(
                        new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME")),
                        rs.getLong("BOOK_ID")))
                .stream()
                .flatMap(e -> e.entrySet().stream())
                .collect(Collectors.groupingBy(Entry::getValue,
                        Collectors.mapping(Entry::getKey, Collectors.toSet())));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rec, int rowNum) throws SQLException {
            return new Book(rec.getLong("BOOK_ID"), rec.getString("BOOK_TITLE"), rec.getString("BOOK_DESC"));
        }
    }
}
