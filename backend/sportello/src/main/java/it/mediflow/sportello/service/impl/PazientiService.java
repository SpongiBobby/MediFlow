package it.mediflow.sportello.service.impl;

import it.mediflow.sportello.entity.Pazienti;
import it.mediflow.sportello.exceptions.ConflictException;
import it.mediflow.sportello.exceptions.NotFoundException;
import it.mediflow.sportello.mappers.IPazientiMapper;
import it.mediflow.sportello.repository.PazientiRepository;
import it.mediflow.sportello.repository.PrenotazioniRepository;
import it.mediflow.sportello.repository.specification.SearchSpecification;
import it.mediflow.sportello.service.IPazientiService;
import it.mediflow.sportello.web.dto.PazientiDto;
import it.mediflow.sportello.web.dto.PazientiFilterDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PazientiService implements IPazientiService {

    private final PazientiRepository pazientiRepository;
    private final PrenotazioniRepository prenotazioniRepository;
    private final IPazientiMapper pazientiMapper;

    @Override
    public Page<PazientiDto> ricercaPazienti(PazientiFilterDto filters, Pageable pageable) {
        Specification<Pazienti> spec = SearchSpecification.ricercaPazienti(filters);

        return pazientiRepository.findAll(spec, pageable).map(pazientiMapper::toDTO);
    }

    @Override
    public PazientiDto dettaglioPaziente(Long id) {
        Optional<Pazienti> optPaziente = pazientiRepository.findById(id);
        Pazienti pazienti = optPaziente.orElseThrow(() -> new NotFoundException("Paziente non trovato"));

        return pazientiMapper.toDTO(pazienti);
    }

    @Override
    public PazientiDto dettaglioPazientePerCodiceFiscale(String cf) {
        Optional<Pazienti> optPaziente = pazientiRepository.findByCodiceFiscaleContainsIgnoreCase(cf);
        Pazienti pazienti = optPaziente.orElseThrow(() -> new NotFoundException("Paziente non trovato"));

        return pazientiMapper.toDTO(pazienti);
    }

    @Override
    public List<String> ricercaPerCodiceFiscale(String cf) {
        Pageable page = PageRequest.of(0, 10);
        List<Pazienti> pazienti = pazientiRepository.findDistinctByCodiceFiscaleContainsIgnoreCase(cf, page);

        return pazienti.stream().map(Pazienti::getCodiceFiscale).toList();
    }

    @Override
    @Transactional
    public PazientiDto salvaPaziente(PazientiDto paziente) {
        Pazienti objSaved = pazientiMapper.toEntity(paziente);
        objSaved = pazientiRepository.save(objSaved);

        return pazientiMapper.toDTO(objSaved);
    }

    @Override
    @Transactional
    public void eliminaPaziente(Long id) {
        if(!pazientiRepository.existsById(id))
            throw new NotFoundException("Paziente non trovato");

        if(prenotazioniRepository.existsByPaziente_Id(id))
            throw new ConflictException("Impossibile eliminare un paziente con prenotazioni");

        pazientiRepository.deleteById(id);
    }
}
