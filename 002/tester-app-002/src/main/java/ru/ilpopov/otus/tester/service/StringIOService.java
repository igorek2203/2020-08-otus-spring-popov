package ru.ilpopov.otus.tester.service;

import java.util.function.Supplier;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.tester.exception.StringIOException;

@Validated
public interface StringIOService {

    void write(@NotNull Supplier<String> message) throws StringIOException;

    void writeln(@NotNull Supplier<String> message) throws StringIOException;

    String read();
}
