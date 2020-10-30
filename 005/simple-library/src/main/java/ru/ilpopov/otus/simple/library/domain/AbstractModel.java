package ru.ilpopov.otus.simple.library.domain;


import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ilpopov.otus.simple.library.domain.validation.Insert;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@AllArgsConstructor
@Data
abstract class AbstractModel {

    @NotNull(groups = {Update.class}, message = "id must be set")
    private Long id;

    @NotNull(groups = {Insert.class, Update.class}, message = "name must be set")
    private String name;
    private String description;

    public AbstractModel(String name) {
        this(name, null);
    }

    public AbstractModel(Long id, String name) {
        this(id, name, null);
    }

    public AbstractModel(String name, String description) {
        this(null, name, description);
    }

    @Override
    public String toString() {
        return toStringHelper()
                .toString();
    }

    protected ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description);
    }
}
