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
public class Genre {

    @NotNull(groups = {Update.class}, message = "id must be set")
    private Long id;

    @NotNull(groups = {Insert.class, Update.class}, message = "name must be set")
    private String name;

    public Genre(String name) {
        this(null, name);
    }

}
