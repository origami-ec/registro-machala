/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.origami.ws.mappers;

import java.util.List;

import org.origami.ws.dto.ReporteBandejaTareasDTO;
import org.origami.ws.entities.origami.TareasActivas;
import org.mapstruct.Mapper;

/**
 * @author Origami
 */
@Mapper(componentModel = "spring")
public interface ReporteBandejaTareasMapper {

    ReporteBandejaTareasDTO toDTO(TareasActivas tareas);

    TareasActivas toEntity(ReporteBandejaTareasDTO dto);

    List<ReporteBandejaTareasDTO> toListDTO(List<TareasActivas> tareas);

}
