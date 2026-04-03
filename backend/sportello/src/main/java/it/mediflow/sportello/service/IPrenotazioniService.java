package it.mediflow.sportello.service;


import it.mediflow.sportello.web.dto.PrenotazioniDto;
import it.mediflow.sportello.web.dto.PrenotazioniFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPrenotazioniService {

    Page<PrenotazioniDto> ricercaPrenotazioni(PrenotazioniFilterDto filters, Pageable pageable);
    PrenotazioniDto dettaglioPrenotazione(Long id);
    PrenotazioniDto salvaPrenotazione(PrenotazioniDto prenotazione);
    void eliminaPrenotazione(Long id);
}
