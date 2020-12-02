package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Book;

@Validated
@Repository
public interface BookDao extends MongoRepository<Book, String> {

    List<Book> findByTitleContaining(@NotNull String title);

    List<Book> findByAuthors_fullName(@NotNull String fullName);

    List<Book> findByGenres_name(@NotNull String name);
}
