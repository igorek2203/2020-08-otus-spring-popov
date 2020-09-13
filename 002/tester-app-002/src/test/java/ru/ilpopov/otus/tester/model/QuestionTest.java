package ru.ilpopov.otus.tester.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тест модели Question")
class QuestionTest {

    @DisplayName("Проверка создания конструктором")
    @Test
    public void testConstructor() {
        Question question = new Question("question");
        assertEquals("question", question.getText());
        assertNotNull(question.getAnswers());
    }
}