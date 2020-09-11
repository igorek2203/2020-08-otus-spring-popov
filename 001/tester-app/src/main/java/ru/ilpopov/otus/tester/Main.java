package ru.ilpopov.otus.tester;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.ilpopov.otus.tester.service.QuestionService;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        System.out.println("Questions:");
        QuestionService questionService = context.getBean(QuestionService.class);
        questionService.printQuestions();
    }

}
