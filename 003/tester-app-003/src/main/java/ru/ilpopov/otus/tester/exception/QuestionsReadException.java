package ru.ilpopov.otus.tester.exception;

public class QuestionsReadException extends RuntimeException {

    public QuestionsReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionsReadException(Throwable cause) {
        super(cause);
    }
}
