package ru.ilpopov.otus.tester.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тест модели Answer")
class AnswerTest {

    @DisplayName("Проверка создания конструктором с обязательными аргуметами")
    @Test
    public void testRequiredAgrsConstructor() {
        Answer answer = new Answer(1, "answer");
        assertEquals("answer", answer.getText());
    }

    @DisplayName("Проверка создания конструктором со всеми аргументами")
    @Test
    public void testAllAgrsConstructor() {
        Answer answer = new Answer(1, "answer", false);
        assertEquals("answer", answer.getText());
    }

}