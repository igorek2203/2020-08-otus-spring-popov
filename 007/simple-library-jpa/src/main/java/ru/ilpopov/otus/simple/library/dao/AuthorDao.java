package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Author;

@Validated
public interface AuthorDao extends CrudRepository<Author, Long> {

    List<Author> findByFullName(@NotNull String fullName);

}
