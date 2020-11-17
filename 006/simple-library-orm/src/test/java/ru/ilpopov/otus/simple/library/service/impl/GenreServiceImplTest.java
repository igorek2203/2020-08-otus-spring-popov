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
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.service.GenreService;

@DisplayName("Тестирование сервиса по работе с жанрами")
@SpringBootTest(classes = {GenreServiceImpl.class})
@Import(ValidationAutoConfiguration.class)
class GenreServiceImplTest {

    @Autowired
    private GenreService service;

    @MockBean
    private GenreDao dao;

    @BeforeEach
    public void beforeEach() {
        given(dao.create(any(Genre.class)))
                .will((invocationOnMock) -> {
                    Genre input = invocationOnMock.getArgument(0);
                    return new Genre(1L, input.getName());
                });

        given(dao.update(any(Genre.class)))
                .will((invocationOnMock) -> {
                    Genre input = invocationOnMock.getArgument(0);
                    return new Genre(1L, input.getName());
                });
    }

    @DisplayName("Создаст новый жанр")
    @Test
    void createAuthorSuccess() {
        Genre genre = new Genre("Test create");

        assertThat(service.create(genre))
                .extracting(Genre::getName)
                .isEqualTo("Test create");

        ArgumentCaptor<Genre> genreArgumentCaptor = ArgumentCaptor.forClass(Genre.class);
        verify(dao, only()).create(genreArgumentCaptor.capture());

        assertThat(genreArgumentCaptor.getValue())
                .isEqualTo(genre);
    }

    @DisplayName("Исключение при попытке создать жанра без имени")
    @Test
    void createAuthorNameConstraintValidationException() {
        Genre genre = new Genre("Test create");
        genre.setName(null);

        assertThatThrownBy(() -> service.create(genre))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("create.genre.name: name must be set");

        verify(dao, never()).create(genre);
    }

    @DisplayName("Вернет однин жанр по заданному идентификатору")
    @Test
    void getByIdSuccess() {
        given(dao.getById(1L))
                .willReturn(Optional.of(new Genre(1L, "Test get")));

        assertThat(service.getById(1L))
                .isNotEmpty()
                .get()
                .extracting(Genre::getName)
                .isEqualTo("Test get");

        verify(dao, only()).getById(1L);
    }

    @DisplayName("Вернет пустой Optional т.к. жанр с заданным идентификатором не создан")
    @Test
    void getByIdEmptyOptional() {
        assertThat(service.getById(1L))
                .isEmpty();

        verify(dao, only()).getById(1L);
    }

    @DisplayName("Изменит жанр и вернет его")
    @Test
    void updateSuccess() {
        Genre genre = new Genre(1L, "Test update");

        assertThat(service.update(genre))
                .extracting(Genre::getName)
                .isEqualTo("Test update");

        verify(dao, only()).update(genre);
    }

    @DisplayName("Исключение при попытке обновить жанр без его идентификатора")
    @Test
    void updateIdConstraintValidationException() {
        Genre genre = new Genre("Test update");

        assertThatThrownBy(() -> service.update(genre))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("update.genre.id: id must be set");

        verify(dao, never()).update(genre);
    }

    @DisplayName("Удалит жанр")
    @Test
    void deleteAuthor() {
        service.deleteById(1L);

        verify(dao, only()).deleteById(1L);
    }

    @DisplayName("Найдет жанры по имени")
    @Test
    void findByName() {
        given(dao.findByName(anyString()))
                .will((invocationOnMock) -> {
                    String name = invocationOnMock.getArgument(0);
                    return List.of(new Genre(1L, name),
                            new Genre(2L, name));
                });

        assertThat(service.findByName("test search"))
                .hasSize(2)
                .flatExtracting(Genre::getName)
                .containsOnly("test search");

        verify(dao, only()).findByName("test search");
    }
}