package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Genre;

@Validated
public interface GenreDao extends CrudRepository<Genre, Long> {

    List<Genre> findByName(@NotNull String name);
}
