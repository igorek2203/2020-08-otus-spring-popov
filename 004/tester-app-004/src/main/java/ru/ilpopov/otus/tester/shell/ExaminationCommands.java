package ru.ilpopov.otus.tester.shell;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.ilpopov.otus.tester.config.ApplicationProperties;
import ru.ilpopov.otus.tester.service.ExaminationService;
import ru.ilpopov.otus.tester.service.LocalizationService;

@ShellComponent
@RequiredArgsConstructor
public class ExaminationCommands {

    private final static String LANGUAGE_MESSAGE_KEY = "lang";

    private final ExaminationService examinationService;
    private final LocalizationService localizationService;
    private final ApplicationProperties properties;

    @ShellMethod(value = "Start examination", key = {"start", "start-exam", "exam"})
    public void startExamination() {
        examinationService.startExamination();
    }

    @ShellMethod(value = "Switch language (available options ru-RU, en-US)", key = {"l", "lang", "language"})
    public String changeLanguage(@ShellOption(defaultValue = "en-US") String lang) {
        properties.setLocale(Locale.forLanguageTag(lang));
        return localizationService.getLocalizedMessage(LANGUAGE_MESSAGE_KEY);
    }
}
