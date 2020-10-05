package ru.ilpopov.otus.tester.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import ru.ilpopov.otus.tester.config.ApplicationProperties;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.exception.QuestionsReadException;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;

@Repository
public class QuestionDaoFile implements QuestionDao {

    private static final String RIGHT_ANSWERS_SPLITTER = ",";
    private final ApplicationProperties properties;

    public QuestionDaoFile(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<Question> getQuestions() {
        try {
            Resource csvResource = Optional.ofNullable(properties)
                    .map(ApplicationProperties::getLocalizedQuestions)
                    .orElseThrow(() -> new QuestionsReadException("Csv resource is empty"));
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
        int rowNumber = ((Long) rec.getRecordNumber()).intValue();
        Question question = new Question(rowNumber, rec.get(0));
        question.getAnswers()
                .addAll(parseAnswers(rec));
        return question;
    }

    private static List<Answer> parseAnswers(CSVRecord rec) {
        List<Answer> answers = Lists.newArrayList();
        List<Integer> rightAnswers = Arrays.stream(rec.get(rec.size() - 1).split(RIGHT_ANSWERS_SPLITTER))
                .filter(an -> !Strings.isNullOrEmpty(an))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int i = 1; i < rec.size() - 1; i++) {
            if (!Strings.isNullOrEmpty(rec.get(i))) {
                answers.add(new Answer(i, rec.get(i), rightAnswers.contains(i)));
            }
        }
        return answers;

    }

}
