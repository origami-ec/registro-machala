package org.origami.ws.mappers;

import org.origami.ws.dto.ServiciosDepartamentoItemsRequisitosDTO;
import org.origami.ws.entities.origami.ServiciosDepartamentoRequisitos;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiciosDepartamentoRequisitosMapper {

    ServiciosDepartamentoItemsRequisitosDTO toDto(ServiciosDepartamentoRequisitos entity);

    ServiciosDepartamentoRequisitos toEntity(ServiciosDepartamentoItemsRequisitosDTO dto);


    List<ServiciosDepartamentoItemsRequisitosDTO> toDto(List<ServiciosDepartamentoRequisitos> entity);

}
