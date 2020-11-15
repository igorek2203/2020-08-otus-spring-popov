package ru.ilpopov.otus.simple.library.shell;

import com.google.common.base.Splitter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.dto.BookDto;
import ru.ilpopov.otus.simple.library.service.BookService;
import ru.ilpopov.otus.simple.library.service.CommentService;
import ru.ilpopov.otus.simple.library.service.FormatterService;
import ru.ilpopov.otus.simple.library.service.StringIOService;

@RequiredArgsConstructor
@ShellComponent
public class SimpleLibraryCommands {

    private final BookService bookService;
    private final StringIOService stringIOService;
    private final FormatterService<BookDto> bookFormatter;
    private final CommentService commentService;

    @ShellMethod(value = "Create a new book", key = {"book-new"})
    public void createBook(
            @ShellOption(value = "--title", help = "Book title")
                    String bookName,
            @ShellOption(value = "--desc", defaultValue = "", help = "Book description")
                    String bookDescription,
            @ShellOption(value = {"-A", "--author"},
                    defaultValue = "Неизвестный автор", help = "Author name. Split Authors by comma.")
                    String authors,
            @ShellOption(value = {"-G", "--genre"},
                    defaultValue = "Неизвестный жанр", help = "Genre name. Split genres by comma.")
                    String genres) {
        BookDto book = new BookDto(bookName, bookDescription, parseAuthorsFullNames(authors), parseGenresNames(genres));
        stringIOService.writeln("Book created");
        stringIOService.writeln(
                bookFormatter.formatToString(bookService.create(book)));
    }

    @ShellMethod(value = "Find books by title", key = {"book-find"})
    public void findBooks(
            @ShellOption(value = "-B", defaultValue = "", help = "Book title") String bookTitle,
            @ShellOption(value = "-A", defaultValue = "", help = "Author full name") String authorFullName,
            @ShellOption(value = "-G", defaultValue = "", help = "Genre name") String genreName,
            @ShellOption(value = "-C", defaultValue = "false", help = "Include comments") boolean withComments) {
        List<BookDto> books = bookService.findAllByTitleAndAuthorFullNameAndGenreName(bookTitle,
                authorFullName, genreName, withComments);
        books.forEach(b -> {
            stringIOService.writeln("----------------------------------");
            stringIOService.writeln(bookFormatter.formatToString(b));
            if (withComments) {
                outputBookComments(b);
            }
        });
    }

    @ShellMethod(value = "Modify the book", key = {"book-modify"})
    public void modifyBook(
            @ShellOption(value = "--id", help = "The book id") Long bookId,
            @ShellOption(value = "--field", help = "Field name") String fieldName,
            @ShellOption(value = "--value", help = "New Value") String fieldValue) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                bookService.updateBookField(bookId, fieldName, fieldValue)));
    }


    @ShellMethod(value = "Associate an existed author with a book", key = {"book-add-author"})
    public void bookAddAuthor(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--author", help = "An author name") String authorName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(
                bookFormatter.formatToString(bookService.addAuthorToBook(bookId, authorName)));
    }

    @ShellMethod(value = "Dissociate an existed author with a book", key = {"book-remove-author"})
    public void bookRemoveAuthor(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--author", help = "An author name") String authorName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(
                bookFormatter.formatToString(bookService.removeAuthorFromBook(bookId, authorName)));
    }

    @ShellMethod(value = "Associate an existed genre with a book", key = {"book-add-genre"})
    public void bookAddGenre(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--genre", help = "An genre name") String genreName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                bookService.addGenreToBook(bookId, genreName)));
    }

    @ShellMethod(value = "Dissociate an existed genre with a book", key = {"book-remove-genre"})
    public void bookRemoveGenre(
            @ShellOption(value = "--book-id", help = "A book identifier") Long bookId,
            @ShellOption(value = "--genre", help = "A genre name") String genreName) {
        stringIOService.writeln("Book updated");
        stringIOService.writeln(bookFormatter.formatToString(
                bookService.removeGenreFromBook(bookId, genreName)));
    }

    @ShellMethod(value = "Delete the book by id", key = {"book-delete"})
    public void deleteBook(@ShellOption long id) {
        bookService.deleteById(id);
        stringIOService.writeln("The book deleted");
    }

    @ShellMethod(value = "Add a new comment for a book", key = {"book-add-comment"})
    public void bookAddComment(
            @ShellOption(value = "--bookId", help = "A book identifier") long bookId,
            @ShellOption(value = "--text", help = "Some comment") String commentText) {
        commentService.create(new Comment(bookId, commentText));
    }

    @ShellMethod(value = "Remove a comment", key = {"book-remove-comment"})
    public void bookRemoveComment(@ShellOption(value = "--id", help = "A comment identifier") long commentId) {
        commentService.deleteById(commentId);
    }

    @ShellMethod(value = "Modify a comment for a book", key = {"book-modify-comment"})
    public void bookModifyComment(
            @ShellOption(value = "--id", help = "A comment identifier") long id,
            @ShellOption(value = "--text", help = "A new comment text") String commentText) {
        commentService.getById(id)
                .ifPresent(c -> {
                    c.setText(commentText);
                    commentService.update(c);
                });
    }

    private Set<Genre> parseGenresNames(String genres) {
        return Splitter.on(",")
                .splitToStream(genres)
                .map(Genre::new)
                .collect(Collectors.toSet());
    }

    private Set<Author> parseAuthorsFullNames(String authors) {
        return Splitter.on(",")
                .splitToStream(authors)
                .map(Author::new)
                .collect(Collectors.toSet());
    }

    private void outputBookComments(BookDto b) {
        stringIOService.writeln("---- Book comments -----");
        b.getComments().stream()
                .sorted(Comparator.comparingLong(Comment::getId))
                .forEach(comment -> stringIOService.writeln(
                        String.format("Comment: id=%d, text=%s", comment.getId(), comment.getText())));
    }

}