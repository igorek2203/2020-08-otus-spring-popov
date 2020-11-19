package ru.ilpopov.otus.simple.library.service;

public interface FormatterService<T> {

    String formatToString(T entity);

}

