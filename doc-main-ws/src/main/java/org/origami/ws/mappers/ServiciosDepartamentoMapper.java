package org.origami.ws.mappers;

import org.origami.ws.dto.ServiciosDepartamentoItemsDTO;
import org.origami.ws.entities.origami.ServiciosDepartamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface ServiciosDepartamentoMapper {

    ServiciosDepartamentoItemsDTO toDTO(ServiciosDepartamento serviciosDepartamento);

    ServiciosDepartamento toEntity(ServiciosDepartamentoItemsDTO serviciosDepartamentoItemsDTO);

}
