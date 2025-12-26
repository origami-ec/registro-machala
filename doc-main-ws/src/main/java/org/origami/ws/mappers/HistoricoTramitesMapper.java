package org.origami.ws.mappers;

import org.origami.ws.dto.HistoricoTramitesDTO;
import org.origami.ws.entities.origami.HistoricoTramites;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoricoTramitesMapper {

    HistoricoTramitesDTO toDTO(HistoricoTramites historicoTramites);

    HistoricoTramites toEntity(HistoricoTramitesDTO historicoTramitesDTO);

}
