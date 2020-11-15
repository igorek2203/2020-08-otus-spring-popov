package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Comment;

@Validated
public interface CommentDao {

    Comment create(@NotNull Comment comment);

    Optional<Comment> getById(long commentId);

    Comment update(@NotNull Comment comment);

    void deleteById(long commentId);

    List<Comment> findByComment(@NotNull String text);

    List<Comment> findByBookId(@NotNull Long... bookId);

}
