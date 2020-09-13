package ru.ilpopov.otus.tester.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.exception.QuestionOutputException;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionPrintService;

@Service
public class QuestionPrintServiceImpl implements QuestionPrintService {

    private static final String QUESTION_TEMPLATE = "%s) %s\r\n";
    private static final String ANSWER_TEMPLATE = "    %s) %s\r\n";

    public void printAllQuestions(OutputStream output, List<Question> questions) {
        IntStream.range(0, questions.size())
                .forEachOrdered(qn -> {
                    Question question = questions.get(qn);
                    try {
                        output.write(String.format(QUESTION_TEMPLATE, qn + 1, question.getText()).getBytes());
                    } catch (IOException ex) {
                        throw new QuestionOutputException(ex);
                    }
                    IntStream.range(0, question.getAnswers().size())
                            .forEachOrdered(an -> {
                                try {
                                    output.write(String.format(ANSWER_TEMPLATE, an + 1,
                                            question.getAnswers().get(an).getText()).getBytes());
                                } catch (IOException ex) {
                                    throw new QuestionOutputException(ex);
                                }
                            });
                });
    }
}
