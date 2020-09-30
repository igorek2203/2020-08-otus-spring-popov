package ru.ilpopov.otus.tester.service;

import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LocalizationService {

    String getLocalizedMessage(@NotNull String messageKey, String... args);
}
