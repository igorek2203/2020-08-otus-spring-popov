package ru.ilpopov.otus.tester.util;

import java.util.Comparator;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;

public final class QuestionFormatter {

    private static final String QUESTION_TEMPLATE = "%s) %s\r\n";
    private static final String ANSWER_TEMPLATE = "    %s) %s\r\n";

    private QuestionFormatter() {
        // empty
    }

    public static String formatToString(Question question) {
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
