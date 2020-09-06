package ru.ilpopov.otus.tester.model;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

public @Value class Answer {

    @NonNull String id;
    @NonNull String text;
    @NonFinal boolean isTrue;

}
