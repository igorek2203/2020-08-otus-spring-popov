package ru.ilpopov.otus.simple.library.listener;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.ilpopov.otus.simple.library.domain.Comment;

@Component
public class CommentCascadeRemoveMongoEventListener extends AbstractMongoEventListener<Object> {

    private final MongoOperations mongoOperations;

    public CommentCascadeRemoveMongoEventListener(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        Document document = event.getSource();
        if ("books".equalsIgnoreCase(event.getCollectionName())) {
            Query queryRemoveCommentsByBookId = Query.query(Criteria.where("book")
                    .is(document.getObjectId("_id").toString()));
            mongoOperations.remove(queryRemoveCommentsByBookId, Comment.class);
        }
    }
}
