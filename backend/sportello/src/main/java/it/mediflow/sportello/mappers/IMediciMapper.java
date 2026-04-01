package it.mediflow.sportello.mappers;

import it.mediflow.sportello.entity.Medici;
import it.mediflow.sportello.web.dto.MediciDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IMediciMapper extends IMappers<Medici, MediciDto> {
}
