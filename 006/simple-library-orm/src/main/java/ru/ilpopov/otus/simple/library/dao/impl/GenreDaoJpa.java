package ru.ilpopov.otus.simple.library.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;

@RequiredArgsConstructor
@Repository
public class GenreDaoJpa implements GenreDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Genre create(Genre genre) {
        em.persist(genre);
        return genre;
    }

    @Override
    public Optional<Genre> getById(long genreId) {
        return Optional.ofNullable(em.find(Genre.class, genreId));
    }

    @Override
    public Genre update(Genre genre) {
        return em.merge(genre);
    }

    @Override
    public void deleteById(long genreId) {
        Query query = em.createQuery("delete " +
                "from Genre g " +
                "where g.id = :id");
        query.setParameter("id", genreId);
        query.executeUpdate();
    }

    @Override
    public List<Genre> findByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g " +
                        "from Genre g " +
                        "where g.name = :name",
                Genre.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

}
