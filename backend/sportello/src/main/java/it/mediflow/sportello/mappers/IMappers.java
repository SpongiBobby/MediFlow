package it.mediflow.sportello.mappers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMappers<E, D> {
    /**
     * Converte una entity in un Data Trasfer Object (DTO).
     *
     * @param entity l'entity da convertire
     * @return il DTO convertito
     */
    D toDTO(E entity);

    /**
     * Converte una lista di di DTO in una lista di Entity
     *
     * @param entities la lista di DTO da convertire
     * @return la lista di Entity convertita
     */
    List<D> toDTOs(List<E> entities);

    /**
     * Converti un Data Transfer Object (DTO) in un entity.
     *
     * @param dto Il DTO da convertire
     * @return l'entity convertita
     */
    E toEntity(D dto);
}
