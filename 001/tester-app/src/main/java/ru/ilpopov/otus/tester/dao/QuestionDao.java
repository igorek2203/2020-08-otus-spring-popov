package ru.ilpopov.otus.tester.dao;

import java.util.List;
import ru.ilpopov.otus.tester.model.Question;

public interface QuestionDao {

    List<Question> getQuestions();
}
