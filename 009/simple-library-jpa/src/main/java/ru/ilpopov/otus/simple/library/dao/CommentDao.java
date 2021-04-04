package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Comment;

@Validated
public interface CommentDao extends CrudRepository<Comment, Long> {

    List<Comment> findByTextContaining(@NotNull String text);

    List<Comment> findAllByBook_idIn(@NotNull Set<Long> ids);

}
