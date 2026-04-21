package it.mediflow.sportello.entity;

import it.mediflow.sportello.enums.StatoPrenotazione;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "prenotazioni")
public class Prenotazioni {
    @Id
    @SequenceGenerator(
            name = "prenotazioni_seq_gen",
            sequenceName = "prenotazioni_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prenotazioni_seq_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("now()")
    @Column(name = "data_prenotazione", nullable = false, insertable = false)
    private LocalDate dataPrenotazione;

    @Column(name = "data_visita", nullable = false)
    private LocalDate dataVisita;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false, length = 100)
    private StatoPrenotazione stato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Pazienti paziente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medici medico;

    @Column(name = "annotazioni", columnDefinition = "text")
    private String annotazioni;
}