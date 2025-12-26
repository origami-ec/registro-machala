package org.origami.ws.mappers;

import org.origami.ws.dto.TipoTramitePublicDTO;
import org.origami.ws.entities.origami.TipoTramite;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoTramitePublicMapper {

    TipoTramitePublicDTO toDTO(TipoTramite tipoTramite);

    TipoTramite toEntity(TipoTramitePublicDTO tipoTramiteDTO);

    List<TipoTramitePublicDTO> toDto(List<TipoTramite> entity);

}
