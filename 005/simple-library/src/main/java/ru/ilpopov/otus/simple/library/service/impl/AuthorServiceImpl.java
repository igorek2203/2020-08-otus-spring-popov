package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.service.AuthorService;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public Author create(Author author) {
        return authorDao.create(author);
    }

    @Override
    public Optional<Author> get(long id) {
        return authorDao.getOptional(id);
    }

    @Override
    public Author update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public void delete(long id) {
        authorDao.deleteById(id);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorDao.findByFullName(name);
    }
}
