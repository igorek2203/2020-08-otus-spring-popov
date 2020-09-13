package ru.ilpopov.otus.tester;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.ilpopov.otus.tester.service.QuestionPrintService;
import ru.ilpopov.otus.tester.service.QuestionService;

@ComponentScan
@Configuration
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        QuestionService questionService = context.getBean(QuestionService.class);
        QuestionPrintService printService = context.getBean(QuestionPrintService.class);
        System.out.println("Questions:");
        printService.printAllQuestions(System.out, questionService.getQuestions());
    }

}
