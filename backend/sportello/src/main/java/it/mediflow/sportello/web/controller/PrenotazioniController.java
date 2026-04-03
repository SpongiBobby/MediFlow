package it.mediflow.sportello.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mediflow.sportello.service.IPrenotazioniService;
import it.mediflow.sportello.web.dto.PrenotazioniDto;
import it.mediflow.sportello.web.dto.PrenotazioniFilterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prenotazioni")
class PrenotazioniController {

    private final IPrenotazioniService prenotazioniService;


    @Operation(
            summary = "Ricerca prenotazioni con filtri",
            description = "Restituisce una lista paginata di prenotazioni filtrata in base ai criteri specificati nel corpo della richiesta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ricerca completata con successo"),
            @ApiResponse(responseCode = "400", description = "I filtri forniti non sono validi o il corpo della richiesta è malformato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'elaborazione della ricerca")
    })
    @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PrenotazioniDto>> filtro(Pageable pageable, @RequestBody PrenotazioniFilterDto filter) {
        return ResponseEntity.ok(prenotazioniService.ricercaPrenotazioni(filter, pageable));
    }


    @Operation(
            summary = "Dettaglio prenotazione per ID",
            description = "Recupera le informazioni  di una prenotazione dato il suo Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dettaglio recuperato con successo"),
            @ApiResponse(responseCode = "400", description = "L'ID fornito non è valido"),
            @ApiResponse(responseCode = "404", description = "Nessuna prenotazione trovata con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il recupero del dettaglio della prenotazione")
    })
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PrenotazioniDto> dettaglio(@PathVariable("id") Long id) {
        return ResponseEntity.ok(prenotazioniService.dettaglioPrenotazione(id));
    }

    @Operation(
            summary = "Creazione di una nuova prenotazione",
            description = "Crea una nuova prenotazione nel sistema a partire dai dati forniti nel corpo della richiesta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prenotazione effettuata con successo"),
            @ApiResponse(responseCode = "400", description = "I dati della prenotazione non superano la validazione o il corpo della richiesta è malformato"),
            @ApiResponse(responseCode = "409", description = "Esiste già una prenotazione con gli stessi dati identificativi"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il salvataggio della prenotazione")
    })
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PrenotazioniDto> salvaPrenotazione(@RequestBody @Valid PrenotazioniDto prenotazione) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prenotazioniService.salvaPrenotazione(prenotazione));
    }


    @Operation(
            summary = "Eliminazione di una prenotazione",
            description = "Eliminazione di una prenotazione con passaggio a stato Annullato"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prenotazione annullata con successo"),
            @ApiResponse(responseCode = "400", description = "L'ID fornito non è in un formato valido"),
            @ApiResponse(responseCode = "404", description = "Nessuna prenotazione trovata con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'operazione di eliminazione della prenotazione")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminaPrenotazione(@PathVariable("id") Long id) {
        prenotazioniService.eliminaPrenotazione(id);
        return ResponseEntity.noContent().build();
    }

}
