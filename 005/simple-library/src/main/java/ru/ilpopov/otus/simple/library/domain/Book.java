package ru.ilpopov.otus.simple.library.domain;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Book extends AbstractModel {

    private List<Author> authors = Lists.newArrayList();
    private List<Genre> genres = Lists.newArrayList();

    public Book(Long id, String name, String description) {
        super(id, name, description);
    }

    public Book(Long id, String name) {
        super(id, name);
    }

    public Book(String name, String description) {
        super(name, description);
    }

    public Book(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return super.toStringHelper()
                .add("authors", authors)
                .add("genres", genres)
                .toString();
    }
}
