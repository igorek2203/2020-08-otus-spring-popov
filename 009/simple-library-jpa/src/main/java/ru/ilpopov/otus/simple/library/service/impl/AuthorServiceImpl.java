package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilpopov.otus.simple.library.dao.AuthorDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.service.AuthorService;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public Author create(Author author) {
        return authorDao.save(author);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> getById(long authorId) {
        return authorDao.findById(authorId);
    }

    @Override
    public Author update(Author author) {
        return authorDao.save(author);
    }

    @Override
    public void deleteById(long authorId) {
        authorDao.deleteById(authorId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> findByFullName(String fullName) {
        return authorDao.findByFullName(fullName);
    }

    @Override
    public List<Author> findAll() {
        return StreamSupport.stream(authorDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
