package org.origami.ws.mappers;

import org.origami.ws.dto.CatEnteDTO;
import org.origami.ws.entities.origami.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatEnteMapper {

    CatEnteDTO toDTO(Persona persona);

    Persona toEntity(CatEnteDTO catEnteDTO);

}
