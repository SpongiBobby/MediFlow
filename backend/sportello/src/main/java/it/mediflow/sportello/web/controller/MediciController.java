package it.mediflow.sportello.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mediflow.sportello.annotations.FiscalCode;
import it.mediflow.sportello.service.IMediciService;
import it.mediflow.sportello.web.dto.MediciDto;
import it.mediflow.sportello.web.dto.MediciFilterDto;
import it.mediflow.sportello.web.dto.PageResonse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE}
)
@RequestMapping("/medici")
class MediciController {

    private final IMediciService mediciService;

    @Operation(
            summary = "Ricerca medici con filtri",
            description = "Restituisce una lista paginata di medici filtrata in base ai criteri specificati nel corpo della richiesta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ricerca completata con successo"),
            @ApiResponse(responseCode = "400", description = "I filtri forniti non sono validi o il corpo della richiesta è malformato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'elaborazione della ricerca")
    })
    @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResonse<MediciDto>> filtro(Pageable pageable, @RequestBody MediciFilterDto filter) {
        Page<MediciDto> result = mediciService.ricercaMedici(filter, pageable);
        return ResponseEntity.ok(PageResonse.of(result));
    }


    @Operation(
            summary = "Dettaglio medico per ID",
            description = "Recupera le informazioni  di un medico dato il suo Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dettaglio recuperato con successo"),
            @ApiResponse(responseCode = "400", description = "L'ID fornito non è valido"),
            @ApiResponse(responseCode = "404", description = "Nessun medico trovato con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il recupero del dettaglio")
    })
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MediciDto> dettaglio(@PathVariable("id") Long id) {
        return ResponseEntity.ok(mediciService.dettaglioMedico(id));
    }

    @Operation(
            summary = "Lista dei medici per Codice Fiscale",
            description = "Recupera le informazioni  dei medici in base al Codice Fiscale"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dettaglio recuperato con successo"),
            @ApiResponse(responseCode = "400", description = "Il codice fiscale fornito non è valido"),
            @ApiResponse(responseCode = "404", description = "Nessun medico trovato con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il recupero del dettaglio")
    })
    @GetMapping(value = "/detail/cf/{cf}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MediciDto>> dettaglio(@PathVariable("cf") String cf) {
        return ResponseEntity.ok(mediciService.dettaglioPerCodiceFiscale(cf));
    }

    @Operation(
            summary = "Ricerca medici per codice fiscale",
            description = "Restituisce una lista di codici fiscali"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ricerca completata con successo"),
            @ApiResponse(responseCode = "400", description = "Il codice fiscale fornito non è in un formato valido"),
            @ApiResponse(responseCode = "404", description = "Nessun medico trovato con il codice fiscale specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'elaborazione della ricerca")
    })
    @GetMapping(value = "search/cf/{cf}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> ricercaCF(@PathVariable("cf") String cf) {
        return ResponseEntity.ok(mediciService.ricercaPerCodiceFiscale(cf));
    }


    @Operation(
            summary = "Creazione di un nuovo medico",
            description = "Crea un nuovo medico nel sistema a partire dai dati forniti nel corpo della richiesta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medico creato con successo"),
            @ApiResponse(responseCode = "400", description = "I dati del medico non superano la validazione o il corpo della richiesta è malformato"),
            @ApiResponse(responseCode = "409", description = "Esiste già un medico con gli stessi dati identificativi"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il salvataggio del medico")
    })
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MediciDto> salvaMedico(@RequestBody @Valid MediciDto medico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mediciService.salvaMedico(medico));
    }


    @Operation(
            summary = "Eliminazione di un medico",
            description = "Rimuove definitivamente dal sistema il medico corrispondente all'ID fornito"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medico eliminato con successo"),
            @ApiResponse(responseCode = "400", description = "L'ID fornito non è in un formato valido"),
            @ApiResponse(responseCode = "404", description = "Nessun medico trovato con l'ID specificato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante l'operazione di eliminazione del medico")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminaMedico(@PathVariable("id") Long id) {
        mediciService.eliminaMedico(id);
        return ResponseEntity.noContent().build();
    }
}
