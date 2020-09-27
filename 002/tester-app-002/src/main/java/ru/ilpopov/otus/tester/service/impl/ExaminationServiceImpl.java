package ru.ilpopov.otus.tester.service.impl;

import com.google.common.base.Strings;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.ExaminationService;
import ru.ilpopov.otus.tester.service.QuestionService;
import ru.ilpopov.otus.tester.service.StringIOService;
import ru.ilpopov.otus.tester.util.QuestionFormatter;

@AllArgsConstructor
@Service
public class ExaminationServiceImpl implements ExaminationService {

    private static final String ANSWER_SPLIT_PATTERN = "[,; ]";
    private static final String DIGITS_PATTERN = "([0-9]*)";
    private static final String ASK_NAME_STRING = "Please write your name:";
    private static final String GREETING_STRING_TEMPLATE = "Hello %s! Let's start your examination.";
    private static final String CHOOSING_ANSWERS_STRING = "Please choose one or more answers "
            + "(use space , ; for splitting answers):";
    public static final String TOTAL_SCORE_TEMPLATE = "Your score is %d.";

    private final QuestionService questionService;
    private final StringIOService stringIOService;

    @Override
    public void startExamination() {
        stringIOService.writeln(() -> ASK_NAME_STRING);
        String name = stringIOService.read();
        stringIOService.writeln(() -> String.format(GREETING_STRING_TEMPLATE, name));
        int score = 0;
        for (Question question : questionService.getQuestions()) {
            stringIOService.writeln(() -> QuestionFormatter.formatToString(question));
            stringIOService.writeln(() -> CHOOSING_ANSWERS_STRING);
            score += Arrays.equals(getArrayOfRightAnswers(question), getArrayOfAnswers()) ? 1 : 0;
        }
        final String resultMessage = String.format(TOTAL_SCORE_TEMPLATE, score);
        stringIOService.writeln(() -> resultMessage);
    }

    private int[] getArrayOfRightAnswers(Question question) {
        return question.getAnswers()
                .stream()
                .filter(Answer::isRight)
                .mapToInt(Answer::getNumber)
                .toArray();
    }

    private int[] getArrayOfAnswers() {
        return Arrays.stream(stringIOService.read()
                .split(ANSWER_SPLIT_PATTERN))
                .filter(a -> !Strings.isNullOrEmpty(a))
                .filter(a -> a.matches(DIGITS_PATTERN))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
