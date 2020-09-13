package ru.ilpopov.otus.tester.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class DaoConfig {

    @Bean
    public Resource csvResource(@Value("classpath:questions.csv") Resource csvResource) {
        return csvResource;
    }
}
