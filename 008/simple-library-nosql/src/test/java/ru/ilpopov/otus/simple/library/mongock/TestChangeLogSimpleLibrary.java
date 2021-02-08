package ru.ilpopov.otus.simple.library.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import java.util.Set;
import ru.ilpopov.otus.simple.library.repository.BookRepository;
import ru.ilpopov.otus.simple.library.repository.CommentRepository;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.Genre;

@ChangeLog
public class TestChangeLogSimpleLibrary {

    private final Author authorOne = new Author("Автор 1", "Описание автора 1");
    private final Author authorTwo = new Author("Автор 2", "Описание автора 2");
    private final Author authorThree = new Author("Автор 3", "Описание автора 3");
    private final Author authorFour = new Author("Автор 4", "Описание автора 4");


    private final Genre genreOne = new Genre("Жанр 1");
    private final Genre genreTwo = new Genre("Жанр 2");
    private final Genre genreThree = new Genre("Жанр 3");
    private final Genre genreFour = new Genre("Жанр 4");


    @ChangeSet(order = "001", id = "dropDb", author = "ipopov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertBookWolfAndSevenGoatlings", author = "ipopov")
    public void insertBookOne(BookRepository repository) {
        Book book = new Book("Книга 1", "Описание к книге 1");
        book.setGenres(List.of(genreOne, genreTwo));
        book.setAuthors(List.of(authorOne, authorTwo));
        repository.save(book);
    }

    @ChangeSet(order = "003", id = "insertBookTwo", author = "ipopov")
    public void insertBookTwo(BookRepository repository) {
        Book book = new Book("Книга 2", "Описание к книге 2");
        book.setGenres(List.of(genreOne));
        book.setAuthors(List.of(authorOne));
        repository.save(book);
    }

    @ChangeSet(order = "004", id = "insertBookThree", author = "ipopov")
    public void insertBookThree(BookRepository repository) {
        Book book = new Book("Книга 3", "Описание к книге 3");
        book.setGenres(List.of(genreThree, genreOne, genreTwo));
        book.setAuthors(List.of(authorOne, authorTwo, authorThree));
        repository.save(book);
    }

    @ChangeSet(order = "005", id = "insertBookFour", author = "ipopov")
    public void insertBookFour(BookRepository repository) {
        Book book = new Book("Книга 4", "Описание к книге 4");
        book.setGenres(List.of(genreFour));
        book.setAuthors(List.of(authorFour));
        repository.save(book);
    }

    @ChangeSet(order = "006", id = "insertComment1ForBookOne", author = "ipopov")
    public void insertComment1ForBookOne(CommentRepository repository, BookRepository bookRepository) {
        Book book = bookRepository.findByTitleContaining("Книга 1")
                .stream()
                .findFirst()
                .orElse(null);
        Comment comment = new Comment(book, "комментарий 1");
        repository.save(comment);
    }

    @ChangeSet(order = "007", id = "insertComment2ForBookOne", author = "ipopov")
    public void insertComment2ForBookOne(CommentRepository repository, BookRepository bookRepository) {
        Book book = bookRepository.findByTitleContaining("Книга 1")
                .stream()
                .findFirst()
                .orElse(null);
        Comment comment = new Comment(book, "комментарий 2");
        repository.save(comment);
    }

    @ChangeSet(order = "008", id = "insertComment3ForBookOne", author = "ipopov")
    public void insertComment3ForBookOne(CommentRepository repository, BookRepository bookRepository) {
        Book book = bookRepository.findByTitleContaining("Книга 1")
                .stream()
                .findFirst()
                .orElse(null);
        Comment comment = new Comment(book, "комментарий 3");
        repository.save(comment);
    }

    @ChangeSet(order = "009", id = "insertComment1ForBookTwo", author = "ipopov")
    public void insertComment1ForBookTwo(CommentRepository repository, BookRepository bookRepository) {
        Book book = bookRepository.findByTitleContaining("Книга 2")
                .stream()
                .findFirst()
                .orElse(null);
        Comment comment = new Comment(book, "комментарий к книге 2");
        repository.save(comment);
    }

    @ChangeSet(order = "010", id = "insertBookRemoveByIdWithComments", author = "ipopov")
    public void insertBookRemoveByIdWithComments(BookRepository bookRepository, CommentRepository commentRepository) {
        Book book = new Book("Удалить по ID 1 с комментариями", "Описание к книге");
        book = bookRepository.save(book);

        Comment commentOne = new Comment(book, "первый комментарий к книге 1, которую надо удалить");
        Comment commentTwo = new Comment(book, "второй комментарий к книге 1, которую надо удалить");
        commentRepository.saveAll(Set.of(commentOne, commentTwo));
    }

    @ChangeSet(order = "010", id = "insertBookRemoveWithComments", author = "ipopov")
    public void insertBookRemoveWithComments(BookRepository bookRepository, CommentRepository commentRepository) {
        Book book = new Book("Удалит с комментариями", "Описание к книге");
        book = bookRepository.save(book);

        Comment commentOne = new Comment(book, "первый комментарий к книге 1, которую надо удалить");
        Comment commentTwo = new Comment(book, "второй комментарий к книге 1, которую надо удалить");
        commentRepository.saveAll(Set.of(commentOne, commentTwo));
    }

}
