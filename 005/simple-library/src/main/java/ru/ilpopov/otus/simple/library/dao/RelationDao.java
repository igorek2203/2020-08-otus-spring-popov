package ru.ilpopov.otus.simple.library.dao;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Validated
public interface RelationDao<F, T> {

    @Validated({Default.class, Update.class})
    void relate(@Valid @NotNull F from, @Valid @NotNull T to);

    @Validated({Default.class, Update.class})
    List<F> findByTo(@Valid @NotNull T to);

    @Validated({Default.class, Update.class})
    List<T> findByFrom(@Valid @NotNull F from);

    @Validated({Default.class, Update.class})
    void detach(@Valid @NotNull F from, @Valid @NotNull T to);

}
