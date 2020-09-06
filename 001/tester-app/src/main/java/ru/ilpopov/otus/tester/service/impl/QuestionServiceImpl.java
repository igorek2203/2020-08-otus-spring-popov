package ru.ilpopov.otus.tester.service.impl;

import java.util.List;
import java.util.stream.IntStream;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionService;

public class QuestionServiceImpl implements QuestionService {

    private static final String QUESTION_TEMPLATE = "%s) %s\r\n";
    private static final String ANSWER_TEMPLATE = "    %s) %s\r\n";

    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Question> getQuestions() {
        return dao.getQuestions();
    }

    @Override
    public void printQuestions() {
        List<Question> questions = getQuestions();
        IntStream.range(0, questions.size())
                .forEachOrdered(qn -> {
                    Question question = questions.get(qn);
                    System.out.format(QUESTION_TEMPLATE, qn + 1, question.getText());
                    IntStream.range(0, question.getAnswers().size())
                            .forEachOrdered(an -> System.out
                                    .format(ANSWER_TEMPLATE, an + 1, question.getAnswers().get(an).getText()));
                });
    }
}
