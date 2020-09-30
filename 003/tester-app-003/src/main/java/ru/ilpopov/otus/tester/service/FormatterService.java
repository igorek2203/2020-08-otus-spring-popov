package ru.ilpopov.otus.tester.service;

public interface FormatterService<T> {

    String formatToString(T entity);
}
