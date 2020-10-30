package ru.ilpopov.otus.simple.library.service;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface CrudService<T> {

    @Validated(Insert.class)
    T create(@Valid @NotNull T entity);

    Optional<T> get(long id);

    @Validated(Update.class)
    T update(@Valid @NotNull T entity);

    void delete(long id);

    List<T> findByName(@NotNull String name);
}
