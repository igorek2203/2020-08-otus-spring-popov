package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;

@RequiredArgsConstructor
@Repository
public class CommentDaoJpa implements CommentDao {

    @PersistenceContext
    private final EntityManager em;

    private final BookDao bookDao;

    @Override
    public Comment create(Comment comment) {
        resolveBookId(comment);
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> getById(long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    @Override
    public Comment update(Comment comment) {
        return em.merge(comment);
    }

    @Override
    public void deleteById(long commentId) {
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", commentId);
        query.executeUpdate();
    }

    @Override
    public List<Comment> findByComment(String text) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.text like :text",
                Comment.class);
        query.setParameter("text", "%" + text + "%");
        return query.getResultList();
    }

    @Override
    public List<Comment> findByBookId(Long... bookId) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c join c.book b where b.id in (:bookId)",
                Comment.class);
        query.setParameter("bookId", List.of(bookId));
        return query.getResultList();
    }

    private void resolveBookId(Comment comment) {
        Optional.of(comment.getBook())
                .filter(b -> b.getId() == null)
                .ifPresent(book -> comment.setBook(findBookByName(book.getTitle())));
    }

    private Book findBookByName(String bookTitle) {
        return bookDao.findByTitle(bookTitle)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFound(String.format("The book '%s' does not exists", bookTitle)));
    }
}
