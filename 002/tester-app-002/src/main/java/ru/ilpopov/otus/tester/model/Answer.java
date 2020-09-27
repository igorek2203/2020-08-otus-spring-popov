package ru.ilpopov.otus.tester.model;

import javax.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.Value;

public @Value class Answer {

    @NonNull int number;
    @NonNull String text;
    @NotNull boolean isRight;

}
