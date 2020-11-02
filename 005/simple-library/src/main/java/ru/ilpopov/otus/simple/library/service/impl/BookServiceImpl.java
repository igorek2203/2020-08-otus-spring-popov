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
    public Optional<Book> get(long id) {
        return bookDao.get(id);
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book);
    }

    @Override
    public void delete(long id) {
        bookDao.delete(id);
    }

    @Override
    public List<Book> findByName(String name) {
        return bookDao.findByName(name);
    }

    @Override
    public List<Book> findByAuthorName(@NotNull String name) {
        return bookDao.findByAuthorName(name);
    }

    @Override
    public List<Book> findByGenreName(@NotNull String name) {
        return bookDao.findByGenreName(name);
    }
}
