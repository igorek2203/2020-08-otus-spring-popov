package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface AuthorService {

    @Validated(Create.class)
    Author create(@Valid @NotNull Author author);

    Optional<Author> getById(long authorId);

    @Validated(Update.class)
    Author update(@Valid @NotNull Author author);

    void deleteById(long authorId);

    List<Author> findByFullName(@NotNull String fullName);

    List<Author> findAll();
}
