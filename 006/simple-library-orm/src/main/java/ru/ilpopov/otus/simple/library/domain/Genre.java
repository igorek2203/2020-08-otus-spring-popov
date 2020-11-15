package ru.ilpopov.otus.simple.library.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import ru.ilpopov.otus.simple.library.domain.validation.Create;
import ru.ilpopov.otus.simple.library.domain.validation.Update;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genres")
public class Genre {

    @NotNull(groups = {Update.class}, message = "id must be set")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId(mutable = true)
    @NotNull(groups = {Create.class, Update.class}, message = "name must be set")
    @Column(nullable = false, unique = true)
    private String name;

    public Genre(String name) {
        this(null, name);
    }
}
