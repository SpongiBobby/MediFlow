package it.mediflow.sportello.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.mediflow.sportello.annotations.FiscalCode;
import it.mediflow.sportello.annotations.PhoneNumber;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediciDto implements Serializable {
    
    private Long id;
    
    @NotBlank(message = "Il nome è obbligatorio!")
    private String nome;
    
    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;
    
    @NotNull(message = "La data di nascita è obbligatoria")
    @FutureOrPresent(message = "L'inserimento di una data odierna o futura non è consentita per la data di nascita")
    private LocalDate dataDiNascita;
    
    @NotBlank(message = "Il luogo di nascita è obbligatorio")
    private String luogoDiNascita;

    @NotBlank(message = "Il codice fiscale è obbligatorio")
    @Size(max = 16, message = "Hai superato il limite massimo di caratteri per il codice fiscale")
    @FiscalCode(message = "Codice Fiscale non valido")
    private String codiceFiscale;

    @NotBlank(message = "L'indirizzo di residenza è obbligatorio")
    private String indirizzoDiResidenza;

    @NotNull(message = "La specializzazione è obbligatoria")
    private SpecializzazioneDto specializzazione;

    @PhoneNumber(message = "Il numero di telefono non è valido")
    private String telefono;

    @NotBlank(message = "Il numero di telefono è obbligatorio")
    @PhoneNumber(message = "Il numero di telefono non è valido")
    private String cellulare;
    
    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Indirizzo email non è valido")
    private String email;

    private String annotazioni;
}