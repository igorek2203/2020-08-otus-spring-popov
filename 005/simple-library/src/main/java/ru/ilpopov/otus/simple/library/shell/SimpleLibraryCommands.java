package ru.ilpopov.otus.simple.library.shell;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayList;
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
import ru.ilpopov.otus.simple.library.service.FormatterService;
import ru.ilpopov.otus.simple.library.service.GenreService;
import ru.ilpopov.otus.simple.library.service.StringIOService;

@RequiredArgsConstructor
@ShellComponent
public class SimpleLibraryCommands {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final StringIOService stringIOService;
    private final FormatterService<Book> bookFormatter;

    @ShellMethod(value = "Create a new book", key = {"book-new"})
    public void createBook(
            @ShellOption(value = "--name", help = "Book name")
                    String bookName,
            @ShellOption(value = "--desc", defaultValue = "", help = "Book description")
                    String bookDescription,
            @ShellOption(value = {"-A", "--author"},
                    defaultValue = "Неизвестный автор", help = "Author name. Split Authors by comma.")
                    String authors,
            @ShellOption(value = {"-G", "--genre"},
                    defaultValue = "Неизвестный жанр", help = "Genre name. Split genres by comma.")
                    String genres) {
        Book createdBook = bookService.create(newBook(bookName, bookDescription, authors, genres));
        stringIOService.writeln("Book created");
        stringIOService.writeln(bookFormatter.formatToString(createdBook));
    }

    @ShellMethod(value = "Find books by title", key = {"book-find"})
    public void findBooksByTitle(
            @ShellOption(value = "--title", defaultValue = "", help = "Book title") String bookTitle,
            @ShellOption(value = "-A", defaultValue = "", help = "Author full name") String authorFullName,
            @ShellOption(value = "-G", defaultValue = "", help = "Genre name") String genreName
    ) {
        List<Book> books = findBooksByTitleOrAuthorFullNameOrGenreName(bookTitle, authorFullName, genreName);
        stringIOService.writeln("----------------------------------");
        stringIOService.writeln(String.format("Books found: %s", books.size()));
        books.forEach(b -> {
            stringIOService.writeln("----------------------------------");
            stringIOService.writeln(bookFormatter.formatToString(b));
            stringIOService.writeln("");
        });
    }

    @ShellMethod(value = "Modify the book", key = {"book-modify"})
    public void modifyBook(
            @ShellOption(value = "--id", help = "The book id") Long bookId,
            @ShellOption(value = "--field", help = "Field name") String fieldName,
            @ShellOption(value = "--value", help = "New Value") String fieldValue) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                updateBookField(bookId, fieldName, fieldValue)));
    }


    @ShellMethod(value = "Associate an existed author with a book", key = {"book-add-author"})
    public void bookAddAuthor(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--author", help = "An author name") String authorName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                addAuthorToBook(bookId, authorName)));
    }

    @ShellMethod(value = "Dissociate an existed author with a book", key = {"book-remove-author"})
    public void bookRemoveAuthor(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--author", help = "An author name") String authorName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                removeAuthorFromBook(bookId, authorName)
        ));
    }

    @ShellMethod(value = "Associate an existed genre with a book", key = {"book-add-genre"})
    public void bookAddGenre(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--genre", help = "An genre name") String genreName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                addGenreToBook(bookId, genreName)));
    }

    @ShellMethod(value = "Dissociate an existed genre with a book", key = {"book-remove-genre"})
    public void bookRemoveGenre(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--genre", help = "An genre name") String genreName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                removeGenreFromBook(bookId, genreName)));
    }

    @ShellMethod(value = "Delete the book by id", key = {"book-delete"})
    public void deleteBook(@ShellOption long id) {
        bookService.deleteById(id);
        stringIOService.writeln("The book deleted");
    }

    private Book removeGenreFromBook(Long bookId, String genreName) {
        return bookService.getById(bookId)
                .map(book -> {
                    book.getGenres().removeIf(a -> a.getName().equalsIgnoreCase(genreName));
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    private Book addGenreToBook(Long bookId, String genreName) {
        List<Genre> genres = genreService.findByName(genreName);
        if (genres.isEmpty()) {
            throw new ObjectNotFound(String.format("The genre '%s' does not exists", genreName));
        }
        return bookService.getById(bookId)
                .map(book -> {
                    book.getGenres().addAll(genres);
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    private Book removeAuthorFromBook(Long bookId, String authorName) {
        return bookService.getById(bookId)
                .map(book -> {
                    book.getAuthors().removeIf(a -> a.getFullName().equalsIgnoreCase(authorName));
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    private Book addAuthorToBook(Long bookId, String authorName) {
        List<Author> authors = authorService.findByFullName(authorName);
        if (authors.isEmpty()) {
            throw new ObjectNotFound(String.format("The author '%s' does not exists", authorName));
        }
        return bookService.getById(bookId)
                .map(book -> {
                    book.getAuthors().addAll(authors);
                    return book;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("Can't modify the book"));
    }

    private Book updateBookField(Long bookId, String fieldName, String fieldValue) {
        return bookService.getById(bookId)
                .map(b -> {
                    switch (fieldName) {
                        case "name":
                            b.setTitle(fieldValue);
                            break;
                        case "description":
                            b.setDescription(fieldValue);
                    }
                    return b;
                })
                .map(bookService::update)
                .orElseThrow(() -> new BookModificationException("The book was not found"));
    }

    private List<Book> findBooksByTitleOrAuthorFullNameOrGenreName(String bookTitle, String authorFullName,
            String genreName) {
        List<Book> books = new ArrayList<>();
        if (!Strings.isNullOrEmpty(bookTitle)) {
            bookService.findByTitle(bookTitle)
                    .stream()
                    .filter(b -> !books.contains(b))
                    .forEach(books::add);
        }
        if (!Strings.isNullOrEmpty(authorFullName)) {
            bookService.findByAuthorFullName(authorFullName)
                    .stream()
                    .filter(b -> !books.contains(b))
                    .forEach(books::add);
        }
        if (!Strings.isNullOrEmpty(genreName)) {
            bookService.findByGenreName(genreName)
                    .stream()
                    .filter(b -> !books.contains(b))
                    .forEach(books::add);
        }
        return books;
    }

    private Book newBook(String bookName, String bookDescription, String authors, String genres) {
        List<Author> authorList = authorsFullNamesStringToAuthors(authors);
        List<Genre> genreList = genresNamesToGenres(genres);
        Book book = new Book(bookName);
        if (!Strings.isNullOrEmpty(bookDescription)) {
            book.setDescription(bookDescription);
        }
        book.getAuthors().addAll(authorList);
        book.getGenres().addAll(genreList);
        return book;
    }

    private List<Genre> genresNamesToGenres(String genres) {
        List<Genre> genreList = Splitter.on(",")
                .splitToStream(genres)
                .map(Genre::new)
                .collect(Collectors.toList());
        return genreList;
    }

    private List<Author> authorsFullNamesStringToAuthors(String authors) {
        List<Author> authorList = Splitter.on(",")
                .splitToStream(authors)
                .map(Author::new)
                .collect(Collectors.toList());
        return authorList;
    }

}