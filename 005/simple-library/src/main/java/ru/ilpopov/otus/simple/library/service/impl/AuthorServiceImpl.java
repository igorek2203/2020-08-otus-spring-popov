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
    public Optional<Author> getById(long id) {
        return authorDao.getById(id);
    }

    @Override
    public Author update(Author author) {
        return authorDao.update(author);
    }

    @Override
    public void deleteById(long id) {
        authorDao.deleteById(id);
    }

    @Override
    public List<Author> findByFullName(String fullName) {
        return authorDao.findByFullName(fullName);
    }
}
