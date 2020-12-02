package ru.ilpopov.otus.simple.library.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.ilpopov.otus.simple.library.config.MongoConfig;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;

@DisplayName("Тестирование DAO слоя по работе с коментариями")
@DataMongoTest
@Import(value = {MongoConfig.class})
class CommentDaoTest {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("найдет комментарий по содержащейся в нем фразе")
    @Test
    void findByText() {
        assertThat(commentDao.findByTextContaining("комментарий 1"))
                .hasSize(1)
                .flatExtracting(Comment::getText)
                .containsOnlyOnce("комментарий 1");
    }

    @DisplayName("найдет комментарий по идентификатору книги")
    @Test
    void findByBookId() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is("Книга 1"));
        Book book = mongoTemplate.find(query, Book.class).stream().findFirst().orElseThrow();

        assertThat(commentDao.findByBookIn(List.of(book.getId())))
                .hasSize(3)
                .flatExtracting(Comment::getText)
                .containsOnlyOnce("комментарий 1", "комментарий 2", "комментарий 3");
    }
}