package it.mediflow.sportello.service.impl;

import it.mediflow.sportello.entity.Prenotazioni;
import it.mediflow.sportello.enums.StatoPrenotazione;
import it.mediflow.sportello.exceptions.NotFoundException;
import it.mediflow.sportello.mappers.IPrenotazioniMapper;
import it.mediflow.sportello.repository.PrenotazioniRepository;
import it.mediflow.sportello.repository.specification.SearchSpecification;
import it.mediflow.sportello.service.IPrenotazioniService;
import it.mediflow.sportello.web.dto.PrenotazioniDto;
import it.mediflow.sportello.web.dto.PrenotazioniFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class PrenotazioniService implements IPrenotazioniService {


    private final PrenotazioniRepository prenotazioniRepository;
    private final IPrenotazioniMapper prenotazioniMapper;

    @Override
    public Page<PrenotazioniDto> ricercaPrenotazioni(PrenotazioniFilterDto filters, Pageable pageable) {
        Specification<Prenotazioni> spec = SearchSpecification.ricercaPrenotazioni(filters);

        return prenotazioniRepository.findAll(spec, pageable).map(prenotazioniMapper::toDTO);
    }

    @Override
    public PrenotazioniDto dettaglioPrenotazione(Long id) {
        Optional<Prenotazioni> optPrenotazioni = prenotazioniRepository.findById(id);
        Prenotazioni prenotazione = optPrenotazioni.orElseThrow(() -> new NotFoundException("Prenotazione non trovata"));

        return prenotazioniMapper.toDTO(prenotazione);
    }

    @Override
    public PrenotazioniDto salvaPrenotazione(PrenotazioniDto prenotazione) {

        Prenotazioni objSaved = prenotazioniMapper.toEntity(prenotazione);
        objSaved = prenotazioniRepository.save(objSaved);

        return prenotazioniMapper.toDTO(objSaved);

    }

    @Override
    public void eliminaPrenotazione(Long id) {
        Optional<Prenotazioni> optPrenotazioni = prenotazioniRepository.findById(id);
        Prenotazioni prenotazione = optPrenotazioni.orElseThrow(() -> new NotFoundException("Prenotazione non trovata"));

        prenotazione.setStato(StatoPrenotazione.ANNULLATO);

    }
}
