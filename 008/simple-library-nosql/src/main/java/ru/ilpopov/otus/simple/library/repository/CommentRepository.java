package ru.ilpopov.otus.simple.library.repository;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Comment;

@Validated
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByTextContaining(@NotNull String text);

    List<Comment> findByBookIn(@NotNull List<String> ids);

    void deleteAllByBook(@NotNull String bookId);
}
