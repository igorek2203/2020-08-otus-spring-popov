package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;

@DisplayName("Тестирование DAO слоя по работе с жанрами")
@DataJpaTest
class GenreDaoJpaTest {

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Добавт в БД новый жанр")
    @Test
    void create() {
        Genre genre = new Genre("Test creation");
        assertThat(genreDao.save(genre))
                .satisfies(new Condition<>(g -> g.getId() != null, "id must be not null"))
                .extracting(Genre::getName)
                .isEqualTo("Test creation");
    }

    @DisplayName("Вычитает из БД жанр по идентификатору")
    @Test
    void get() {
        assertThat(genreDao.findById(1L).orElseThrow())
                .extracting(Genre::getName)
                .isEqualTo("Жанр 1");
    }

    @DisplayName("Переименует жанр")
    @Test
    void update() {
        Genre genre = genreDao.findById(1L).orElseThrow();
        genre.setName("Test update");
        assertThat(genreDao.save(genre))
                .extracting(Genre::getName)
                .isEqualTo("Test update");
    }

    @DisplayName("Удалит из БД жанр по идентификатору")
    @Test
    void delete() {
        long deleteId = 4L;
        genreDao.deleteById(deleteId);
        assertThat(em.find(Genre.class, deleteId)).isNull();
    }

    @DisplayName("Найдет жанр по его названию")
    @Test
    void findByName() {
        assertThat(genreDao.findByName("Жанр 1"))
                .flatExtracting(Genre::getId)
                .contains(1L);
    }
}