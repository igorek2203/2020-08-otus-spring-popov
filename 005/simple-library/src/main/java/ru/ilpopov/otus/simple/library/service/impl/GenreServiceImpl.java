package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.service.CrudService;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements CrudService<Genre> {

    private final GenreDao genreDao;

    @Override
    public Genre create(Genre genre) {
        return genreDao.create(genre);
    }

    @Override
    public Optional<Genre> get(long id) {
        return genreDao.get(id);
    }

    @Override
    public Genre update(Genre genre) {
        return genreDao.update(genre);
    }

    @Override
    public void delete(long id) {
        genreDao.delete(id);
    }

    @Override
    public List<Genre> findByName(String name) {
        return genreDao.findByName(name);
    }
}
