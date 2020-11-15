package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Optional;
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
        book.getAuthors()
                .stream()
                .filter(a -> a.getId() == null)
                .forEach(a -> a.setId(resolveAuthorId(a)));
        book.getGenres()
                .stream()
                .filter(g -> g.getId() == null)
                .forEach(g -> g.setId(resolveGenreId(g)));
    }

    private Long resolveAuthorId(Author author) {
        return Optional.ofNullable(author.getId())
                .orElseGet(() -> authorDao.findByFullName(author.getFullName())
                        .stream()
                        .mapToLong(Author::getId)
                        .findFirst()
                        .orElseThrow(() -> new BookCreationException(
                                String.format("The author '%s' must be existed before the book",
                                        author.getFullName()))));
    }

    private Long resolveGenreId(Genre genre) {
        return Optional.ofNullable(genre.getId())
                .orElseGet(() -> genreDao.findByName(genre.getName())
                        .stream()
                        .mapToLong(Genre::getId)
                        .findFirst()
                        .orElseThrow(() -> new BookCreationException(
                                String.format("The genre '%s' must be existed before the book", genre.getName()))));
    }
}
