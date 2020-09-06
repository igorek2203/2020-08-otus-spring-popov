package ru.ilpopov.otus.tester.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ilpopov.otus.tester.dao.QuestionDao;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionService;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    private QuestionDao dao;
    private QuestionService service;

    @BeforeEach
    void setUpAll() {
        service = new QuestionServiceImpl(dao);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void testGetQuestions() {
        will(invocationOnMock -> Lists.newArrayList(new Question("q1"), new Question("q2")))
                .given(dao)
                .getQuestions();
        List<Question> questions = service.getQuestions();
        verify(dao, only()).getQuestions();
        assertThat(questions).hasSize(2);
    }

    @Test
    public void testPrintQuestions() {
        will(invocationOnMock -> Lists.newArrayList(new Question("q1"), new Question("q2")))
                .given(dao)
                .getQuestions();
        service.printQuestions();
        verify(dao, only()).getQuestions();
        assertThat(outputStreamCaptor.toString().trim())
                .isEqualToIgnoringCase("1) q1\r\n2) q2");
    }

}