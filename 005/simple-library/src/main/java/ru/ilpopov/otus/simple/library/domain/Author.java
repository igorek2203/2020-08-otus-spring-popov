package ru.ilpopov.otus.simple.library.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @NotNull(groups = {Update.class}, message = "id must be set")
    private Long id;

    @NotNull(groups = {Insert.class, Update.class}, message = "name must be set")
    private String fullName;

    private String description;

    public Author(Long id, String fullName) {
        this(id, fullName, null);
    }

    public Author(String fullName) {
        this(null, fullName);
    }
}
