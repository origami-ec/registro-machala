package org.origami.ws.mappers;

import org.origami.ws.dto.SolicitudDocumentosDTO;
import org.origami.ws.entities.origami.SolicitudDocumentos;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartamentoMapper.class})
public interface SolicitudDocumentosMapper {

    SolicitudDocumentosDTO toDTO(SolicitudDocumentos solicitudDocumentos);

    SolicitudDocumentos toEntity(SolicitudDocumentosDTO solicitudDocumentosDTO);

    List<SolicitudDocumentosDTO> toDTO(List<SolicitudDocumentos> solicitudDocumentos);

}
