package it.mediflow.sportello.service;

import it.mediflow.sportello.web.dto.PazientiDto;
import it.mediflow.sportello.web.dto.PazientiFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPazientiService {
    Page<PazientiDto> ricercaPazienti(PazientiFilterDto filters, Pageable pageable);
    PazientiDto dettaglioPaziente(Long id);
    List<String> ricercaPerCodiceFiscale(String cf);
    PazientiDto salvaPaziente(PazientiDto paziente);
    void eliminaPaziente(Long id);
}
