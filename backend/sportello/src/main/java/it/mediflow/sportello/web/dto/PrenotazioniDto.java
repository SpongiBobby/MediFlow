package it.mediflow.sportello.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.mediflow.sportello.entity.Prenotazioni;
import it.mediflow.sportello.enums.StatoPrenotazione;
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

    @NotNull(message = "")
    private StatoPrenotazione stato;

    @NotNull(message = "Paziente Obbligatorio")
    private PazientiDto idPaziente;

    @NotNull(message = "Medico Obbligatorio")
    private MediciDto idMedico;

    private String annotazioni;
}