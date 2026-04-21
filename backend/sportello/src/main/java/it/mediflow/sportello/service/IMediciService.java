package it.mediflow.sportello.service;

import it.mediflow.sportello.web.dto.MediciDto;
import it.mediflow.sportello.web.dto.MediciFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMediciService {
    Page<MediciDto> ricercaMedici(MediciFilterDto filters, Pageable pageable);
    MediciDto dettaglioMedico(Long id);
    List<String> ricercaPerCodiceFiscale(String cf);
    List<MediciDto> dettaglioPerCodiceFiscale(String cf);
    MediciDto salvaMedico(MediciDto medico);
    void eliminaMedico(Long id);
}
