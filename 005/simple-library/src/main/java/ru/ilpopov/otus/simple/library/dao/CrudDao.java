package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CrudDao<T> {

    T create(@NotNull T entity);

    Optional<T> get(long id);

    T update(@NotNull T entity);

    void delete(long id);

    List<T> findByName(@NotNull String name);
}
