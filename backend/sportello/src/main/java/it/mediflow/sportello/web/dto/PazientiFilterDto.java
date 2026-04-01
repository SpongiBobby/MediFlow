package it.mediflow.sportello.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PazientiFilterDto {
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private LocalDate dataDiNascita;
    private String luogoDiNascita;
}
