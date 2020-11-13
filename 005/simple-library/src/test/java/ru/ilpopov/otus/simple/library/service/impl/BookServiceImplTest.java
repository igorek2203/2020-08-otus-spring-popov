package ru.ilpopov.otus.simple.library.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.service.BookService;

@DisplayName("Тестирование сервиса по работе с книгами")
@SpringBootTest(classes = {BookServiceImpl.class})
@Import(ValidationAutoConfiguration.class)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @MockBean
    private BookDao dao;

    @BeforeEach
    public void beforeEach() {
        given(dao.create(any(Book.class)))
                .will((invocationOnMock) -> {
                    Book input = invocationOnMock.getArgument(0);
                    return new Book(1L, input.getTitle(), input.getDescription());
                });

        given(dao.update(any(Book.class)))
                .will((invocationOnMock) -> {
                    Book input = invocationOnMock.getArgument(0);
                    return new Book(1L, input.getTitle(), input.getDescription());
                });
    }

    @DisplayName("Создаст новую книгу")
    @Test
    void createBookSuccess() {
        Book book = new Book("Test create");
        assertThat(service.create(book))
                .extracting(Book::getTitle)
                .isEqualTo("Test create");

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(dao, only()).create(bookArgumentCaptor.capture());

        assertThat(bookArgumentCaptor.getValue())
                .isEqualTo(book);
    }

    @DisplayName("Исключении при попытке создать книгу без имени")
    @Test
    void createBookNameConstraintViolationException() {
        Book book = new Book("Test create");
        book.setTitle(null);
        assertThatThrownBy(() -> service.create(book))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("create.book.title: name must be set");
        verify(dao, never()).create(book);
    }

    @DisplayName("Вернет одну книгу по заданному идентификатору")
    @Test
    void getByIdSuccess() {
        given(dao.getById(1L))
                .willReturn(Optional.of(new Book(1L, "Test get")));

        assertThat(service.getById(1L))
                .isNotEmpty()
                .get()
                .extracting(Book::getTitle)
                .isEqualTo("Test get");

        verify(dao, only()).getById(1L);
    }

    @DisplayName("Вернет пустой Optional т.к. книга с заданным идентификатором не создана")
    @Test
    void getByIdEmptyOptional() {
        assertThat(service.getById(1L))
                .isEmpty();

        verify(dao, only()).getById(1L);
    }

    @DisplayName("Изменит книгу и вернет ее")
    @Test
    void updateSuccess() {
        Book book = new Book(1L, "Test update");

        assertThat(service.update(book))
                .extracting(Book::getTitle)
                .isEqualTo("Test update");

        verify(dao, only()).update(book);
    }

    @DisplayName("Исключение при попытке обновить книгу без ее идентификатора")
    @Test
    void updateIdConstraintValidationException() {
        Book book = new Book("Test update");

        assertThatThrownBy(() -> service.update(book))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("update.book.id: id must be set");

        verify(dao, never()).update(book);
    }

    @DisplayName("Удалит книгу")
    @Test
    void delete() {
        service.deleteById(1L);

        verify(dao, only()).deleteById(1L);
    }

    @DisplayName("Найдет книги по заданному имени")
    @Test
    void findByName() {
        given(dao.findByTitle(anyString()))
                .will((invocationOnMock) -> {
                    String name = invocationOnMock.getArgument(0);
                    return List.of(new Book(1L, name),
                            new Book(2L, name));
                });

        assertThat(service.findByTitle("test search"))
                .hasSize(2)
                .flatExtracting(Book::getTitle)
                .containsOnly("test search");

        verify(dao, only()).findByTitle("test search");
    }

    @DisplayName("Найдет книги по имени автора")
    @Test
    void findByAuthorName() {
        given(dao.findByAuthorFullName(anyString()))
                .will((invocationOnMock) -> {
                    String name = invocationOnMock.getArgument(0);
                    return List.of(new Book(1L, name),
                            new Book(2L, name));
                });

        assertThat(service.findByAuthorFullName("test search"))
                .hasSize(2)
                .flatExtracting(Book::getTitle)
                .containsOnly("test search");

        verify(dao, only()).findByAuthorFullName("test search");
    }

    @DisplayName("Найдет книги по названию жанра")
    @Test
    void findByGenreName() {
        given(dao.findByGenreName(anyString()))
                .will((invocationOnMock) -> {
                    String name = invocationOnMock.getArgument(0);
                    return List.of(new Book(1L, name),
                            new Book(2L, name));
                });

        assertThat(service.findByGenreName("test search"))
                .hasSize(2)
                .flatExtracting(Book::getTitle)
                .containsOnly("test search");

        verify(dao, only()).findByGenreName("test search");
    }
}