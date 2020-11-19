package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Book;

@Validated
public interface BookDao extends CrudRepository<Book, Long> {

    List<Book> findByTitleContaining(@NotNull String title);

    List<Book> findByAuthors_fullName(@NotNull String fullName);

    List<Book> findByGenres_name(@NotNull String name);
}
