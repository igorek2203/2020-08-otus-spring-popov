package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface GenreService {

    @Validated(Create.class)
    Genre create(@Valid @NotNull Genre genre);

    Optional<Genre> getById(long genreId);

    @Validated(Update.class)
    Genre update(@Valid @NotNull Genre genre);

    void deleteById(long genreId);

    List<Genre> findByName(@NotNull String name);
}
