package it.mediflow.sportello.service.impl;

import it.mediflow.sportello.entity.Specializzazione;
import it.mediflow.sportello.mappers.ISpecializzazioneMapper;
import it.mediflow.sportello.repository.SpecializzazioneRepository;
import it.mediflow.sportello.service.ISpecializzazioneService;
import it.mediflow.sportello.web.dto.SpecializzazioneDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializzazioneService implements ISpecializzazioneService {

    private final SpecializzazioneRepository specializzazioneRepository;
    private final ISpecializzazioneMapper specializzazioneMapper;

    @Override
    public List<SpecializzazioneDto> ricecercaSpecializzazione() {
        List<Specializzazione> specializzazione = specializzazioneRepository.findAll();
        return specializzazioneMapper.toDTOs(specializzazione);
    }
}
