package it.mediflow.sportello.repository;

import it.mediflow.sportello.entity.Pazienti;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface PazientiRepository extends ListCrudRepository<Pazienti, Long>, JpaSpecificationExecutor<Pazienti> {
    List<Pazienti> findDistinctByCodiceFiscaleContainsIgnoreCase(String codiceFiscale, Pageable pageable);
    Optional<Pazienti> findByCodiceFiscaleContainsIgnoreCase(String codiceFiscale);
}