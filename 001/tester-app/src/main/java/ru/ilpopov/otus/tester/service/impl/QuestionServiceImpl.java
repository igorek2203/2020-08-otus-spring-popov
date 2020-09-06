package ru.ilpopov.otus.tester.service.impl;

import java.util.Comparator;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionService;

public class QuestionServiceImpl implements QuestionService {

    private static final String QUESTION_TEMPLATE = "%s) %s?\r\n";
    private static final String ANSWER_TEMPLATE = "    %s) %s;\r\n";

    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public void printQuestions() {
        dao.getQuestions()
                .stream()
                .sorted(Comparator.comparing(Question::getId))
                .forEach(q -> {
                    System.out.format(QUESTION_TEMPLATE, q.getId(), q.getText());
                    q.getAnswers()
                            .stream()
                            .sorted(Comparator.comparing(Answer::getId))
                            .forEach(a -> System.out.format(ANSWER_TEMPLATE, a.getId(), a.getText()));
                });
    }
}
