package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;

@DisplayName("Тестирование DAO слоя по работе с жанрами")
@JdbcTest
@Import({GenreDaoJdbc.class})
class GenreDaoJdbcTest {

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @DisplayName("Добавт в БД новый жанр")
    @Test
    void create() {
        Genre genre = new Genre("Test creation");
        assertThat(genreDao.create(genre))
                .extracting(Genre::getName)
                .isEqualTo("Test creation");
    }

    @DisplayName("Вычитает из БД жанр по идентификатору")
    @Test
    void get() {
        assertThat(genreDao.get(1L).get())
                .extracting(Genre::getName)
                .isEqualTo("Жанр 1");
    }

    @DisplayName("Переименует жанр")
    @Test
    void update() {
        Genre genre = genreDao.get(1L).get();
        genre.setName("Test update");
        assertThat(genreDao.update(genre))
                .extracting(Genre::getName)
                .isEqualTo("Test update");
    }

    @DisplayName("Удалит из БД жанр по идентификатору")
    @Test
    void delete() {
        long deleteId = 4L;
        genreDao.delete(deleteId);

        Long count = jdbc.queryForObject(
                "SELECT COUNT(g.*) FROM GENRES g WHERE g.ID = :id",
                Map.of("id", deleteId),
                Long.class);

        assertThat(count).isEqualTo(0);

    }

    @DisplayName("Найдет жанр по его названию")
    @Test
    void findByName() {
        assertThat(genreDao.findByName("Жанр 1"))
                .flatExtracting(Genre::getId)
                .contains(1L);
    }
}