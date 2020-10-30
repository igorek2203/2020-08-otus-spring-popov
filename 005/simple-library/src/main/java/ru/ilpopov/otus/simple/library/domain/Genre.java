package ru.ilpopov.otus.simple.library.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Genre extends AbstractModel {

    public Genre(Long id, String name) {
        super(id, name);
    }

    public Genre(String name) {
        super(name);
    }

    public Genre(Long id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String toString() {
        return super.toStringHelper()
                .toString();
    }
}
