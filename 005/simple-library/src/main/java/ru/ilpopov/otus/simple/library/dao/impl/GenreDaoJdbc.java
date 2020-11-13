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
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;

@RequiredArgsConstructor
@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Genre create(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("INSERT INTO GENRES(NAME) VALUES(:name)",
                new MapSqlParameterSource()
                        .addValue("name", genre.getName()),
                keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey());
        return getById(id.longValue())
                .orElseThrow(
                        () -> new BookCreationException(
                                String.format("The genre with id '%s' was not created", id)));
    }

    @Override
    public Optional<Genre> getById(long id) {
        return getByIds(Set.of(id)).stream()
                .findFirst();
    }

    @Override
    public Genre update(Genre genre) {
        jdbc.update("UPDATE GENRES SET NAME = :name WHERE ID = :id",
                Map.of("id", genre.getId(), "name", genre.getName()));
        return getById(genre.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The genre with id '%s' was found", genre.getId())));
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("DELETE GENRES WHERE ID = :id", Map.of("id", id));
    }

    @Override
    public List<Genre> findByName(String name) {
        return jdbc.query("SELECT g.ID, g.NAME FROM GENRES g WHERE g.NAME = :name",
                new MapSqlParameterSource()
                        .addValue("name", name),
                new GenreRowMapper());
    }

    private List<Genre> getByIds(Set<Long> ids) {
        return jdbc.query("SELECT g.ID, g.NAME FROM GENRES g WHERE g.ID in (:ids)",
                Map.of("ids", ids),
                new GenreRowMapper());
    }

    static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet record, int rowNum) throws SQLException {
            return new Genre(record.getLong("ID"), record.getString("NAME"));
        }
    }


}
