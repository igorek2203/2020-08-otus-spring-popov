package ru.ilpopov.otus.tester.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.ExaminationService;
import ru.ilpopov.otus.tester.service.FormatterService;
import ru.ilpopov.otus.tester.service.LocalizationService;
import ru.ilpopov.otus.tester.service.QuestionService;
import ru.ilpopov.otus.tester.service.StringIOService;

@DisplayName("Сервис проведения тестирования")
@SpringBootTest(classes = {ExaminationServiceImpl.class})
class ExaminationServiceImplTest {

    @Configuration
    static class conf {

    }

    @MockBean
    private QuestionService questionService;

    @MockBean
    private StringIOService stringIOService;

    @MockBean
    private FormatterService<Question> formatterService;

    @MockBean
    private LocalizationService localizationService;

    @Autowired
    private ExaminationService examinationService;

    @BeforeEach
    void setUp() {
        given(localizationService.getLocalizedMessage(any(String.class), any()))
                .will((org.mockito.stubbing.Answer<String>) invocation -> invocation.getArgument(0).toString());
    }

    @DisplayName("Спросит имя, попривествует, задаст вопрос, получит ответ и выведет результат")
    @Test
    void testStartExamination() {
        Answer answer = new Answer(1, "a1", true);
        Question question = new Question(1, "q1");
        question.getAnswers().add(answer);

        given(questionService.getQuestions())
                .willReturn(Lists.newArrayList(question));
        willReturn("test-name")
                .willReturn("1")
                .given(stringIOService).read();

        examinationService.startExamination();

        then(questionService).should().getQuestions();
        InOrder inOrder = Mockito.inOrder(stringIOService);
        then(stringIOService)
                .should(inOrder, times(1))
                .writeln(any());
        then(stringIOService)
                .should(inOrder, times(1))
                .read();
        then(stringIOService)
                .should(inOrder, times(3))
                .writeln(any());
        then(stringIOService)
                .should(inOrder, times(1))
                .read();
        then(stringIOService)
                .should(inOrder, times(1))
                .writeln(any());
    }

    @DisplayName("Спросит имя, поприветствует и т.к. вопросв нет, то сразу выведет результат")
    @Test
    void testStartExaminationWithoutQuestions() {
        given(questionService.getQuestions())
                .willReturn(Lists.newArrayList());
        willReturn("test-name")
                .given(stringIOService).read();

        examinationService.startExamination();

        then(questionService).should().getQuestions();
        InOrder inOrder = Mockito.inOrder(stringIOService);
        then(stringIOService)
                .should(inOrder, times(1))
                .writeln(any());
        then(stringIOService)
                .should(inOrder, times(1))
                .read();
        then(stringIOService)
                .should(inOrder, times(2))
                .writeln(any());
    }

    @DisplayName("Выполнит успешное тестирование с одним вопросом и одним ответом")
    @Test
    void testStartExaminationSeccess() {
        Answer answer = new Answer(1, "a1", true);
        Question question = new Question(1, "q1");
        question.getAnswers().add(answer);

        given(questionService.getQuestions())
                .willReturn(Lists.newArrayList(question));
        given(stringIOService.read())
                .willReturn("test-name")
                .willReturn("1");
        given(formatterService.formatToString(any(Question.class)))
                .willReturn("1) q1\r\n    1) a1\r\n");

        examinationService.startExamination();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(stringIOService, times(5)).writeln(stringArgumentCaptor.capture());
        stringArgumentCaptor.getAllValues();

        assertThat(stringArgumentCaptor.getAllValues())
                .contains("ask.user.name",
                        "greeting.user",
                        "1) q1\r\n    1) a1\r\n",
                        "choose.options",
                        "your.score");
    }
}