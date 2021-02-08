package ru.ilpopov.otus.simple.library.repository;

import ru.ilpopov.otus.simple.library.domain.Book;

public interface BookCustomRepository {

    Book modifyFieldValue(String bookId, String fieldName, Object value);

    void deleteById(String bookId, boolean withComments);

    void delete(Book book, boolean withComments);

}
