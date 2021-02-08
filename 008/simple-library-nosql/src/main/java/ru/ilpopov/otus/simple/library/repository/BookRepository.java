package ru.ilpopov.otus.simple.library.repository;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.Book;

@Validated
@Repository
public interface BookRepository extends MongoRepository<Book, String>, BookCustomRepository {

    List<Book> findByTitleContaining(@NotNull String title);

    List<Book> findByAuthors_fullName(@NotNull String fullName);

    List<Book> findByGenres_name(@NotNull String name);
}
