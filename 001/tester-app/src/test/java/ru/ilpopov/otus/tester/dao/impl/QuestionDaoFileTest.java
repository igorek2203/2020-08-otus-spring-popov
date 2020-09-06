package ru.ilpopov.otus.tester.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.will;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;

@ExtendWith(MockitoExtension.class)
class QuestionDaoFileTest {

    @Mock
    private Resource csvResource;
    private QuestionDao dao;

    @BeforeEach
    void setUpAll() {
        dao = new QuestionDaoFile(csvResource);
    }

    @Test
    public void testGetQuestions() throws IOException {
        will(invocationOnMock -> new ByteArrayInputStream("q1,a1,a2\r\nq2,a21".getBytes()))
                .given(csvResource).getInputStream();
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