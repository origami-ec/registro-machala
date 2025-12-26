package org.origami.ws.mappers;

import org.origami.ws.dto.UsuarioResponsableDto;
import org.origami.ws.entities.origami.UsuarioResponsable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface UsuarioResponsableMapper {

    @Mappings({@Mapping(target = "usuario", ignore = true)})
    UsuarioResponsableDto toDTO(UsuarioResponsable entity);

    @Mappings({@Mapping(target = "usuario", ignore = true)})
    UsuarioResponsable toEntity(UsuarioResponsableDto dto);

    List<UsuarioResponsableDto> toDTO(List<UsuarioResponsable> list);


}
