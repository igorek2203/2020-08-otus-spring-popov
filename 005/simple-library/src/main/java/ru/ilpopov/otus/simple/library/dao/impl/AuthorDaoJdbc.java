package ru.ilpopov.otus.simple.library.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
        jdbc.update("INSERT INTO AUTHORS(FULL_NAME, DESCRIPTION) VALUES(:name, :desc)",
                new MapSqlParameterSource()
                        .addValue("name", author.getFullName())
                        .addValue("desc", author.getDescription()),
                keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey());
        return getOptional(id.longValue())
                .orElseThrow(
                        () -> new BookCreationException(
                                String.format("The author with id '%s' was not created", id)));
    }

    @Override
    public Optional<Author> getOptional(long id) {
        return getByIds(Set.of(id)).stream()
                .findFirst();
    }

    @Override
    public Author update(Author author) {
        jdbc.update("UPDATE AUTHORS SET FULL_NAME = :name, DESCRIPTION = :desc WHERE ID = :id",
                Map.of("id", author.getId(),
                        "name", author.getFullName(),
                        "desc", author.getDescription()));
        return getOptional(author.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The author with id '%s' was found", author.getId())));
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("DELETE AUTHORS WHERE ID = :id", Map.of("id", id));
    }

    @Override
    public List<Author> findByFullName(String name) {
        return jdbc.query("SELECT a.ID, a.FULL_NAME, a.DESCRIPTION FROM AUTHORS a WHERE a.FULL_NAME = :name",
                new MapSqlParameterSource()
                        .addValue("name", name),
                new AuthorRowMapper());
    }

    private List<Author> getByIds(Set<Long> ids) {
        return jdbc.query("SELECT a.ID, a.FULL_NAME, a.DESCRIPTION FROM AUTHORS a WHERE a.ID in (:ids)",
                new MapSqlParameterSource()
                        .addValue("ids", ids),
                new AuthorRowMapper());
    }

    static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet record, int rowNum) throws SQLException {
            return new Author(record.getLong("ID"), record.getString("FULL_NAME"), record.getString("DESCRIPTION"));
        }
    }
}
