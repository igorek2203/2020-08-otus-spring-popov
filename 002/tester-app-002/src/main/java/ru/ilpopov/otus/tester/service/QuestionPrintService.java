package ru.ilpopov.otus.tester.service;

import java.io.OutputStream;
import java.util.List;
import ru.ilpopov.otus.tester.model.Question;

public interface QuestionPrintService {

    void printAllQuestions(OutputStream output, List<Question> questions);

}
