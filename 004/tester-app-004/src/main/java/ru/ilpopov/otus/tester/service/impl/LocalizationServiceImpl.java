package ru.ilpopov.otus.tester.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.ilpopov.otus.tester.config.ApplicationProperties;
import ru.ilpopov.otus.tester.service.LocalizationService;

@Service
public class LocalizationServiceImpl implements LocalizationService {

    private final ApplicationProperties properties;
    private final MessageSource messageSource;

    public LocalizationServiceImpl(ApplicationProperties properties,
            MessageSource messageSource) {
        this.properties = properties;
        this.messageSource = messageSource;
    }

    @Override
    public String getLocalizedMessage(String messageKey, String... args) {
        return messageSource.getMessage(messageKey, args, properties.getLocale());
    }

}
