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
import ru.ilpopov.otus.simple.library.domain.Author;

@RequiredArgsConstructor
@Repository
public class AuthorDaoJpa implements AuthorDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Author create(Author author) {
        em.persist(author);
        return author;
    }

    @Override
    public Optional<Author> getById(long authorId) {
        return Optional.ofNullable(em.find(Author.class, authorId));
    }

    @Override
    public Author update(Author author) {
        return em.merge(author);
    }

    @Override
    public void deleteById(long authorId) {
        Query query = em.createQuery("delete " +
                "from Author a " +
                "where a.id = :id");
        query.setParameter("id", authorId);
        query.executeUpdate();
    }

    @Override
    public List<Author> findByFullName(String fullName) {
        TypedQuery<Author> query = em.createQuery("select a " +
                        "from Author a " +
                        "where a.fullName = :name",
                Author.class);
        query.setParameter("name", fullName);
        return query.getResultList();
    }
}
