package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface AuthorService {

    @Validated(Insert.class)
    Author create(@Valid @NotNull Author author);

    Optional<Author> get(long id);

    @Validated(Update.class)
    Author update(@Valid @NotNull Author author);

    void delete(long id);

    List<Author> findByName(@NotNull String name);
}
