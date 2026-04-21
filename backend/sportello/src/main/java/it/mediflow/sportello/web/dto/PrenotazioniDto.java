package it.mediflow.sportello.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.mediflow.sportello.entity.Prenotazioni;
import it.mediflow.sportello.enums.StatoPrenotazione;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrenotazioniDto implements Serializable {

    private Long id;

    private LocalDate dataPrenotazione;

    private LocalDate dataVisita;

    private StatoPrenotazione stato;

    @NotNull(message = "Paziente Obbligatorio")
    @Valid
    private PazientiDto paziente;

    @NotNull(message = "Medico Obbligatorio")
    @Valid
    private MediciDto medico;

    private String annotazioni;
}