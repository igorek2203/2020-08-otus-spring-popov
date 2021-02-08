package ru.ilpopov.otus.simple.library.repository.impl;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.repository.BookCustomRepository;
import ru.ilpopov.otus.simple.library.repository.CommentRepository;

@Component
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final MongoTemplate mongoTemplate;
    private final CommentRepository commentRepository;

    public BookCustomRepositoryImpl(MongoTemplate mongoTemplate, CommentRepository commentRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commentRepository = commentRepository;
    }

    @Override
    public Book modifyFieldValue(String bookId, String fieldName, Object value) {
        Criteria criteria = Criteria.where("_id").is(bookId)
                .and(fieldName).exists(true);
        Query query = Query.query(criteria);
        FindAndModifyOptions options = FindAndModifyOptions.options();
        options.returnNew(true);
        Update update = Update.update(fieldName, value);
        return mongoTemplate.findAndModify(query, update, options, Book.class);
    }

    @Override
    public void deleteById(String bookId, boolean withComments) {
        if (withComments) {
            commentRepository.deleteAllByBook(bookId);
        }
        mongoTemplate.findAndRemove(Query.query(Criteria.where("_id").is(bookId)), Book.class);
    }

    @Override
    public void delete(Book book, boolean withComments) {
        deleteById(book.getId(), withComments);
    }
}
