package it.mediflow.sportello.repository;

import it.mediflow.sportello.entity.Specializzazione;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SpecializzazioneRepository extends ListCrudRepository<Specializzazione, Long> {
    List<Specializzazione> findByDescrizione(String descrizione);
}