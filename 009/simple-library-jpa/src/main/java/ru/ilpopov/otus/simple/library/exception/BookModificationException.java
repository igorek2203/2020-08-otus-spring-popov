package ru.ilpopov.otus.simple.library.exception;

public class BookModificationException extends RuntimeException {

    public BookModificationException() {
    }

    public BookModificationException(String message) {
        super(message);
    }

    public BookModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
