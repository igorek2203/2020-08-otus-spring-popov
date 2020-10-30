package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.RelationDao;
import ru.ilpopov.otus.simple.library.dao.impl.AuthorDaoImpl.AuthorRowMapper;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;

@RequiredArgsConstructor
@Repository
public class BookAuthorRelationDaoImpl implements RelationDao<Book, Author> {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public void relate(Book from, Author to) {
        jdbc.update(
                "INSERT INTO BOOKS_AUTHORS_RELATIONS(BOOK_ID, AUTHOR_ID) VALUES(:bookId, :authorId)",
                new MapSqlParameterSource()
                        .addValue("bookId", from.getId())
                        .addValue("authorId", to.getId()));
    }

    @Override
    public List<Book> findByTo(Author to) {
        return jdbc.query(
                "SELECT b.* FROM BOOKS b JOIN BOOKS_AUTHORS_RELATIONS r on r.BOOK_ID = b.ID WHERE r.AUTHOR_ID = :authorId",
                Map.of("authorId", to.getId()),
                new BookDaoImpl.BookRowMapper());
    }

    @Override
    public List<Author> findByFrom(Book from) {
        return jdbc.query(
                "SELECT a.* FROM AUTHORS a JOIN BOOKS_AUTHORS_RELATIONS r on r.AUTHOR_ID = a.ID WHERE r.BOOK_ID = :bookId",
                Map.of("bookId", from.getId()),
                new AuthorRowMapper());
    }

    @Override
    public void detach(Book from, Author to) {
        jdbc.update("DELETE BOOKS_AUTHORS_RELATIONS WHERE BOOK_ID = :bookId AND AUTHOR_ID = :authorId",
                Map.of("bookId", from.getId(),
                        "authorId", to.getId()));
    }
}
