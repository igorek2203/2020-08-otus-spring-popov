package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Comment;

@DisplayName("Тестирование DAO слоя по работе с коментариями")
@DataJpaTest
@Import({CommentDaoJpa.class})
class CommentDaoJpaTest {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TestEntityManager em;

    @DisplayName("добавит коментарий к книге")
    @Test
    void createCommentSuccess() {
        assertThat(commentDao.create(new Comment(5L, "test create")))
                .matches(c -> c.getId() != null)
                .matches(c -> c.getBookId() == 5L)
                .matches(c -> c.getText().equalsIgnoreCase("test create"));
    }

    @DisplayName("исключение при добавлении коментария без текста")
    @Test
    void createCommentException() {
        assertThatThrownBy(() -> commentDao.create(new Comment(5L, null)))
                .hasCauseInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("получит коментарий по идентификатору")
    @Test
    void getById() {
        assertThat(commentDao.getById(1L))
                .isNotEmpty()
                .get()
                .matches(c -> c.getId() == 1L)
                .matches(c -> c.getBookId() == 1L)
                .matches(c -> c.getText().equalsIgnoreCase("1 комментарий к книге 1"));
    }

    @DisplayName("получит пустой Optional по идентификатору")
    @Test
    void getByIdEmptyOptional() {
        assertThat(commentDao.getById(-1L))
                .isEmpty();
    }

    @DisplayName("обновит текст коментария к книге")
    @Test
    void updateCommentText() {
        var comment = em.find(Comment.class, 1L);
        comment.setText("Test update");
        assertThat(commentDao.update(comment))
                .matches(c -> "Test update".equalsIgnoreCase(c.getText()));
    }

    @DisplayName("изменит книгу к которой относится коментарий")
    @Test
    void updateCommentBookId() {
        var comment = em.find(Comment.class, 1L);
        comment.setBookId(2L);
        assertThat(commentDao.update(comment))
                .matches(c -> c.getBookId() == 2L);
    }

    @DisplayName("удалит коментарий к книге")
    @Test
    void deleteById() {
        commentDao.deleteById(5L);
        assertThat(commentDao.getById(5L))
                .isEmpty();
    }

    @DisplayName("найдет коментарий по содержащейся в нем фразе")
    @Test
    void findByText() {
        assertThat(commentDao.findByComment("комментарий для теста поиска"))
                .hasSize(2)
                .flatExtracting(Comment::getText)
                .containsOnlyOnce("1 комментарий для теста поиска к книге 2",
                        "2 комментарий для теста поиска к книге 2");
    }

    @DisplayName("найдет коментарий по идентификатору книги")
    @Test
    void findByBookId() {
        assertThat(commentDao.findByBookId(1L, 2L))
                .hasSize(6)
                .flatExtracting(Comment::getText)
                .containsOnlyOnce("1 комментарий к книге 1", "2 комментарий к книге 1", "3 комментарий к книге 1",
                        "1 комментарий для теста поиска к книге 2", "2 комментарий для теста поиска к книге 2",
                        "3 комментарий к книге 2");
    }
}