package org.origami.ws.mappers;

import org.origami.ws.dto.RolUsuarioDTO;
import org.origami.ws.entities.origami.RolUsuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface RolUsuarioMapper {

    RolUsuarioDTO toDTO(RolUsuario rolUsuario);

    RolUsuario toEntity(RolUsuarioDTO rolUsuarioDTO);

}
