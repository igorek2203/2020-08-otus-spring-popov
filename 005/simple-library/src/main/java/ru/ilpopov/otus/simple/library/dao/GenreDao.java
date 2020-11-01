package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Genre;

@Validated
public interface GenreDao {

    Genre create(@NotNull Genre genre);

    Optional<Genre> get(long id);

    Genre update(@NotNull Genre genre);

    void delete(long id);

    List<Genre> findByName(@NotNull String name);
}
