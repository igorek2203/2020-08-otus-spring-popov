package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.service.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Override
    public Book create(Book book) {
        return bookDao.create(book);
    }

    @Override
    public Optional<Book> getById(long id) {
        return bookDao.getById(id);
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book);
    }

    @Override
    public void deleteById(long id) {
        bookDao.deleteById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookDao.findByTitle(title);
    }

    @Override
    public List<Book> findByAuthorFullName(@NotNull String fullName) {
        return bookDao.findByAuthorFullName(fullName);
    }

    @Override
    public List<Book> findByGenreName(@NotNull String name) {
        return bookDao.findByGenreName(name);
    }
}
