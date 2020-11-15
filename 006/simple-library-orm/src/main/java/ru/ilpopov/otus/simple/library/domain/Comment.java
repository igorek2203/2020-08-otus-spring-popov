package ru.ilpopov.otus.simple.library.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @NotNull(groups = {Update.class}, message = "id must be set")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private long bookId;

    @NotNull(groups = {Create.class, Update.class}, message = "id must be set")
    @Column(name = "text", nullable = false)
    private String text;

    public Comment(long bookId, String text) {
        this.bookId = bookId;
        this.text = text;
    }
}
