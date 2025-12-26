package org.origami.ws.mappers;

import org.origami.ws.dto.ReporteBandejaTareasDTO;
import org.origami.ws.entities.origami.TareasHistoricas;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReporteTareasRealizadasMapper {

    ReporteBandejaTareasDTO toDTO(TareasHistoricas tareas);

    TareasHistoricas toEntity(ReporteBandejaTareasDTO dto);

    List<ReporteBandejaTareasDTO> toListDTO(List<TareasHistoricas> tareas);

}
