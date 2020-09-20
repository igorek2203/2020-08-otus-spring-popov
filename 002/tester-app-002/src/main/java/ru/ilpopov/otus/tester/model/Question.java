package ru.ilpopov.otus.tester.model;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

public @Value class Question {

    @NonNull int number;
    @NonNull String text;
    @NonFinal
    List<Answer> answers = Lists.newArrayList();

    public Question(@NonNull int number, @NonNull String text) {
        this.number = number;
        this.text = text;
    }
}
