package ru.ilpopov.otus.simple.library.shell;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookModificationException;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;
import ru.ilpopov.otus.simple.library.service.AuthorService;
import ru.ilpopov.otus.simple.library.service.BookService;
import ru.ilpopov.otus.simple.library.service.GenreService;
import ru.ilpopov.otus.simple.library.service.StringIOService;

@RequiredArgsConstructor
@ShellComponent
public class SimpleLibraryCommands {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final StringIOService stringIOService;

    @ShellMethod(value = "Create a new book", key = {"new-book"})
    public void createBook(
            @ShellOption(value = "--name", help = "Book name")
                    String bookName,
            @ShellOption(value = "--desc", help = "Book description")
                    String bookDescription,
            @ShellOption(value = {"-A", "--author"},
                    defaultValue = "Unknown",
                    help = "Author name. Split Authors by comma.")
                    String authors,
            @ShellOption(value = {"-G", "--genre"},
                    defaultValue = "Unknown",
                    help = "Genre name. Split genres by comma.")
                    String genres) {

        List<Author> authorList = Splitter.on(",")
                .splitToStream(authors)
                .map(Author::new)
                .collect(Collectors.toList());

        List<Genre> genreList = Splitter.on(",")
                .splitToStream(genres)
                .map(Genre::new)
                .collect(Collectors.toList());

        Book book = new Book(bookName);
        if (!Strings.isNullOrEmpty(bookDescription)) {
            book.setDescription(bookDescription);
        }
        book.getAuthors().addAll(authorList);
        book.getGenres().addAll(genreList);
        Book createdBook = bookService.create(book);
        stringIOService.writeln(String.format("Created book %s", createdBook));
    }

    @ShellMethod(value = "Find books by name", key = {"find-book"})
    public void findBookByName(@ShellOption String bookName) {
        bookService.findByName(bookName)
                .forEach(b -> stringIOService.writeln(String.format("The book %s", b)));
    }

    @ShellMethod(value = "Modify the book", key = {"modify-book"})
    public void modifyBook(
            @ShellOption(value = "--id", help = "The book id") Long bookId,
            @ShellOption(value = "--field", help = "Field name") String fieldName,
            @ShellOption(value = "--value", help = "New Value") String fieldValue) {
        Book newBook = bookService.get(bookId)
                .map(b -> {
                    switch (fieldName) {
                        case "name":
                            b.setName(fieldValue);
                            break;
                        case "description":
                            b.setDescription(fieldValue);
                    }
                    return b;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("The book was not found"));
        stringIOService.writeln(String.format("Updated book %s", newBook));
    }

    @ShellMethod(value = "Associate an existed author with a book", key = {"book-add-author"})
    public void bookAddAuthor(
            @ShellOption(value = "--id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--author", help = "An author name") String authorName) {
        List<Author> authors = authorService.findByName(authorName);
        if (authors.isEmpty()) {
            throw new ObjectNotFound(String.format("The author '%s' does not exists", authorName));
        }
        Book updatedBook = bookService.get(bookId)
                .map(book -> {
                    book.getAuthors()
                            .addAll(authors);
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
        stringIOService.writeln(String.format("Updated book %s", updatedBook));
    }

    @ShellMethod(value = "Dissociate an existed author with a book", key = {"book-remove-author"})
    public void bookRemoveAuthor(
            @ShellOption(value = "--id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--author", help = "An author name") String authorName) {
        Book updatedBook = bookService.get(bookId)
                .map(book -> {
                    book.getAuthors()
                            .removeIf(a -> a.getName().equalsIgnoreCase(authorName));
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
        stringIOService.writeln(String.format("Updated book %s", updatedBook));
    }

    @ShellMethod(value = "Associate an existed genre with a book", key = {"book-add-genre"})
    public void bookAddGenre(
            @ShellOption(value = "--id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--genre", help = "An genre name") String genreName) {
        List<Genre> genres = genreService.findByName(genreName);
        if (genres.isEmpty()) {
            throw new ObjectNotFound(String.format("The genre '%s' does not exists", genreName));
        }
        Book updatedBook = bookService.get(bookId)
                .map(book -> {
                    book.getGenres()
                            .addAll(genres);
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
        stringIOService.writeln(String.format("Updated book %s", updatedBook));
    }

    @ShellMethod(value = "Dissociate an existed genre with a book", key = {"book-remove-genre"})
    public void bookRemoveGenre(
            @ShellOption(value = "--id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--genre", help = "An genre name") String genreName) {
        Book updatedBook = bookService.get(bookId)
                .map(book -> {
                    book.getGenres()
                            .removeIf(a -> a.getName().equalsIgnoreCase(genreName));
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
        stringIOService.writeln(String.format("Updated book %s", updatedBook));
    }

    @ShellMethod(value = "Delete the book by id", key = {"del-book"})
    public void deleteBook(@ShellOption long id) {
        bookService.delete(id);
        stringIOService.writeln("The book deleted");
    }


}