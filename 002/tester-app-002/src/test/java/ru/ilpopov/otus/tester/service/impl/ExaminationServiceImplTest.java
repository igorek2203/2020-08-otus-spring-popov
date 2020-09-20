package ru.ilpopov.otus.tester.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Supplier;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ilpopov.otus.tester.model.Answer;
import ru.ilpopov.otus.tester.model.Question;
import ru.ilpopov.otus.tester.service.ExaminationService;
import ru.ilpopov.otus.tester.service.QuestionService;
import ru.ilpopov.otus.tester.service.StringIOService;

@DisplayName("Сервиса проведения тестирования")
@ExtendWith(MockitoExtension.class)
class ExaminationServiceImplTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private StringIOService stringIOService;

    private ExaminationService examinationService;

    @BeforeEach
    void setUp() {
        this.examinationService = new ExaminationServiceImpl(questionService, stringIOService);
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
    void testStartExamination1() {
        Answer answer = new Answer(1, "a1", true);
        Question question = new Question(1, "q1");
        question.getAnswers().add(answer);

        given(questionService.getQuestions())
                .willReturn(Lists.newArrayList(question));
        willReturn("test-name")
                .willReturn("1")
                .given(stringIOService).read();

        examinationService.startExamination();

        ArgumentCaptor<Supplier<String>> supplierArgumentCaptor = ArgumentCaptor.forClass(Supplier.class);
        verify(stringIOService, times(5)).writeln(supplierArgumentCaptor.capture());
        supplierArgumentCaptor.getAllValues();

        assertThat(supplierArgumentCaptor.getAllValues())
                .extracting(Supplier::get)
                .contains("Please write your name:",
                        "Hello test-name! Let's start your examination.",
                        "1) q1\r\n    1) a1\r\n",
                        "Please choose one or more answers (use space , ; for splitting answers):",
                        "Your score is 1.");
    }
}