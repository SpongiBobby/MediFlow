package it.mediflow.sportello.web.controller;

import it.mediflow.sportello.service.IPazientiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/pazienti")
class PazientiController {

    private final IPazientiService pazientiService;

}
