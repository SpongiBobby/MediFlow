package it.mediflow.sportello.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mediflow.sportello.service.ISpecializzazioneService;
import it.mediflow.sportello.web.dto.SpecializzazioneDto;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/specializzazione")
class SpecializzazioneController {

    private final ISpecializzazioneService specializzazioneService;

    @Operation(
            summary = "Recupero di tutte le specializzazione",
            description = "Recupero di tutte le specializzazione dei medici"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Specializzazioni recuperate con successo"),
            @ApiResponse(responseCode = "404", description = "Nessuna specializzazione trovata"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server durante il recupero delle specializzazioni")
    })
    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpecializzazioneDto>> recuperoSpecializzazioni() {
        return ResponseEntity.ok(specializzazioneService.ricecercaSpecializzazione());
    }

}
