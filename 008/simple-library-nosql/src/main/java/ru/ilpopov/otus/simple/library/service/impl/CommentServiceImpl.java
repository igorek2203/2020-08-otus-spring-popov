package ru.ilpopov.otus.simple.library.service.impl;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.service.CommentService;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> getById(String commentId) {
        return commentDao.findById(commentId);
    }

    @Override
    public Comment create(@Valid @NotNull Comment comment) {
        return commentDao.save(comment);
    }

    @Override
    public Comment update(@Valid @NotNull Comment comment) {
        return commentDao.save(comment);
    }

    @Override
    public void deleteById(String commentId) {
        commentDao.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByComment(String comment) {
        return commentDao.findByTextContaining(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(List<String> bookId) {
        return commentDao.findByBookIn(bookId);
    }
}
