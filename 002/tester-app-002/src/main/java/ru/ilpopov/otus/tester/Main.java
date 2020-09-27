package ru.ilpopov.otus.tester;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.ilpopov.otus.tester.service.ExaminationService;

@ComponentScan
@Configuration
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        ExaminationService examinationService = context.getBean(ExaminationService.class);
        examinationService.startExamination();
    }

}