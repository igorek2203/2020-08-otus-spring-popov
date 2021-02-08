package ru.ilpopov.otus.simple.library.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
@Document(collection = "books")
public class Book {

    @NotNull(groups = {Update.class}, message = "id must be set")
    @Id
    private String id;

    @NotNull(groups = {Create.class, Update.class}, message = "name must be set")
    private String title;
    private String description;
    private List<Author> authors = Lists.newArrayList();
    private List<Genre> genres = Lists.newArrayList();

    public Book(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Book(String title, String description) {
        this(null, title, description);
    }

    public Book(String title) {
        this(null, title, null);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("description", description)
                .toString();
    }

}
