package ru.ilpopov.otus.tester.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class TesterAppConfig {

}
