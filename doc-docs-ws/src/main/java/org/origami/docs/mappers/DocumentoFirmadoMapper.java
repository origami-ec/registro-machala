package org.origami.docs.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.origami.docs.entity.DocumentoFirmado;
import org.origami.docs.model.DocumentoFirmadoDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentoFirmadoMapper {
    @Mapping(source = "entity.usuario.usuario", target = "usuario")
    @Mapping(source = "entity.tipo.valor", target = "tipo")
    DocumentoFirmadoDTO toDto(DocumentoFirmado entity);

    @Mapping(source = "dto.usuario", target = "usuario.usuario")
    @Mapping(source = "dto.tipo", target = "tipo.valor")
    DocumentoFirmado toEntity(DocumentoFirmadoDTO dto);

    List<DocumentoFirmadoDTO> toDto(List<DocumentoFirmado> list);
}
