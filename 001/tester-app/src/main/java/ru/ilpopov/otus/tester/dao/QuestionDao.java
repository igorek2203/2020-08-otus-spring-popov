package ru.ilpopov.otus.tester.dao;

import java.util.Set;
import ru.ilpopov.otus.tester.model.Question;

public interface QuestionDao {

    Set<Question> getQuestions();
}
