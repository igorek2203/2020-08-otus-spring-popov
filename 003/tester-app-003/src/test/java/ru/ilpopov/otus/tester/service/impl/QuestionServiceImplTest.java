package ru.ilpopov.otus.tester.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionService;

@DisplayName("Сервиса для работы с вопросами")
@SpringBootTest(classes = {QuestionServiceImpl.class})
public class QuestionServiceImplTest {

    @Configuration
    static class conf {

    }

    @MockBean
    private QuestionDao dao;

    @Autowired
    private QuestionService service;

    @DisplayName("Возвращает все вопросы с вариантами ответов")
    @Test
    public void testGetQuestions() {
        will(invocationOnMock -> Lists.newArrayList(new Question(1, "q1"), new Question(2, "q2")))
                .given(dao)
                .getQuestions();
        List<Question> questions = service.getQuestions();
        verify(dao, only()).getQuestions();
        assertThat(questions).hasSize(2);
    }

}