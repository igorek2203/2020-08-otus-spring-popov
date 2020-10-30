package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.simple.library.dao.CrudDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.service.CrudService;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements CrudService<Author> {

    private final CrudDao<Author> authorDao;

    @Override
    public Author create(Author author) {
        return authorDao.create(author);
    }

    @Override
    public Optional<Author> get(long id) {
        return authorDao.get(id);
    }

    @Override
    public Author update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public void delete(long id) {
        authorDao.delete(id);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorDao.findByName(name);
    }
}
