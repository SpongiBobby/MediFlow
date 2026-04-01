package it.mediflow.sportello.repository;

import it.mediflow.sportello.entity.Medici;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MediciRepository extends ListCrudRepository<Medici, Long>, JpaSpecificationExecutor<Medici> {
    List<Medici> findDistinctByCodiceFiscaleLikeIgnoreCase(String codiceFiscale, Pageable pageable);
}