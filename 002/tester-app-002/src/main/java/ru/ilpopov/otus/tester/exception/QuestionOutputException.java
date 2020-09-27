package ru.ilpopov.otus.tester.exception;

public class QuestionOutputException extends RuntimeException {

    public QuestionOutputException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionOutputException(Throwable cause) {
        super(cause);
    }
}
