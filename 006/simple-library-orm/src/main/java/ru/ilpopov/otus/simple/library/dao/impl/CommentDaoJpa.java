package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Comment;

@RequiredArgsConstructor
@Repository
public class CommentDaoJpa implements CommentDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Comment create(Comment comment) {
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
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.bookId in (:bookId)",
                Comment.class);
        query.setParameter("bookId", List.of(bookId));
        return query.getResultList();
    }
}
