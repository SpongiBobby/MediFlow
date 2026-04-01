package it.mediflow.sportello.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "specializzazione", schema = "public")
public class Specializzazione implements Serializable {
    @Serial
    private static final long serialVersionUID = -4452570655642129902L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @OneToMany(mappedBy = "specializzazione")
    private Set<Medici> medicis = new LinkedHashSet<>();
}