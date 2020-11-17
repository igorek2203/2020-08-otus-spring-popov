package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.BookCreationException;

@RequiredArgsConstructor
@Repository
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private final EntityManager em;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public Book create(Book book) {
        resolveIds(book);
        em.persist(book);
        return book;
    }

    @Override
    public Optional<Book> getById(long bookId) {
        return Optional.ofNullable(em.find(Book.class, bookId));
    }

    @Override
    public Book update(Book book) {
        resolveIds(book);
        return em.merge(book);
    }

    @Override
    public void deleteById(long bookId) {
        Query query = em.createQuery("delete " +
                "from Book b " +
                "where b.id = :id");
        query.setParameter("id", bookId);
        query.executeUpdate();
    }

    @Override
    public List<Book> findByTitle(String title) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b " +
                        "where b.title like :title",
                Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }


    @Override
    public List<Book> findByAuthorFullName(String fullName) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b join b.authors a " +
                        "where a.fullName = :name",
                Book.class);
        query.setParameter("name", fullName);
        return query.getResultList();
    }

    @Override
    public List<Book> findByGenreName(String genreName) {
        TypedQuery<Book> query = em.createQuery("select b " +
                        "from Book b join b.genres g " +
                        "where g.name = :genreName",
                Book.class);
        query.setParameter("genreName", genreName);
        return query.getResultList();
    }

    private void resolveIds(Book book) {
        Set<Author> authors = book.getAuthors()
                .stream()
                .map(a -> a.getId() != null ? a : findAuthorByName(a.getFullName()))
                .collect(Collectors.toSet());

        Set<Genre> genres = book.getGenres()
                .stream()
                .map(g -> g.getId() != null ? g : findGenreByName(g.getName()))
                .collect(Collectors.toSet());

        book.setAuthors(authors);
        book.setGenres(genres);
    }

    private Author findAuthorByName(String fullName) {
        return authorDao.findByFullName(fullName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookCreationException(
                        String.format("The author '%s' does not exists", fullName)));
    }

    private Genre findGenreByName(String name) {
        return genreDao.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookCreationException(
                        String.format("The genre '%s' does not exists", name)));
    }
}
