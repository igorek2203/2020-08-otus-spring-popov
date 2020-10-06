package ru.ilpopov.otus.tester.model;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Question {

    private final int number;

    @NonNull
    private final String text;

    private List<Answer> answers = Lists.newArrayList();

}
