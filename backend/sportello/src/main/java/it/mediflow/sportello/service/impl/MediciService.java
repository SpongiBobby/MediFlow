package it.mediflow.sportello.service.impl;

import it.mediflow.sportello.entity.Medici;
import it.mediflow.sportello.entity.Specializzazione;
import it.mediflow.sportello.exceptions.ConflictException;
import it.mediflow.sportello.exceptions.NotFoundException;
import it.mediflow.sportello.mappers.IMediciMapper;
import it.mediflow.sportello.repository.MediciRepository;
import it.mediflow.sportello.repository.PrenotazioniRepository;
import it.mediflow.sportello.repository.SpecializzazioneRepository;
import it.mediflow.sportello.repository.specification.SearchSpecification;
import it.mediflow.sportello.service.IMediciService;
import it.mediflow.sportello.web.dto.MediciDto;
import it.mediflow.sportello.web.dto.MediciFilterDto;
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

@Service
@RequiredArgsConstructor
public class MediciService implements IMediciService {

    private final MediciRepository mediciRepository;
    private final PrenotazioniRepository prenotazioniRepository;
    private final SpecializzazioneRepository specializzazioneRepository;
    private final IMediciMapper mediciMapper;

    @Override
    public Page<MediciDto> ricercaMedici(MediciFilterDto filters, Pageable pageable) {
        Specification<Medici> spec = SearchSpecification.ricercaMedici(filters);

        return mediciRepository.findAll(spec, pageable).map(mediciMapper::toDTO);
    }

    @Override
    public MediciDto dettaglioMedico(Long id) {
        Optional<Medici> optMedico = mediciRepository.findById(id);
        Medici medico = optMedico.orElseThrow(() -> new NotFoundException("Medico non trovato"));

        return mediciMapper.toDTO(medico);
    }

    @Override
    public List<String> ricercaPerCodiceFiscale(String cf) {
        Pageable page = PageRequest.of(0, 10); // Limito le corrispondenze a 10
        List<Medici> medici = mediciRepository.findDistinctByCodiceFiscaleContainsIgnoreCase(cf, page);

        return medici.stream().map(Medici::getCodiceFiscale).toList();
    }

    @Override
    public List<MediciDto> dettaglioPerCodiceFiscale(String cf) {
        Pageable page = PageRequest.of(0, 10); // Limito le corrispondenze a 10
        List<Medici> medici = mediciRepository.findDistinctByCodiceFiscaleContainsIgnoreCase(cf, page);

        return mediciMapper.toDTOs(medici);
    }

    @Override
    @Transactional
    public MediciDto salvaMedico(MediciDto medico) {
        Medici objSaved = mediciMapper.toEntity(medico);

        Specializzazione specializzazione = specializzazioneRepository.findById(
                objSaved.getSpecializzazione().getId()
        ).orElseThrow(() -> new IllegalArgumentException("Specializzazione non trovata"));

        objSaved.setSpecializzazione(specializzazione);
        objSaved = mediciRepository.save(objSaved);

        return mediciMapper.toDTO(objSaved);
    }

    @Override
    @Transactional
    public void eliminaMedico(Long id) {
        if(!mediciRepository.existsById(id)) throw new NotFoundException("Medico non trovato");

        if(prenotazioniRepository.existsByMedico_Id(id))
            throw new ConflictException("Impossibile eliminare un medico con prenotazioni");

        mediciRepository.deleteById(id);
    }
}
