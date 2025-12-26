package org.origami.ws.mappers;

import java.util.List;

import org.origami.ws.dto.UsuarioDTO;
import org.origami.ws.entities.security.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);
    
    List<UsuarioDTO> toListDTO(List<Usuario> usuarios);

}
