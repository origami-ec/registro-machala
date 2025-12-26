package org.origami.ws.mappers;

import org.origami.ws.dto.SolicitudServiciosDTO;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface SolicitudServicioMapper {

    @Mappings({@Mapping(target = "tramite.solicitante", ignore = true)})
    SolicitudServiciosDTO toDTO(SolicitudServicios solicitudServicios);

    SolicitudServicios toEntity(SolicitudServiciosDTO solicitudServiciosDTO);

    List<SolicitudServiciosDTO> toDTO(List<SolicitudServicios> solicitudServiciosList);

}
