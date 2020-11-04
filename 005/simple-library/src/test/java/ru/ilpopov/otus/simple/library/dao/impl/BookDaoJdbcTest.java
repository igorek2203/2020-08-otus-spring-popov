package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;

@DisplayName("Тестирование DAO слоя по работе с книгами")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @DisplayName("Исключение при создании книги с автором, который еще не создан")
    @Test
    void createBookWithAuthorWhoDoesNotExists() {
        Book newBook = new Book("Test creation");
        newBook.setAuthors(List.of(new Author("NotExisted")));

        assertThatThrownBy(() -> bookDao.create(newBook))
                .isInstanceOf(BookCreationException.class)
                .hasMessage("The author 'NotExisted' must be existed before the book");
    }

    @DisplayName("Исключение при создании книги с жанром, который еще не создан")
    @Test
    void createBookWithGenreThatDoesNotExists() {
        Book newBook = new Book("Test creation");
        newBook.setGenres(List.of(new Genre("NotExisted")));

        assertThatThrownBy(() -> bookDao.create(newBook))
                .isInstanceOf(BookCreationException.class)
                .hasMessage("The genre 'NotExisted' must be existed before the book");
    }

    @DisplayName("Создаст книгу с 2 авторами и 2 жанрами и вернет ее")
    @Test
    void createBook() {
        Book newBook = new Book("Test creation");
        newBook.setAuthors(List.of(new Author("Автор 1"), new Author("Автор 2")));
        newBook.setGenres(List.of(new Genre("Жанр 1"), new Genre("Жанр 2")));

        Book createdBook = bookDao.create(newBook);

        assertThat(createdBook)
                .extracting(Book::getName)
                .isEqualTo("Test creation");
        assertThat(createdBook.getAuthors())
                .extracting(Author::getName)
                .containsExactly("Автор 1", "Автор 2");
        assertThat(createdBook.getGenres())
                .extracting(Genre::getName)
                .containsExactly("Жанр 1", "Жанр 2");
    }

    @DisplayName("Создаст книгу без авторов и жанров и вернет ее")
    @Test
    void createBookWithoutAuthorsAndGenres() {
        Book newBook = new Book("Test creation");
        Book createdBook = bookDao.create(newBook);
        assertThat(createdBook)
                .extracting(Book::getName)
                .isEqualTo("Test creation");
        assertThat(createdBook.getAuthors())
                .isEmpty();
        assertThat(createdBook.getGenres())
                .isEmpty();
    }

    @DisplayName("Получит одну тестовую книгу с 3мя авторами и 3мя жанрами")
    @Test
    void getBook() {
        Book book = bookDao.get(1L).orElseThrow();
        assertThat(book)
                .extracting(Book::getId)
                .isEqualTo(1L);
        assertThat(book.getAuthors())
                .extracting(Author::getName)
                .asList()
                .containsExactly("Автор 1", "Автор 2", "Автор 3");
        assertThat(book.getGenres())
                .extracting(Genre::getName)
                .asList()
                .containsExactly("Жанр 1", "Жанр 2", "Жанр 3");
    }

    @DisplayName("Получит одну тестовую книгу без авторов и жанров")
    @Test
    void getBookWithoutAuthorsAndGenres() {
        Book book = bookDao.get(2L).orElseThrow();
        assertThat(book)
                .extracting(Book::getId)
                .isEqualTo(2L);
        assertThat(book.getAuthors())
                .isEmpty();
        assertThat(book.getGenres())
                .isEmpty();
    }

    @DisplayName("Изменит имя книги")
    @Test
    void updateBookChangeName() {
        Book newBook = new Book("Test update");
        Book createdBook = bookDao.create(newBook);

        createdBook.setName("Test Updated");

        assertThat(bookDao.update(createdBook))
                .extracting(Book::getName)
                .isEqualTo("Test Updated");
    }

    @DisplayName("Удалит одного старого автора из книги и добавит одного нового")
    @Test
    void updateBookChangeAuthors() {
        Book newBook = new Book("Test update");
        newBook.setAuthors(List.of(new Author("Автор 1"), new Author("Автор 2")));
        Book createdBook = bookDao.create(newBook);

        createdBook.setName("Test Updated");
        createdBook.setAuthors(List.of(new Author("Автор 3"), new Author("Автор 2")));

        Book updatedBook = bookDao.update(createdBook);

        assertThat(updatedBook)
                .extracting(Book::getName)
                .isEqualTo("Test Updated");

        assertThat(updatedBook.getAuthors())
                .flatExtracting(Author::getName)
                .containsOnly("Автор 3", "Автор 2");
    }

    @DisplayName("Удалит один старый жанр из книги и добавит один новый")
    @Test
    void updateBookChangeGenres() {
        Book newBook = new Book("Test update");
        newBook.setGenres(List.of(new Genre("Жанр 1"), new Genre("Жанр 2")));
        Book createdBook = bookDao.create(newBook);

        createdBook.setName("Test Updated");
        createdBook.setGenres(List.of(new Genre("Жанр 3"), new Genre("Жанр 2")));

        Book updatedBook = bookDao.update(createdBook);

        assertThat(updatedBook)
                .extracting(Book::getName)
                .isEqualTo("Test Updated");

        assertThat(updatedBook.getGenres())
                .flatExtracting(Genre::getName)
                .containsOnly("Жанр 3", "Жанр 2");
    }

    @DisplayName("Удалит книгу и ее связи с авторами и жанрами из БД")
    @Test
    void deleteBook() {
        Book newBook = new Book("Test deletion");
        newBook.setAuthors(List.of(new Author("Автор 1"), new Author("Автор 2")));
        newBook.setGenres(List.of(new Genre("Жанр 1"), new Genre("Жанр 2")));

        Book createdBook = bookDao.create(newBook);

        bookDao.delete(createdBook.getId());

        Long genresCount = jdbc.queryForObject(
                "SELECT COUNT(r.*) FROM BOOKS_GENRES_RELATIONS r WHERE r.BOOK_ID = :id",
                Map.of("id", createdBook.getId()),
                Long.class);

        Long authorsCount = jdbc.queryForObject(
                "SELECT COUNT(r.*) FROM BOOKS_AUTHORS_RELATIONS r WHERE r.BOOK_ID = :id",
                Map.of("id", createdBook.getId()),
                Long.class);

        Long booksCount = jdbc.queryForObject(
                "SELECT COUNT(b.*) FROM BOOKS b WHERE b.ID = :id",
                Map.of("id", createdBook.getId()),
                Long.class);

        assertThat(booksCount).isEqualTo(0);
        assertThat(genresCount).isEqualTo(0);
        assertThat(authorsCount).isEqualTo(0);
    }

    @DisplayName("Найдет книги по имени")
    @Test
    void findByName() {
        List<Book> books = bookDao.findByName("Книга 1");
        assertThat(books)
                .flatExtracting(Book::getName)
                .contains("Книга 1");
    }

    @DisplayName("Найдет книги по имени ее автора")
    @Test
    void findByAuthorName() {
        List<Book> books = bookDao.findByAuthorName("Автор 1");
        assertThat(books)
                .flatExtracting(Book::getName)
                .contains("Книга 1");
    }

    @DisplayName("Найдет книги по названию жанра")
    @Test
    void findByGenreName() {
        List<Book> books = bookDao.findByGenreName("Жанр 1");
        assertThat(books)
                .flatExtracting(Book::getName)
                .contains("Книга 1");
    }
}