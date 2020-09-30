package ru.ilpopov.otus.tester.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.google.common.collect.ImmutableMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ilpopov.otus.tester.config.ApplicationProperties;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;

@DisplayName("Dao слой по работе с вопросами")
@ExtendWith(SpringExtension.class)
class QuestionDaoFileTest {

    @Configuration
    static class conf {

        @Bean
        public ApplicationProperties questionsSettings(Resource filePath) {
            Locale locale = new Locale("ru");
            ApplicationProperties questionsSettings = new ApplicationProperties();
            questionsSettings.setLocale(locale);
            questionsSettings.setQuestions(ImmutableMap.of(locale, filePath));
            return questionsSettings;
        }

        @Bean
        public QuestionDao dao(ApplicationProperties questionsSettings) {
            return new QuestionDaoFile(questionsSettings);
        }
    }

    @MockBean
    Resource filePath;

    @Autowired
    QuestionDao dao;

    @DisplayName("Возвращает список вопросов")
    @Test
    void testGetQuestions() throws IOException {
        given(filePath.getInputStream())
                .willReturn(new ByteArrayInputStream("q1,a1,a2,\"1,2\"\r\nq2,a21,\"1\"".getBytes()));

        List<Question> questions = dao.getQuestions();
        assertThat(questions).hasSize(2)
                .extracting(Question::getText)
                .contains("q1", "q2");

        assertThat(questions.get(0).getAnswers())
                .extracting(Answer::getText)
                .containsExactly("a1", "a2");

        assertThat(questions).hasSize(2)
                .extracting(Question::getAnswers)
                .flatExtracting(answerList -> answerList)
                .extracting(Answer::getText)
                .containsExactly("a1", "a2", "a21");
    }

}