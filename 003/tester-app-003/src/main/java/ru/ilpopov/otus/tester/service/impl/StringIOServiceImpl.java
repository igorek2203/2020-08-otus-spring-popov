package ru.ilpopov.otus.tester.service.impl;

import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.exception.StringIOException;
import ru.ilpopov.otus.tester.service.StringIOService;

@Service
public class StringIOServiceImpl implements StringIOService {

    private final OutputStream outputStream;
    private final Scanner stringScanner;

    public StringIOServiceImpl(
            @Value("#{ T(System).out}") OutputStream outputStream,
            @Value("#{ T(System).in}") InputStream inputStream) {
        this.outputStream = outputStream;
        this.stringScanner = new Scanner(inputStream);
    }

    @Override
    public void write(String message) throws StringIOException {
        writeString(Strings.nullToEmpty(message));
    }

    @Override
    public void writeln(String message) throws StringIOException {
        writeString(Strings.nullToEmpty(message) + "\r\n");
    }

    private void writeString(String message) {
        try {
            outputStream.write(message.getBytes());
        } catch (IOException ex) {
            throw new StringIOException("Cannot write a string", ex);
        }
    }

    @Override
    public String read() {
        return stringScanner.nextLine();
    }
}
