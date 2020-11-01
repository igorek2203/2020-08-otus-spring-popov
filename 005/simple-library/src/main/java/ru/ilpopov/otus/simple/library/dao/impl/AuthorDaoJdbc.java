package ru.ilpopov.otus.simple.library.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;

@RequiredArgsConstructor
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Author create(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("INSERT INTO AUTHORS(NAME, DESCRIPTION) VALUES(:name, :desc)",
                new MapSqlParameterSource()
                        .addValue("name", author.getName())
                        .addValue("desc", author.getDescription()),
                keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey());
        return get(id.longValue())
                .orElseThrow(
                        () -> new BookCreationException(
                                String.format("The author with id '%s' was not created", id)));
    }

    @Override
    public Optional<Author> get(long id) {
        return jdbc.query("SELECT a.ID, a.NAME, a.DESCRIPTION FROM AUTHORS a WHERE a.ID = :id",
                new MapSqlParameterSource()
                        .addValue("id", id),
                new AuthorRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public Author update(Author author) {
        jdbc.update("UPDATE AUTHORS SET NAME = :name, DESCRIPTION = :desc WHERE ID = :id",
                Map.of("id", author.getId(),
                        "name", author.getName(),
                        "desc", author.getDescription()));
        return get(author.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The author with id '%s' was found", author.getId())));
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE AUTHORS WHERE ID = :id", Map.of("id", id));
    }

    @Override
    public List<Author> findByName(String name) {
        return jdbc.query("SELECT a.ID, a.NAME, a.DESCRIPTION FROM AUTHORS a WHERE a.NAME = :name",
                new MapSqlParameterSource()
                        .addValue("name", name),
                new AuthorRowMapper());
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet record, int rowNum) throws SQLException {
            return new Author(record.getLong("ID"), record.getString("NAME"), record.getString("DESCRIPTION"));
        }
    }
}
