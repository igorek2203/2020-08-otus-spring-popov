package ru.ilpopov.otus.simple.library.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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

    @Fetch(FetchMode.SUBSELECT)
    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(groups = {Create.class, Update.class}, message = "id must be set")
    @Column(name = "text", nullable = false)
    private String text;

    public Comment(Book book, String text) {
        this.book = book;
        this.text = text;
    }
}
