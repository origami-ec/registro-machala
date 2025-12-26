package org.origami.ws.mappers;

import org.origami.ws.dto.RolDTO;
import org.origami.ws.entities.origami.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface RolMapper {

    RolDTO toDTO(Rol rol);

    Rol toEntity(RolDTO rolDTO);

}
