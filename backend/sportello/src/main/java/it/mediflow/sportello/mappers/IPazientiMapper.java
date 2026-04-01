package it.mediflow.sportello.mappers;

import it.mediflow.sportello.entity.Pazienti;
import it.mediflow.sportello.web.dto.PazientiDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPazientiMapper extends IMappers<Pazienti, PazientiDto> {

}
