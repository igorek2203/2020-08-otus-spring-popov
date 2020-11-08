package ru.ilpopov.otus.simple.library.domain;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
public class Book {

    @NotNull(groups = {Update.class}, message = "id must be set")
    private Long id;

    @NotNull(groups = {Insert.class, Update.class}, message = "name must be set")
    private String title;

    private String description;

    private final Set<Author> authors = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();

    public Book(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Book(Long id, String title) {
        this(id, title, null);
    }

    public Book(String title, String description) {
        this(null, title, description);
    }

    public Book(String title) {
        this(title, null);
    }
}
