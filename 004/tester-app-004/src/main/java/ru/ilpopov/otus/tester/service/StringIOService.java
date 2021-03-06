package ru.ilpopov.otus.tester.service;

import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.tester.exception.StringIOException;

@Validated
public interface StringIOService {

    void write(@NotNull String message) throws StringIOException;

    void writeln(@NotNull String message) throws StringIOException;

    String read();
}
