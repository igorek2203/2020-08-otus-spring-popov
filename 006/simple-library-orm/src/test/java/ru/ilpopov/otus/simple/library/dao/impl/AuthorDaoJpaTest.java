package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;

@DisplayName("Тестирование DAO слоя по работе с авторами")
@DataJpaTest
@Import({AuthorDaoJpa.class})
class AuthorDaoJpaTest {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private EntityManager em;

    @DisplayName("Добавит в БД нового автора")
    @Test
    void create() {
        assertThat(authorDao.create(new Author("Test")))
                .satisfies(new Condition<>(a -> a.getId() != null, "id must be not null"))
                .extracting(Author::getFullName)
                .isEqualTo("Test");
    }

    @DisplayName("Вычитает из БД автора по идентификатору")
    @Test
    void get() {
        assertThat(authorDao.getById(1L).orElseThrow())
                .extracting(Author::getFullName)
                .isEqualTo("Автор 1");
    }

    @DisplayName("Переименует автора")
    @Test
    void update() {
        Author author = authorDao.getById(1L).orElseThrow();
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
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(a) FROM Author a WHERE a.id = :id", Long.class);
        query.setParameter("id", deleteId);
        Long count = query.getSingleResult();

        assertThat(count).isEqualTo(0);
    }

    @DisplayName("Найдет жанр по названию")
    @Test
    void findByName() {
        assertThat(authorDao.findByFullName("Автор 1"))
                .flatExtracting(Author::getId)
                .contains(1L);
    }
}