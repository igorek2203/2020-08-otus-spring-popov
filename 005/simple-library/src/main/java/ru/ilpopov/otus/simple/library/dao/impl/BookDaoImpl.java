package ru.ilpopov.otus.simple.library.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.CrudDao;
import ru.ilpopov.otus.simple.library.dao.RelationDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;

@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements CrudDao<Book> {

    private final NamedParameterJdbcTemplate jdbc;
    private final CrudDao<Author> authorDao;
    private final CrudDao<Genre> genreDao;
    private final RelationDao<Book, Author> bookAuthorRelationDao;
    private final RelationDao<Book, Genre> bookGenreRelationDao;

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
        return jdbc
                .query("SELECT b.ID, b.NAME, b.DESCRIPTION FROM BOOKS b WHERE b.ID = :id",
                        new MapSqlParameterSource()
                                .addValue("id", id),
                        new BookRowMapper())
                .stream()
                .peek(this::addRelatedObjects)
                .findFirst();
    }

    private void addRelatedObjects(Book b) {
        addAuthors(b);
        addGenres(b);
    }

    private void addGenres(Book book) {
        book.getGenres()
                .addAll(bookGenreRelationDao.findByFrom(book));
    }

    private void addAuthors(Book book) {
        book.getAuthors()
                .addAll(bookAuthorRelationDao.findByFrom(book));
    }

    @Override
    public Book update(Book book) {
        jdbc.update("UPDATE BOOKS SET NAME = :name, DESCRIPTION = :desc WHERE ID = :id",
                Map.of("id", book.getId(),
                        "name", book.getName(),
                        "desc", book.getDescription()));
        updateRelations(book);
        return get(book.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The book with id '%s' was found", book.getId())));
    }

    private void updateRelations(Book book) {
        updateBookAuthorsRelations(book);
        updateBookGenresRelations(book);
    }

    private void updateBookGenresRelations(Book book) {
        List<Genre> relatedGenres = bookGenreRelationDao.findByFrom(book);
        relatedGenres.stream()
                .filter(g -> !book.getGenres().contains(g))
                .forEach(g -> bookGenreRelationDao.detach(book, g));
        book.getGenres()
                .stream()
                .filter(g -> !relatedGenres.contains(g))
                .forEach(g -> bookGenreRelationDao.relate(book, g));
    }

    private void updateBookAuthorsRelations(Book book) {
        List<Author> relatedAuthors = bookAuthorRelationDao.findByFrom(book);
        relatedAuthors.stream()
                .filter(a -> !book.getAuthors().contains(a))
                .forEach(a -> bookAuthorRelationDao.detach(book, a));
        book.getAuthors()
                .stream()
                .filter(a -> !relatedAuthors.contains(a))
                .forEach(a -> bookAuthorRelationDao.relate(book, a));
    }

    @Override
    public void delete(long id) {
        get(id).ifPresent(b -> {
            b.getAuthors().forEach(a -> bookAuthorRelationDao.detach(b, a));
            b.getGenres().forEach(g -> bookGenreRelationDao.detach(b, g));
            jdbc.update("DELETE BOOKS WHERE ID = :id", Map.of("id", b.getId()));
        });
    }

    @Override
    public List<Book> findByName(String name) {
        return jdbc.query("SELECT b.ID, b.NAME, b.DESCRIPTION FROM BOOKS b WHERE b.NAME = :name",
                new MapSqlParameterSource()
                        .addValue("name", name),
                new BookRowMapper())
                .stream()
                .peek(this::addRelatedObjects)
                .collect(Collectors.toList());
    }

    private void createRelations(Book book) {
        createBookAuthorsRelations(book);
        createBookGenresRelations(book);
    }

    private void createBookAuthorsRelations(Book book) {
        book.getAuthors()
                .stream()
                .flatMap(a -> {
                    List<Author> authors = authorDao.findByName(a.getName());
                    if (authors.isEmpty()) {
                        throw new BookCreationException(
                                String.format("The author '%s' must be existed before the book", a.getName()));
                    }
                    return authors.stream();
                })
                .forEach(author -> bookAuthorRelationDao.relate(book, author));
    }

    private void createBookGenresRelations(Book book) {
        book.getGenres()
                .stream()
                .flatMap(g -> {
                    List<Genre> genres = genreDao.findByName(g.getName());
                    if (genres.isEmpty()) {
                        throw new BookCreationException(
                                String.format("The genre '%s' must be existed before the book", g.getName()));
                    }
                    return genres.stream();
                })
                .forEach(genre -> bookGenreRelationDao.relate(book, genre));
    }

    static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet record, int rowNum) throws SQLException {
            return new Book(record.getLong("ID"), record.getString("NAME"), record.getString("DESCRIPTION"));
        }
    }

}
