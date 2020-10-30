package ru.ilpopov.otus.simple.library.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Author extends AbstractModel {

    public Author(Long id, String name) {
        super(id, name);
    }

    public Author(String name) {
        super(name);
    }

    public Author(Long id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String toString() {
        return super.toStringHelper()
                .toString();
    }
}
