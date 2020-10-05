package ru.ilpopov.otus.tester.config;

import java.util.Locale;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "tester-app")
public class ApplicationProperties {

    private Locale locale;
    private Map<Locale, Resource> questions;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale local) {
        this.locale = local;
    }

    public Map<Locale, Resource> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<Locale, Resource> questions) {
        this.questions = questions;
    }

    public Resource getLocalizedQuestions() {
        return questions.get(locale);
    }
}
