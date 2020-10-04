package ru.ilpopov.otus.tester.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ilpopov.otus.tester.config.ApplicationProperties;
import ru.ilpopov.otus.tester.service.LocalizationService;

@TestPropertySource(properties = {"spring.messages.basename=i18n.messages"})
@SpringBootTest
class LocalizationServiceImplTest {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private LocalizationService localizationService;

    @Test
    void getLocalizedMessageRu() {
        properties.setLocale(new Locale("ru", "RU"));
        assertThat(localizationService.getLocalizedMessage("test"))
                .isEqualTo("Тест");
    }

    @Test
    void getLocalizedMessageEn() {
        properties.setLocale(new Locale("en", "US"));
        assertThat(localizationService.getLocalizedMessage("test"))
                .isEqualTo("Test");
    }

}