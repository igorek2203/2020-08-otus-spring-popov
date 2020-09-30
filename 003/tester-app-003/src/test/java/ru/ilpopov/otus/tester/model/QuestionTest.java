package ru.ilpopov.otus.tester.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тест модели Question")
class QuestionTest {

    @DisplayName("Проверка создания конструктором с обязательными аргументами")
    @Test
    public void testRequiredArgsConstructor() {
        Question question = new Question(1, "question");
        assertEquals("question", question.getText());
        assertNotNull(question.getAnswers());
    }

    @DisplayName("Проверка создания конструктором со всеми аргументами")
    @Test
    public void testAllArgsConstructor() {
        Question question = new Question(1, "question", Lists.newArrayList());
        assertEquals("question", question.getText());
        assertNotNull(question.getAnswers());
    }
}