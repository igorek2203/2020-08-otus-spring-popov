package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface CommentService {

    Optional<Comment> getById(String commentId);

    @Validated(Create.class)
    Comment create(@Valid @NotNull Comment comment);

    @Validated(Update.class)
    Comment update(@Valid @NotNull Comment comment);

    void deleteById(String commentId);

    List<Comment> findByComment(@NotNull String comment);

    List<Comment> findByBookId(@NotNull List<String> bookId);
}
