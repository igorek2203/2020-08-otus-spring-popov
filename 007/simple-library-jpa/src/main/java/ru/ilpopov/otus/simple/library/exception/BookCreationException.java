package ru.ilpopov.otus.simple.library.exception;

public class BookCreationException extends RuntimeException {

    public BookCreationException() {
    }

    public BookCreationException(String message) {
        super(message);
    }

    public BookCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
