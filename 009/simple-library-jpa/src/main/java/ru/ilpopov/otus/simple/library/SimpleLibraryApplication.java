package ru.ilpopov.otus.simple.library;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SimpleLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleLibraryApplication.class, args);
        log.info("Application started!");
    }

}
