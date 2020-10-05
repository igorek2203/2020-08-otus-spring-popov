package ru.ilpopov.otus.tester.service.impl;

import com.google.common.base.Strings;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.ExaminationService;
import ru.ilpopov.otus.tester.service.FormatterService;
import ru.ilpopov.otus.tester.service.LocalizationService;
import ru.ilpopov.otus.tester.service.QuestionService;
import ru.ilpopov.otus.tester.service.StringIOService;

@RequiredArgsConstructor
@Service
public class ExaminationServiceImpl implements ExaminationService {

    private static final String ANSWER_SPLIT_PATTERN = "[,; ]";
    private static final String DIGITS_PATTERN = "([0-9]*)";
    private static final String ASK_NAME_KEY = "ask.user.name";
    private static final String GREETING_KEY = "greeting.user";
    private static final String CHOOSING_OPTIONS_KEY = "choose.options";
    private static final String TOTAL_SCORE_KEY = "your.score";

    private final QuestionService questionService;
    private final StringIOService stringIOService;
    private final LocalizationService localizationService;
    private final FormatterService<Question> questionFormatterService;

    @Override
    public void startExamination() {
        stringIOService.writeln(localizationService.getLocalizedMessage(ASK_NAME_KEY));
        String name = stringIOService.read();
        stringIOService.writeln(localizationService.getLocalizedMessage(GREETING_KEY, name));
        int score = 0;
        for (Question question : questionService.getQuestions()) {
            stringIOService.writeln(questionFormatterService.formatToString(question));
            stringIOService.writeln(localizationService.getLocalizedMessage(CHOOSING_OPTIONS_KEY));
            score += Arrays.equals(getArrayOfRightAnswers(question), getArrayOfAnswers()) ? 1 : 0;
        }
        stringIOService.writeln(localizationService.getLocalizedMessage(TOTAL_SCORE_KEY, String.valueOf(score)));
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
