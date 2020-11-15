package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Condition;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;

@DisplayName("Тестирование DAO слоя по работе с книгами")
@DataJpaTest
@Import({BookDaoJpa.class, AuthorDaoJpa.class, GenreDaoJpa.class})
class BookDaoJpaTest {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    public void beforeEach() {
        SessionFactory sessionFactory = em.getEntityManager()
                .getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().clear();
    }

    @DisplayName("Исключение при создании книги с автором, который еще не создан")
    @Test
    void createBookWithAuthorWhoDoesNotExists() {
        Book newBook = new Book("Test creation");
        newBook.getAuthors().add(new Author("NotExisted"));

        assertThatThrownBy(() -> bookDao.create(newBook))
                .isInstanceOf(BookCreationException.class)
                .hasMessage("The author 'NotExisted' must be existed before the book");
    }

    @DisplayName("Исключение при создании книги с жанром, который еще не создан")
    @Test
    void createBookWithGenreThatDoesNotExists() {
        Book newBook = new Book("Test creation");
        newBook.getGenres().add(new Genre("NotExisted"));

        assertThatThrownBy(() -> bookDao.create(newBook))
                .isInstanceOf(BookCreationException.class)
                .hasMessage("The genre 'NotExisted' must be existed before the book");
    }

    @DisplayName("Создаст книгу с 2 авторами и 2 жанрами и вернет ее")
    @Test
    void createBook() {
        Book newBook = new Book("Test creation");

        newBook.getAuthors().addAll(Lists.newArrayList(new Author(1L, "Автор 1"), new Author(2L, "Автор 2")));
        newBook.getGenres().addAll(Lists.newArrayList(new Genre(1L, "Жанр 1"), new Genre(2L, "Жанр 2")));

        Book createdBook = bookDao.create(newBook);

        assertThat(createdBook)
                .extracting(Book::getTitle)
                .isEqualTo("Test creation");
        assertThat(createdBook.getAuthors())
                .extracting(Author::getFullName)
                .containsOnlyOnce("Автор 1", "Автор 2");
        assertThat(createdBook.getGenres())
                .extracting(Genre::getName)
                .containsOnlyOnce("Жанр 1", "Жанр 2");
    }

    @DisplayName("Создаст книгу с 2 авторами и 2 жанрами и вернет ее")
    @Test
    void createBookResolveAuthorByName() {
        Book newBook = new Book("Test creation");
        newBook.getAuthors().add(new Author("Автор 1"));

        Book createdBook = bookDao.create(newBook);

        assertThat(createdBook)
                .matches(b -> b.getId() != null)
                .extracting(Book::getTitle)
                .isEqualTo("Test creation");

        Condition<Author> conditionAuthorIdNotNull = new Condition<>(a -> a.getId() != null, "Id is empty");
        assertThat(createdBook.getAuthors())
                .have(conditionAuthorIdNotNull)
                .extracting(Author::getFullName)
                .containsExactly("Автор 1");
    }

    @DisplayName("Создаст книгу без авторов и жанров и вернет ее")
    @Test
    void createBookWithoutAuthorsAndGenres() {
        Book newBook = new Book("Test creation");
        Book createdBook = bookDao.create(newBook);
        assertThat(createdBook)
                .matches(b -> b.getId() != null)
                .extracting(Book::getTitle)
                .isEqualTo("Test creation");
        assertThat(createdBook.getAuthors())
                .isEmpty();
        assertThat(createdBook.getGenres())
                .isEmpty();
    }

    @DisplayName("Получит одну тестовую книгу с 3мя авторами и 3мя жанрами")
    @Test
    void getBook() {
        Book book = bookDao.getById(1L).orElseThrow();
        assertThat(book)
                .extracting(Book::getId)
                .isEqualTo(1L);
        assertThat(book.getAuthors())
                .extracting(Author::getFullName)
                .asList()
                .containsOnlyOnce("Автор 1", "Автор 2", "Автор 3");
        assertThat(book.getGenres())
                .extracting(Genre::getName)
                .asList()
                .containsOnlyOnce("Жанр 1", "Жанр 2", "Жанр 3");
    }

    @DisplayName("Получит одну тестовую книгу без авторов и жанров")
    @Test
    void getBookWithoutAuthorsAndGenres() {
        Book book = bookDao.getById(2L).orElseThrow();
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

        createdBook.setTitle("Test Updated");

        assertThat(bookDao.update(createdBook))
                .extracting(Book::getTitle)
                .isEqualTo("Test Updated");
    }

    @DisplayName("Удалит одного старого автора из книги и добавит одного нового")
    @Test
    void updateBookChangeAuthors() {
        Book book = bookDao.findByTitle("Книга 6").get(0);
        book.getAuthors().removeIf(a -> "Автор 1".equalsIgnoreCase(a.getFullName())
                || "Автор 2".equalsIgnoreCase(a.getFullName())
                || "Автор 5".equalsIgnoreCase(a.getFullName()));
        book.getAuthors().addAll(Set.of(new Author("Автор 3"), new Author("Удалить")));

        Book updatedBook = bookDao.update(book);

        assertThat(updatedBook.getAuthors())
                .flatExtracting(Author::getFullName)
                .containsOnly("Автор 3", "Удалить");
    }

    @DisplayName("Удалит один старый жанр из книги и добавит один новый")
    @Test
    void updateBookChangeGenres() {
        Book book = bookDao.findByTitle("Книга 6").get(0);
        book.getGenres().removeIf(g -> "Жанр 1".equalsIgnoreCase(g.getName()));
        book.getGenres().add(new Genre("Жанр 5"));

        Book updatedBook = bookDao.update(book);

        assertThat(updatedBook.getGenres())
                .flatExtracting(Genre::getName)
                .containsOnly("Жанр 5", "Жанр 3", "Жанр 2");
    }

    @DisplayName("Удалит книгу и ее связи с авторами и жанрами из БД")
    @Test
    void deleteBook() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        bookDao.deleteById(5L);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(3);
    }

    @DisplayName("Найдет книги по имени и запросит связанных с ней авторов и жанры")
    @Test
    void findByName() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Book> books = bookDao.findByTitle("Книга");
        assertThat(books)
                .flatExtracting(Book::getTitle)
                .containsOnly("Книга 1", "Книга 2", "Книга 3", "Книга 4", "Книга 6");

        assertThat(books)
                .flatExtracting(Book::getAuthors)
                .extracting(Author::getFullName)
                .containsOnly("Автор 1", "Автор 2", "Автор 3", "Автор 5");

        assertThat(books)
                .flatExtracting(Book::getGenres)
                .extracting(Genre::getName)
                .containsOnly("Жанр 1", "Жанр 2", "Жанр 3");

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(3);
    }

    @DisplayName("Найдет книги по имени ее автора и не будет запрашивать авторов и жанры")
    @Test
    void findBookByAuthorFullNameWithoutRelatedEntities() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Book> books = bookDao.findByAuthorFullName("Автор 1");
        assertThat(books)
                .flatExtracting(Book::getTitle)
                .containsOnly("Книга 1", "Книга 3", "Удалить", "Книга 6");

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(1);
    }

    @DisplayName("Найдет книги по названию жанра и запросит жанры найденых книг")
    @Test
    void findBooksByGenreNameWithGenreList() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Book> books = bookDao.findByGenreName("Жанр 1");
        assertThat(books)
                .flatExtracting(Book::getTitle)
                .containsOnly("Книга 1", "Книга 3", "Книга 4", "Книга 6", "Удалить");

        assertThat(books)
                .hasSize(5)
                .flatExtracting(Book::getGenres)
                .extracting(Genre::getName)
                .containsOnly("Жанр 1", "Жанр 2", "Жанр 3");

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }
}