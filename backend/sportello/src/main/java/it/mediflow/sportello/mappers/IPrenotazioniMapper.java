package it.mediflow.sportello.mappers;

import it.mediflow.sportello.entity.Prenotazioni;
import it.mediflow.sportello.web.dto.PrenotazioniDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPrenotazioniMapper extends IMappers<Prenotazioni, PrenotazioniDto>{
}
