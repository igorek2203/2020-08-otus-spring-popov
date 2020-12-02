package ru.ilpopov.otus.simple.library.domain;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {

    @NotNull(groups = {Update.class}, message = "id must be set")
    @Id
    private String id;

    @DBRef
    private Book book;
    private String text;

    public Comment(Book book, String text) {
        this.book = book;
        this.text = text;
    }
}
