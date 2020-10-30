package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.RelationDao;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;

@RequiredArgsConstructor
@Repository
public class BookGenreRelationDaoImpl implements RelationDao<Book, Genre> {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public void relate(Book from, Genre to) {
        jdbc.update(
                "INSERT INTO BOOKS_GENRES_RELATIONS(BOOK_ID, GENRE_ID) VALUES(:bookId, :genreId)",
                new MapSqlParameterSource()
                        .addValue("bookId", from.getId())
                        .addValue("genreId", to.getId()));
    }

    @Override
    public List<Book> findByTo(Genre to) {
        return jdbc.query(
                "SELECT b.* FROM BOOKS b JOIN BOOKS_GENRES_RELATIONS r on r.BOOK_ID = b.ID WHERE r.GENRE_ID = :genreId",
                Map.of("genreId", to.getId()),
                new BookDaoImpl.BookRowMapper());
    }

    @Override
    public List<Genre> findByFrom(Book from) {
        return jdbc.query(
                "SELECT g.* FROM GENRES g JOIN BOOKS_GENRES_RELATIONS r on r.GENRE_ID = g.ID WHERE r.BOOK_ID = :bookId",
                Map.of("bookId", from.getId()),
                new GenreDaoImpl.GenreRowMapper());
    }

    @Override
    public void detach(Book from, Genre to) {
        jdbc.update("DELETE BOOKS_GENRES_RELATIONS WHERE BOOK_ID = :bookId AND GENRE_ID = :genreId",
                Map.of("bookId", from.getId(),
                        "genreId", to.getId()));
    }
}
