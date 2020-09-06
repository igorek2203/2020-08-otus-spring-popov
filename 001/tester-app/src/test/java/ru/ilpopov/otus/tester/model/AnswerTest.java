package ru.ilpopov.otus.tester.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тест модели Answer")
class AnswerTest {

    @DisplayName("Проверка создания конструктором")
    @Test
    public void testConstructor() {
        Answer answer = new Answer("answer");
        assertEquals("answer", answer.getText());
    }

}