package ru.ilpopov.otus.simple.library.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.ilpopov.otus.simple.library.config.MongoConfig;
import ru.ilpopov.otus.simple.library.repository.BookRepository;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.Genre;

@DisplayName("Тестирование DAO слоя по работе с книгами")
@DataMongoTest
@Import(value = {MongoConfig.class})
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("Найдет книги по имени и запросит связанных с ней авторов и жанры")
    @Test
    void findByTitle() {
        List<Book> books = bookRepository.findByTitleContaining("Книга");
        assertThat(books)
                .flatExtracting(Book::getTitle)
                .containsOnly("Книга 1", "Книга 2", "Книга 3", "Книга 4");

        assertThat(books)
                .flatExtracting(Book::getAuthors)
                .extracting(Author::getFullName)
                .containsOnly("Автор 1", "Автор 2", "Автор 3", "Автор 4");

        assertThat(books)
                .flatExtracting(Book::getGenres)
                .extracting(Genre::getName)
                .containsOnly("Жанр 1", "Жанр 2", "Жанр 3", "Жанр 4");
    }

    @DisplayName("Найдет книги по имени ее автора и не будет запрашивать авторов и жанры")
    @Test
    void findBookByAuthorFullNameWithoutRelatedEntities() {
        List<Book> books = bookRepository.findByAuthors_fullName("Автор 1");
        assertThat(books)
                .flatExtracting(Book::getTitle)
                .containsOnly("Книга 1", "Книга 2", "Книга 3");
    }

    @DisplayName("Найдет книги по названию жанра и запросит жанры найденых книг")
    @Test
    void findBooksByGenreNameWithGenreList() {
        List<Book> books = bookRepository.findByGenres_name("Жанр 1");
        assertThat(books)
                .flatExtracting(Book::getTitle)
                .containsOnly("Книга 1", "Книга 2", "Книга 3");

        assertThat(books)
                .hasSize(3)
                .flatExtracting(Book::getGenres)
                .extracting(Genre::getName)
                .containsOnly("Жанр 1", "Жанр 2", "Жанр 3");
    }

    @DisplayName("Удалит книгу и связанные с ней комментарии")
    @Test
    void testBookDeleteById() {
        Book book = bookRepository.findByTitleContaining("Удалить по ID 1 с комментариями").stream().findFirst().orElseThrow();
        bookRepository.deleteById(book.getId(), true);

        Query query = new Query();
        query.addCriteria(Criteria.where("book").is(book.getId()));
        List<Comment> comments = mongoTemplate.find(query, Comment.class);

        assertThat(comments).isEmpty();
    }

    @DisplayName("Удалит книгу и связанные с ней комментарии")
    @Test
    void testBookDelete() {
        Book book = bookRepository.findByTitleContaining("Удалит с комментариями").stream().findFirst().orElseThrow();
        bookRepository.delete(book, true);

        Query query = new Query();
        query.addCriteria(Criteria.where("book").is(book.getId()));
        List<Comment> comments = mongoTemplate.find(query, Comment.class);

        assertThat(comments).isEmpty();
    }
}