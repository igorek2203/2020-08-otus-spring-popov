package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Author;

@Validated
public interface AuthorDao {

    Author create(@NotNull Author author);

    Optional<Author> getById(long authorId);

    Author update(@NotNull Author author);

    void deleteById(long authorId);

    List<Author> findByFullName(@NotNull String fullName);

}
