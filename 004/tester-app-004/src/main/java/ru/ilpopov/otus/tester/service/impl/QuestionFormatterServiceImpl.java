package ru.ilpopov.otus.tester.service.impl;

import java.util.Comparator;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.FormatterService;

@Service
public class QuestionFormatterServiceImpl implements FormatterService<Question> {

    private static final String QUESTION_TEMPLATE = "%s) %s\r\n";
    private static final String ANSWER_TEMPLATE = "    %s) %s\r\n";

    public String formatToString(Question question) {
        StringBuilder stringBuilder = new StringBuilder(
                String.format(QUESTION_TEMPLATE, question.getNumber(), question.getText()));
        question.getAnswers()
                .stream()
                .sorted(Comparator.comparingInt(Answer::getNumber))
                .forEachOrdered(answer -> {
                    stringBuilder.append(String.format(ANSWER_TEMPLATE, answer.getNumber(), answer.getText()));
                });
        return stringBuilder.toString();
    }

}
