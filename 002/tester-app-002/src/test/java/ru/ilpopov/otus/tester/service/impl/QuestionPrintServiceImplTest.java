package ru.ilpopov.otus.tester.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.QuestionPrintService;

@DisplayName("Сервис вывода вопросов в поток")
class QuestionPrintServiceImplTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private QuestionPrintService printService;

    @BeforeEach
    void setUpAll() {
        printService = new QuestionPrintServiceImpl();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
    }

    @DisplayName("Выводит все переданные вопросы")
    @Test
    public void testPrintQuestions() {
        printService.printAllQuestions(outputStreamCaptor,
                Lists.newArrayList(new Question("q1"), new Question("q2")));
        assertThat(outputStreamCaptor.toString().trim())
                .isEqualToIgnoringCase("1) q1\r\n2) q2");
    }

}