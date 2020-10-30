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
import ru.ilpopov.otus.simple.library.dao.CrudDao;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;

@RequiredArgsConstructor
@Repository
public class GenreDaoImpl implements CrudDao<Genre> {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Genre create(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("INSERT INTO GENRES(NAME, DESCRIPTION) VALUES(:name, :desc)",
                new MapSqlParameterSource()
                        .addValue("name", genre.getName())
                        .addValue("desc", genre.getDescription()),
                keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey());
        return get(id.longValue())
                .orElseThrow(
                        () -> new BookCreationException(
                                String.format("The genre with id '%s' was not created", id)));
    }

    @Override
    public Optional<Genre> get(long id) {
        return jdbc.query("SELECT g.ID, g.NAME, g.DESCRIPTION FROM GENRES g WHERE g.ID = :id",
                new MapSqlParameterSource()
                        .addValue("id", id),
                new GenreRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public Genre update(Genre genre) {
        jdbc.update("UPDATE GENRES SET NAME = :name, DESCRIPTION = :desc WHERE ID = :id",
                Map.of("id", genre.getId(), "name", genre.getName(), "desc", genre.getDescription()));
        return get(genre.getId())
                .orElseThrow(
                        () -> new ObjectNotFound(
                                String.format("The genre with id '%s' was found", genre.getId())));
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE GENRES WHERE ID = :id", Map.of("id", id));
    }

    @Override
    public List<Genre> findByName(String name) {
        return jdbc.query("SELECT g.ID, g.NAME, g.DESCRIPTION FROM GENRES g WHERE g.NAME = :name",
                new MapSqlParameterSource()
                        .addValue("name", name),
                new GenreRowMapper());
    }

    static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet record, int rowNum) throws SQLException {
            return new Genre(record.getLong("ID"), record.getString("NAME"), record.getString("DESCRIPTION"));
        }
    }
}
