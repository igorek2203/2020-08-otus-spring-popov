package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;

@DisplayName("Тестирование DAO слоя по работе с авторами")
@JdbcTest
@Import({AuthorDaoJdbc.class})
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @DisplayName("Добавит в БД нового автора")
    @Test
    void create() {
        Author author = new Author("Test");
        assertThat(authorDao.create(author))
                .extracting(Author::getFullName)
                .isEqualTo("Test");
    }

    @DisplayName("Вычитает из БД автора по идентификатору")
    @Test
    void get() {
        assertThat(authorDao.getById(1L)).get()
                .extracting(Author::getFullName)
                .isEqualTo("Автор 1");
    }

    @DisplayName("Переименует автора")
    @Test
    void update() {
        Author author = authorDao.getById(1L).get();
        author.setFullName("Test update");
        assertThat(authorDao.update(author))
                .extracting(Author::getFullName)
                .isEqualTo("Test update");
    }

    @DisplayName("Удалит из БД автора по идентификатору")
    @Test
    void delete() {
        long deleteId = 4L;
        authorDao.deleteById(deleteId);

        Long count = jdbc.queryForObject(
                "SELECT COUNT(a.*) FROM AUTHORS a WHERE a.ID = :id",
                Map.of("id", deleteId),
                Long.class);

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("Найдет автора по имени")
    @Test
    void findByName() {
        assertThat(authorDao.findByFullName("Автор 1"))
                .flatExtracting(Author::getId)
                .contains(1L);
    }
}