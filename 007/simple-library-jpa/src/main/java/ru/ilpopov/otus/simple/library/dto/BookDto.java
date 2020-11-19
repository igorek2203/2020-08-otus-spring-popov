package ru.ilpopov.otus.simple.library.dto;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
public class BookDto {

    @NotNull(groups = {Update.class}, message = "id must be set")
    private Long id;

    @NotNull(groups = {Create.class, Update.class}, message = "name must be set")
    private String title;
    private String description;
    private Set<Author> authors = Sets.newHashSet();
    private Set<Genre> genres = Sets.newHashSet();
    private final Set<Comment> comments = Sets.newHashSet();

    public BookDto(String title) {
        this(title, null);
    }

    public BookDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public BookDto(String bookName, String bookDescription, Set<Author> authors, Set<Genre> genres) {
        this(bookName, Strings.emptyToNull(bookDescription));
        this.getAuthors()
                .addAll(authors);
        this.getGenres()
                .addAll(genres);
    }

    public BookDto(Book book) {
        this.setId(book.getId());
        this.setTitle(book.getTitle());
        this.setDescription(book.getDescription());
        this.getAuthors().addAll(book.getAuthors());
        this.getGenres().addAll(book.getGenres());
    }

    public Book toBook() {
        Book book = new Book(this.id, this.title, this.description);
        book.getGenres().addAll(this.getGenres());
        book.getAuthors().addAll(this.getAuthors());
        return book;
    }
}
