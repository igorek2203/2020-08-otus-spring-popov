package ru.ilpopov.otus.simple.library.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.service.CommentService;

@DisplayName("Тестирование сервиса по работе с коментариями")
@SpringBootTest(classes = {CommentServiceImpl.class})
@Import(ValidationAutoConfiguration.class)
class CommentServiceImplTest {

    @MockBean
    private CommentDao commentDao;

    @Autowired
    private CommentService commentService;

    @DisplayName("получит коментарий по идентификатору")
    @Test
    void getById() {
        Book book = new Book(1L, "test");
        given(commentDao.findById(any(Long.class)))
                .willReturn(Optional.of(new Comment(book, "test get")));

        assertThat(commentService.getById(1L))
                .isNotEmpty()
                .get()
                .matches(c -> "test get".equalsIgnoreCase(c.getText()));

        verify(commentDao, only()).findById(1L);
    }

    @DisplayName("добавит коментарий к книге")
    @Test
    void create() {
        Book book = new Book(1L, "test");
        given(commentDao.save(any(Comment.class)))
                .willReturn(new Comment(book, "test create"));

        assertThat(commentService.create(new Comment(book, "test create")))
                .matches(c -> c.getBook().getId() == 1L)
                .matches(c -> "test create".equalsIgnoreCase(c.getText()));

        verify(commentDao, only()).save(any(Comment.class));
    }

    @DisplayName("изменит коментарий")
    @Test
    void update() {
        Book book = new Book(1L, "test");
        Comment comment = new Comment(book, "test update");
        comment.setId(1L);

        given(commentDao.save(any(Comment.class)))
                .willReturn(comment);

        assertThat(commentService.update(comment))
                .matches(c -> c.getBook().getId() == 1L)
                .matches(c -> "test update".equalsIgnoreCase(c.getText()));

        verify(commentDao, only()).save(any(Comment.class));
    }

    @DisplayName("удалит комментарий")
    @Test
    void deleteById() {
        commentService.deleteById(1L);

        verify(commentDao, only()).deleteById(1L);
    }

    @DisplayName("найдет комментарии по части его текста")
    @Test
    void findByComment() {
        Book book = new Book(1L, "test");
        Comment comment = new Comment(book, "test");
        comment.setId(1L);

        given(commentDao.findByTextContaining("test"))
                .willReturn(List.of(comment));

        assertThat(commentService.findByComment("test"))
                .isNotEmpty()
                .flatExtracting(Comment::getText)
                .containsOnlyOnce("test");

        verify(commentDao, only()).findByTextContaining("test");
    }

    @DisplayName("найдет коментарий по идентификатору книги")
    @Test
    void findByBookId() {
        Book book = new Book(1L, "test");
        Comment comment = new Comment(book, "test");
        comment.setId(1L);

        given(commentDao.findAllByBook_idIn(Set.of(1L)))
                .willReturn(List.of(comment));

        assertThat(commentService.findByBookId(Set.of(1L)))
                .isNotEmpty()
                .flatExtracting(Comment::getText)
                .containsOnlyOnce("test");

        verify(commentDao, only()).findAllByBook_idIn(Set.of(1L));
    }
}