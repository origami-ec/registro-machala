package org.origami.ws.mappers;

import org.origami.ws.dto.AclRegistroUserDTO;
import org.origami.ws.entities.origami.RegistroUsuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AclRegistroUserMapper {

    AclRegistroUserDTO toDTO(RegistroUsuario aclRegistroUser);

    RegistroUsuario toEntity(AclRegistroUserDTO aclRegistroUserDTO);

}
