package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilpopov.otus.simple.library.dao.GenreDao;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.service.GenreService;

@Transactional
@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    public Genre create(Genre genre) {
        return genreDao.create(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> getById(long genreId) {
        return genreDao.getById(genreId);
    }

    @Override
    public Genre update(Genre genre) {
        return genreDao.update(genre);
    }

    @Override
    public void deleteById(long genreId) {
        genreDao.deleteById(genreId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findByName(String name) {
        return genreDao.findByName(name);
    }
}
