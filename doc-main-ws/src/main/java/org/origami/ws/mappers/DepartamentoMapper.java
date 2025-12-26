package org.origami.ws.mappers;

import org.origami.ws.dto.DepartamentoDTO;
import org.origami.ws.entities.origami.Departamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {


    DepartamentoDTO toDTO(Departamento entity);

    Departamento toEntity(DepartamentoDTO dto);

}
