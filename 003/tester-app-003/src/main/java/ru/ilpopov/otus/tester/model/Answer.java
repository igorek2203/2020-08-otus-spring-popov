package ru.ilpopov.otus.tester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Answer {

    private final int number;

    @NonNull
    private final String text;

    private boolean isRight;

}
