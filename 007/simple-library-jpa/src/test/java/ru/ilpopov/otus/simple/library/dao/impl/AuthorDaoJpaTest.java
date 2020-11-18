package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;

@DisplayName("Тестирование DAO слоя по работе с авторами")
@DataJpaTest
class AuthorDaoJpaTest {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Добавит в БД нового автора")
    @Test
    void create() {
        assertThat(authorDao.save(new Author("Test")))
                .satisfies(new Condition<>(a -> a.getId() != null, "id must be not null"))
                .extracting(Author::getFullName)
                .isEqualTo("Test");
    }

    @DisplayName("Вычитает из БД автора по идентификатору")
    @Test
    void get() {
        assertThat(authorDao.findById(1L).orElseThrow())
                .extracting(Author::getFullName)
                .isEqualTo("Автор 1");
    }

    @DisplayName("Переименует автора")
    @Test
    void update() {
        Author author = authorDao.findById(1L).orElseThrow();
        author.setFullName("Test update");
        assertThat(authorDao.save(author))
                .extracting(Author::getFullName)
                .isEqualTo("Test update");
    }

    @DisplayName("Удалит из БД автора по идентификатору")
    @Test
    void delete() {
        long deleteId = 4L;
        authorDao.deleteById(deleteId);
        assertThat(em.find(Author.class, deleteId)).isNull();
    }

    @DisplayName("Найдет жанр по названию")
    @Test
    void findByName() {
        assertThat(authorDao.findByFullName("Автор 1"))
                .flatExtracting(Author::getId)
                .contains(1L);
    }
}