package ru.ilpopov.otus.simple.library.dao.impl;

import com.google.common.base.Strings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        jdbc.update("INSERT INTO BOOKS(NAME, DESCRIPTION) VALUES(:name, :desc)",
                new MapSqlParameterSource()
                        .addValue("name", book.getName())
                        .addValue("desc", book.getDescription()),
                keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey());
        book.setId(id.longValue());
        createRelations(book);
        return get(id.longValue())
                .orElseThrow(
                        () -> new BookCreationException(
                                String.format("The book with id '%s' was not created", id)));
    }

    @Override
    public Optional<Book> get(long id) {
        return groupAuthorsAndGenresByBook(findOneToOneBooksByListId(List.of(id))).stream()
                .findFirst();
    }

    @Override
    public Book update(Book book) {
        Book oldBook = get(book.getId()).orElseThrow(() -> new ObjectNotFound("The book does not exists"));
        jdbc.update("UPDATE BOOKS SET NAME = :name, DESCRIPTION = :desc WHERE ID = :id",
                Map.of("id", book.getId(),
                        "name", book.getName(),
                        "desc", Strings.nullToEmpty(book.getDescription())));
        updateRelations(oldBook, book);
        return get(book.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The book with id '%s' was found", book.getId())));
    }

    private void updateRelations(Book oldBook, Book book) {
        updateBookAuthorsRelations(oldBook, book);
        updateBookGenresRelations(oldBook, book);
    }

    private void updateBookAuthorsRelations(Book oldBook, Book book) {
        List<Long> newAuthorIds = book.getAuthors().stream()
                .map(this::resolveAuthorId)
                .collect(Collectors.toList());
        List<Long> oldAuthorIds = oldBook.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toList());
        List<Long> detachAuthorIds = oldAuthorIds.stream()
                .filter(id -> !newAuthorIds.contains(id))
                .collect(Collectors.toList());
        detachAuthors(oldBook.getId(), detachAuthorIds);
        newAuthorIds.stream()
                .filter(id -> !oldAuthorIds.contains(id))
                .forEach(id -> relateAuthor(book.getId(), id));
    }

    private void updateBookGenresRelations(Book oldBook, Book book) {
        List<Long> newGenreIds = book.getGenres().stream()
                .map(this::resolveGenreId)
                .collect(Collectors.toList());
        List<Long> oldGenreIds = oldBook.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        List<Long> detachGenreIds = oldGenreIds.stream()
                .filter(id -> !newGenreIds.contains(id))
                .collect(Collectors.toList());
        detachGenres(oldBook.getId(), detachGenreIds);
        newGenreIds.stream()
                .filter(id -> !oldGenreIds.contains(id))
                .forEach(id -> relateGenre(book.getId(), id));
    }

    @Override
    public void delete(long id) {
        get(id).ifPresent(b -> {
            detachAuthors(b);
            detachGenres(b);
            jdbc.update("DELETE BOOKS WHERE ID = :id", Map.of("id", b.getId()));
        });
    }

    private void detachAuthors(Book book) {
        List<Long> authorIds = book.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toList());
        detachAuthors(book.getId(), authorIds);
    }

    private void detachAuthors(long bookId, List<Long> authorIds) {
        jdbc.update("DELETE BOOKS_AUTHORS_RELATIONS WHERE BOOK_ID = :bookId AND AUTHOR_ID IN (:authorIds)",
                Map.of("bookId", bookId, "authorIds", authorIds));
    }

    private void detachGenres(Book book) {
        List<Long> genreIds = book.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        detachGenres(book.getId(), genreIds);
    }

    private void detachGenres(long bookId, List<Long> genreIds) {
        jdbc.update("DELETE BOOKS_GENRES_RELATIONS WHERE BOOK_ID = :bookId AND GENRE_ID IN (:genreIds)",
                Map.of("bookId", bookId, "genreIds", genreIds));
    }

    @Override
    public List<Book> findByName(String name) {
        return groupAuthorsAndGenresByBook(findOneToOneBooksByName(name));
    }

    private void createRelations(Book book) {
        createBookAuthorsRelations(book);
        createBookGenresRelations(book);
    }

    private void createBookAuthorsRelations(Book book) {
        book.getAuthors()
                .stream()
                .map(this::resolveAuthorId)
                .forEach(authorId -> relateAuthor(book.getId(), authorId));
    }

    private Long resolveAuthorId(Author a) {
        return Optional.ofNullable(a.getId())
                .orElseGet(() -> authorDao.findByName(a.getName())
                        .stream()
                        .mapToLong(Author::getId)
                        .findFirst()
                        .orElseThrow(() -> new BookCreationException(
                                String.format("The author '%s' must be existed before the book", a.getName()))));
    }

    private void createBookGenresRelations(Book book) {
        book.getGenres()
                .stream()
                .map(this::resolveGenreId)
                .forEach(genreId -> relateGenre(book.getId(), genreId));
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

    private void relateAuthor(Long bookId, Long authorId) {
        jdbc.update(
                "INSERT INTO BOOKS_AUTHORS_RELATIONS(BOOK_ID, AUTHOR_ID) VALUES(:bookId, :authorId)",
                new MapSqlParameterSource()
                        .addValue("bookId", bookId)
                        .addValue("authorId", authorId));
    }

    private void relateGenre(Long bookId, Long genreId) {
        jdbc.update(
                "INSERT INTO BOOKS_GENRES_RELATIONS(BOOK_ID, GENRE_ID) VALUES(:bookId, :genreId)",
                new MapSqlParameterSource()
                        .addValue("bookId", bookId)
                        .addValue("genreId", genreId));
    }

    @Override
    public List<Book> findByAuthorName(@NotNull String name) {
        List<Long> bookIdList = jdbc.queryForList("SELECT bar.BOOK_ID \n"
                        + "FROM AUTHORS a \n"
                        + "JOIN BOOKS_AUTHORS_RELATIONS bar ON bar.AUTHOR_ID = a.ID \n"
                        + "WHERE a.NAME = :name",
                new MapSqlParameterSource().addValue("name", name),
                Long.class);
        return groupAuthorsAndGenresByBook(findOneToOneBooksByListId(bookIdList));
    }

    @Override
    public List<Book> findByGenreName(@NotNull String name) {
        List<Long> bookIdList = jdbc.queryForList("SELECT bgr.BOOK_ID \n"
                        + "FROM GENRES g \n"
                        + "JOIN BOOKS_GENRES_RELATIONS bgr ON bgr.GENRE_ID = g.ID \n"
                        + "WHERE g.NAME = :name",
                new MapSqlParameterSource().addValue("name", name),
                Long.class);
        return groupAuthorsAndGenresByBook(findOneToOneBooksByListId(bookIdList));
    }

    private List<Book> findOneToOneBooksByName(String name) {
        return jdbc.query("SELECT b.ID BOOK_ID, b.NAME BOOK_NAME, b.DESCRIPTION BOOK_DESC "
                        + ", a.ID AUTHOR_ID, a.NAME AUTHOR_NAME, a.DESCRIPTION AUTHOR_DESC"
                        + ", g.ID GENRE_ID, g.NAME GENRE_NAME, g.DESCRIPTION GENRE_DESC \r\n"
                        + "FROM BOOKS b \r\n"
                        + "    LEFT JOIN BOOKS_AUTHORS_RELATIONS bar ON bar.BOOK_ID = b.ID \r\n"
                        + "    LEFT JOIN AUTHORS a ON bar.AUTHOR_ID = a.ID \r\n"
                        + "    LEFT JOIN BOOKS_GENRES_RELATIONS bgr ON bgr.BOOK_ID = b.ID \r\n"
                        + "    LEFT JOIN GENRES g ON bgr.GENRE_ID = g.ID \r\n"
                        + "WHERE b.NAME = :name",
                new MapSqlParameterSource().addValue("name", name),
                new BookRowMapper());
    }

    private List<Book> findOneToOneBooksByListId(List<Long> idList) {
        return jdbc.query("SELECT b.ID BOOK_ID, b.NAME BOOK_NAME, b.DESCRIPTION BOOK_DESC "
                        + ", a.ID AUTHOR_ID, a.NAME AUTHOR_NAME, a.DESCRIPTION AUTHOR_DESC"
                        + ", g.ID GENRE_ID, g.NAME GENRE_NAME, g.DESCRIPTION GENRE_DESC \r\n"
                        + "FROM BOOKS b \r\n"
                        + "    LEFT JOIN BOOKS_AUTHORS_RELATIONS bar ON bar.BOOK_ID = b.ID \r\n"
                        + "    LEFT JOIN AUTHORS a ON bar.AUTHOR_ID = a.ID \r\n"
                        + "    LEFT JOIN BOOKS_GENRES_RELATIONS bgr ON bgr.BOOK_ID = b.ID \r\n"
                        + "    LEFT JOIN GENRES g ON bgr.GENRE_ID = g.ID \r\n"
                        + "WHERE b.ID in (:ids)",
                new MapSqlParameterSource().addValue("ids", idList),
                new BookRowMapper());
    }

    private List<Book> groupAuthorsAndGenresByBook(List<Book> flatBookList) {
        Map<Long, List<Author>> authors = groupAuthorsByBook(flatBookList);
        Map<Long, List<Genre>> genres = groupGenresByBook(flatBookList);
        return flatBookList.stream()
                .distinct()
                .peek(b -> {
                    if (authors.get(b.getId()) != null) {
                        b.setAuthors(authors.get(b.getId()));
                    } else {
                        b.setAuthors(List.of());
                    }
                    if (genres.get(b.getId()) != null) {
                        b.setGenres(genres.get(b.getId()));
                    } else {
                        b.setGenres(List.of());
                    }
                }).collect(Collectors.toList());
    }

    private static Map<Long, List<Author>> groupAuthorsByBook(List<Book> flatBook) {
        return flatBook.stream()
                .filter(b -> !b.getAuthors().isEmpty())
                .collect(Collectors.groupingBy(Book::getId,
                        Collectors.flatMapping(book -> book.getAuthors().stream(),
                                Collectors.collectingAndThen(Collectors.toList(),
                                        l -> l.stream().distinct()
                                                .collect(Collectors.toList())))));
    }

    private static Map<Long, List<Genre>> groupGenresByBook(List<Book> flatBook) {
        return flatBook.stream()
                .filter(b -> !b.getGenres().isEmpty())
                .collect(Collectors.groupingBy(Book::getId,
                        Collectors.flatMapping(b -> b.getGenres().stream(),
                                Collectors.collectingAndThen(Collectors.toList(),
                                        l -> l.stream().distinct()
                                                .collect(Collectors.toList())))));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rec, int rowNum) throws SQLException {
            Book book = new Book(rec.getLong("BOOK_ID"),
                    rec.getString("BOOK_NAME"),
                    rec.getString("BOOK_DESC"));
            if (rec.getLong("AUTHOR_ID") != 0 && rec.getString("AUTHOR_NAME") != null) {
                book.getAuthors()
                        .add(new Author(rec.getLong("AUTHOR_ID"),
                                rec.getString("AUTHOR_NAME"),
                                rec.getString("AUTHOR_DESC")));
            }
            if (rec.getLong("GENRE_ID") != 0 && rec.getString("GENRE_NAME") != null) {
                book.getGenres()
                        .add(new Genre(rec.getLong("GENRE_ID"),
                                rec.getString("GENRE_NAME"),
                                rec.getString("GENRE_DESC")));
            }
            return book;
        }
    }
}
