package it.mediflow.sportello.mappers;

import it.mediflow.sportello.entity.Specializzazione;
import it.mediflow.sportello.web.dto.SpecializzazioneDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ISpecializzazioneMapper extends IMappers<Specializzazione, SpecializzazioneDto> {
}
