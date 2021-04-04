package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
        return genreDao.save(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> getById(long genreId) {
        return genreDao.findById(genreId);
    }

    @Override
    public Genre update(Genre genre) {
        return genreDao.save(genre);
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

    @Override
    public List<Genre> findAll() {
        return StreamSupport.stream(genreDao.findAll()
                .spliterator(), false)
                .collect(Collectors.toList());
    }
}
