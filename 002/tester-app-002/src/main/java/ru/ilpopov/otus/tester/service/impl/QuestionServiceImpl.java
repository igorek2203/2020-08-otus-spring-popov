package ru.ilpopov.otus.tester.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Question> getQuestions() {
        return dao.getQuestions();
    }
}
