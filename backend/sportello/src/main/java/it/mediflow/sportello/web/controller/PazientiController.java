package it.mediflow.sportello.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mediflow.sportello.annotations.FiscalCode;
import it.mediflow.sportello.service.IPazientiService;
import it.mediflow.sportello.web.dto.PazientiDto;
import it.mediflow.sportello.web.dto.PazientiFilterDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("/pazienti")
class PazientiController {

    private final IPazientiService pazientiService;

    @Operation(
            summary = "Ricerca pazienti con filtri",
            description = "Restituisce una lista paginata di pazienti filtrata in base ai criteri specificati nel corpo della richiesta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ricerca completata con successo"),
            @ApiResponse(responseCode = "400", description = "I filtri forniti non sono validi o il corpo della richiesta è malformato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'elaborazione della ricerca")
    })
    @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PazientiDto>> filtro(Pageable pageable, @RequestBody PazientiFilterDto filter) {
        return ResponseEntity.ok(pazientiService.ricercaPazienti(filter, pageable));
    }


    @Operation(
            summary = "Dettaglio paziente per ID",
            description = "Recupera le informazioni  di un paziente dato il suo Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dettaglio recuperato con successo"),
            @ApiResponse(responseCode = "400", description = "L'ID fornito non è valido"),
            @ApiResponse(responseCode = "404", description = "Nessun paziente trovato con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il recupero del dettaglio")
    })
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PazientiDto> dettaglio(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pazientiService.dettaglioPaziente(id));
    }


    @Operation(
            summary = "Ricerca paziente per codice fiscale",
            description = "Restituisce una lista di codici fiscali"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ricerca completata con successo"),
            @ApiResponse(responseCode = "400", description = "Il codice fiscale fornito non è in un formato valido"),
            @ApiResponse(responseCode = "404", description = "Nessun paziente trovato con il codice fiscale specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'elaborazione della ricerca")
    })
    @GetMapping(value = "/cf/{cf}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> ricercaCF(@PathVariable("cf") @Valid @FiscalCode String cf) {
        return ResponseEntity.ok(pazientiService.ricercaPerCodiceFiscale(cf));
    }


    @Operation(
            summary = "Creazione di un nuovo paziente",
            description = "Crea un nuovo paziente nel sistema a partire dai dati forniti nel corpo della richiesta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paziente creato con successo"),
            @ApiResponse(responseCode = "400", description = "I dati del paziente non superano la validazione o il corpo della richiesta è malformato"),
            @ApiResponse(responseCode = "409", description = "Esiste già un paziente con gli stessi dati identificativi"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il salvataggio")
    })
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PazientiDto> salvaPaziente(@RequestBody @Valid PazientiDto paziente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pazientiService.salvaPaziente(paziente));
    }


    @Operation(
            summary = "Eliminazione di un paziente",
            description = "Rimuove definitivamente dal sistema il paziente corrispondente all'ID fornito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paziente eliminato con successo"),
            @ApiResponse(responseCode = "400", description = "L'ID fornito non è in un formato valido"),
            @ApiResponse(responseCode = "404", description = "Nessun paziente trovato con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'operazione di eliminazione del paziente")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminaPaziente(@PathVariable("id") Long id) {
        pazientiService.eliminaPaziente(id);
        return ResponseEntity.noContent().build();
    }

}
