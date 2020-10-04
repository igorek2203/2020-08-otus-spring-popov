package ru.ilpopov.otus.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.ilpopov.otus.tester.service.ExaminationService;

@SpringBootApplication
public class TesterAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(TesterAppApplication.class, args);
        ExaminationService examinationService = app.getBean(ExaminationService.class);
        examinationService.startExamination();
    }

}
