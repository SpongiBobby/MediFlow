package it.mediflow.sportello.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pazienti", schema = "public")
public class Pazienti implements Serializable {
    @Serial
    private static final long serialVersionUID = -7098479071325641134L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 100)
    private String cognome;

    @Column(name = "data_di_nascita", nullable = false)
    private LocalDate dataDiNascita;

    @Column(name = "luogo_di_nascita", nullable = false)
    private String luogoDiNascita;

    @Column(name = "codice_fiscale", nullable = false, unique = true, length = 16)
    private String codiceFiscale;

    @Column(name = "indirizzo_di_residenza", nullable = false)
    private String indirizzoDiResidenza;

    @Column(name = "telefono", length = 10)
    private String telefono;

    @Column(name = "cellulare", nullable = false, length = 10)
    private String cellulare;

    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @JdbcTypeCode(SqlTypes.CLOB)
    @Column(name = "annotazioni")
    private String annotazioni;
}