package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface GenreService {

    @Validated(Insert.class)
    Genre create(@Valid @NotNull Genre genre);

    Optional<Genre> get(long id);

    @Validated(Update.class)
    Genre update(@Valid @NotNull Genre genre);

    void delete(long id);

    List<Genre> findByName(@NotNull String name);
}
