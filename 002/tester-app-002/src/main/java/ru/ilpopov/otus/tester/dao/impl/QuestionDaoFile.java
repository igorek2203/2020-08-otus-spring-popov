package ru.ilpopov.otus.tester.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.exception.QuestionsReadException;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;

@Component
public class QuestionDaoFile implements QuestionDao {

    private final Resource csvResource;

    public QuestionDaoFile(Resource csvResource) {
        this.csvResource = csvResource;
    }

    @Override
    public List<Question> getQuestions() {
        try {
            List<Question> questions = Lists.newArrayList();
            Iterable<CSVRecord> csvRecords = CSVFormat.RFC4180.withAllowMissingColumnNames()
                    .withIgnoreSurroundingSpaces()
                    .withIgnoreEmptyLines()
                    .parse(new InputStreamReader(csvResource.getInputStream()));
            for (CSVRecord rec : csvRecords) {
                questions.add(parseQuestion(rec));
            }
            return questions;
        } catch (IOException ex) {
            throw new QuestionsReadException("Error while getting questions", ex);
        }
    }

    private static Question parseQuestion(CSVRecord rec) {
        Question question = new Question(rec.get(0));
        question.getAnswers()
                .addAll(parseAnswers(rec));
        return question;
    }

    private static List<Answer> parseAnswers(CSVRecord rec) {
        List<Answer> answers = Lists.newArrayList();
        for (int i = 1; i < rec.size(); i++) {
            if (!Strings.isNullOrEmpty(rec.get(i))) {
                answers.add(new Answer(rec.get(i)));
            }
        }
        return answers;
    }

}
