package it.mediflow.sportello.web.controller;

import it.mediflow.sportello.service.IMediciService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/medici")
class MediciController {

    private final IMediciService mediciService;

}
