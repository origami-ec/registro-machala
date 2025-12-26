package org.origami.ws.mappers;

import org.origami.ws.dto.TipoTramiteDTO;
import org.origami.ws.entities.origami.TipoTramite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface TipoTramiteMapper {

    TipoTramiteDTO toDTO(TipoTramite tipoTramite);

    TipoTramite toEntity(TipoTramiteDTO tipoTramiteDTO);
}
