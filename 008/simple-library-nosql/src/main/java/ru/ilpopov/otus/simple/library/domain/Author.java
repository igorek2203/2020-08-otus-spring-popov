package ru.ilpopov.otus.simple.library.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @NotNull(groups = {Create.class, Update.class}, message = "name must be set")
    private String fullName;
    private String description;

    public Author(String fullName) {
        this(fullName, null);
    }
}
