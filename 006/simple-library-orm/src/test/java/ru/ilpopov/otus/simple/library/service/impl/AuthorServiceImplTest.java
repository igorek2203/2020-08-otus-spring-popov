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
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.service.AuthorService;

@DisplayName("Тестирование сервиса по работе с авторами")
@SpringBootTest(classes = {AuthorServiceImpl.class})
@Import(ValidationAutoConfiguration.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService service;

    @MockBean
    private AuthorDao dao;

    @BeforeEach
    public void beforeEach() {
        given(dao.create(any(Author.class)))
                .will((invocationOnMock) -> {
                    Author input = invocationOnMock.getArgument(0);
                    return new Author(1L, input.getFullName(), input.getDescription());
                });

        given(dao.update(any(Author.class)))
                .will((invocationOnMock) -> {
                    Author input = invocationOnMock.getArgument(0);
                    return new Author(1L, input.getFullName(), input.getDescription());
                });
    }

    @DisplayName("Создаст нового автора")
    @Test
    void createAuthorSuccess() {
        Author author = new Author("Test create");

        assertThat(service.create(author))
                .extracting(Author::getFullName)
                .isEqualTo("Test create");

        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(dao, only()).create(authorArgumentCaptor.capture());

        assertThat(authorArgumentCaptor.getValue())
                .isEqualTo(author);
    }

    @DisplayName("Исключение при попытке создать автора без имени")
    @Test
    void createAuthorNameConstraintValidationException() {
        Author author = new Author("Test create");
        author.setFullName(null);

        assertThatThrownBy(() -> service.create(author))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("create.author.fullName: name must be set");

        verify(dao, never()).create(author);
    }

    @DisplayName("Вернет одного автора по заданному идентификатору")
    @Test
    void getByIdSuccess() {
        given(dao.getById(1L))
                .willReturn(Optional.of(new Author(1L, "Test get")));

        assertThat(service.getById(1L))
                .isNotEmpty()
                .get()
                .extracting(Author::getFullName)
                .isEqualTo("Test get");

        verify(dao, only()).getById(1L);
    }

    @DisplayName("Вернет пустой Optional т.к. автор с заданным идентификатором не создан")
    @Test
    void getByIdEmptyOptional() {
        assertThat(service.getById(1L))
                .isEmpty();

        verify(dao, only()).getById(1L);
    }

    @DisplayName("Изменит автора и вернет его")
    @Test
    void updateSuccess() {
        Author author = new Author(1L, "Test update");

        assertThat(service.update(author))
                .extracting(Author::getFullName)
                .isEqualTo("Test update");

        verify(dao, only()).update(author);
    }

    @DisplayName("Исключение при попытке обновить автора без его идентификатора")
    @Test
    void updateIdConstraintValidationException() {
        Author author = new Author("Test update");

        assertThatThrownBy(() -> service.update(author))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("update.author.id: id must be set");

        verify(dao, never()).update(author);
    }

    @DisplayName("Удалит автора")
    @Test
    void deleteAuthor() {
        service.deleteById(1L);

        verify(dao, only()).deleteById(1L);
    }

    @DisplayName("Найдет авторов по имени")
    @Test
    void findByName() {
        given(dao.findByFullName(anyString()))
                .will((invocationOnMock) -> {
                    String name = invocationOnMock.getArgument(0);
                    return List.of(new Author(1L, name),
                            new Author(2L, name));
                });

        assertThat(service.findByFullName("test search"))
                .hasSize(2)
                .flatExtracting(Author::getFullName)
                .containsOnly("test search");

        verify(dao, only()).findByFullName("test search");
    }
}