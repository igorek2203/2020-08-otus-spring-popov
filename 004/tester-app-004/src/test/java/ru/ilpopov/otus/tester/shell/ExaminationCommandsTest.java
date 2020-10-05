package ru.ilpopov.otus.tester.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.ilpopov.otus.tester.config.ApplicationProperties;
import ru.ilpopov.otus.tester.service.ExaminationService;
import ru.ilpopov.otus.tester.service.LocalizationService;

@DisplayName("Консольная команда")
@TestPropertySource(properties = {"spring.shell.interactive.enabled=false"})
@SpringBootTest(classes = {ExaminationCommands.class})
class ExaminationCommandsTest {

    @MockBean
    private ExaminationService examinationService;

    @MockBean
    private LocalizationService localizationService;

    @MockBean
    private ApplicationProperties properties;

    @Autowired
    private ExaminationCommands examinationCommands;

    @DisplayName("запустит новое тестирование")
    @Test
    void startExaminationSuccess() {
        examinationCommands.startExamination();
        verify(examinationService, only()).startExamination();
    }

    @DisplayName("изменит язык")
    @Test
    void changeLanguageSuccess() {
        given(localizationService.getLocalizedMessage(anyString()))
                .willReturn("some language");
        assertThat(examinationCommands.changeLanguage("ru-RU"))
                .isEqualTo("some language");
    }
}