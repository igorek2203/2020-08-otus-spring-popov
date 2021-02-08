package ru.ilpopov.otus.simple.library.exception;

public class StringIOException extends RuntimeException {

    public StringIOException() {
    }

    public StringIOException(String message) {
        super(message);
    }

    public StringIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
