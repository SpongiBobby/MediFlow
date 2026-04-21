package it.mediflow.sportello.repository;

import it.mediflow.sportello.entity.Prenotazioni;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

public interface PrenotazioniRepository extends ListCrudRepository<Prenotazioni, Long>, JpaSpecificationExecutor<Prenotazioni> {
    boolean existsByPaziente_Id(Long id);
    boolean existsByMedico_Id(Long id);
}