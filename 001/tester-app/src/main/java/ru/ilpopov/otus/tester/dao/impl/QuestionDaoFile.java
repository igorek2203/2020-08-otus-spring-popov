package ru.ilpopov.otus.tester.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.exception.QuestionsReadException;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;

public class QuestionDaoFile implements QuestionDao {

    private final Resource csvResource;

    public QuestionDaoFile(Resource csvResource) {
        this.csvResource = csvResource;
    }

    @Override
    public Set<Question> getQuestions() {
        try {
            Set<Question> questions = Sets.newHashSet();
            Iterable<CSVRecord> csvRecords = CSVFormat.RFC4180.withAllowMissingColumnNames()
                    .withIgnoreSurroundingSpaces()
                    .parse(new InputStreamReader(csvResource.getInputStream()));
            Question question = null;
            boolean isPreviousRecordEmpty = true;
            for (CSVRecord rec : csvRecords) {
                if (!Strings.isNullOrEmpty(rec.get(0))
                        && !Strings.isNullOrEmpty(rec.get(1))
                        && isPreviousRecordEmpty) {
                    question = parseCsvRecordToQuestion(rec);
                    questions.add(question);
                }
                if (question != null
                        && !Strings.isNullOrEmpty(rec.get(0))
                        && !Strings.isNullOrEmpty(rec.get(1))
                        && !isPreviousRecordEmpty) {
                    question.getAnswers()
                            .add(parseCsvRecordToAnswer(rec));
                }
                isPreviousRecordEmpty = Strings.isNullOrEmpty(rec.get(0));
            }
            return questions;
        } catch (IOException ex) {
            throw new QuestionsReadException("Error while getting questions", ex);
        }
    }

    private static Question parseCsvRecordToQuestion(CSVRecord record) {
        Question question = null;
        if (!Strings.isNullOrEmpty(record.get(0))
                && record.isSet(1)) {
            question = new Question(record.get(0), record.get(1));
        }
        return question;
    }

    private static Answer parseCsvRecordToAnswer(CSVRecord record) {
        Answer answer = null;
        if (!Strings.isNullOrEmpty(record.get(0))
                && record.isSet(1)) {
            boolean isTrue = record.isSet(1) && "true".equalsIgnoreCase(record.get(1));
            answer = new Answer(record.get(0), record.get(1), isTrue);
        }
        return answer;
    }
}
