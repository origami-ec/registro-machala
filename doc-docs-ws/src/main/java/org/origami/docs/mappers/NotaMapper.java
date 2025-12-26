package org.origami.docs.mappers;

import org.mapstruct.Mapper;
import org.origami.docs.entity.Nota;
import org.origami.docs.model.NotaDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotaMapper {

    NotaDto toDto(Nota entity);

    Nota toEntity(NotaDto dto);

    List<NotaDto> toDto(List<Nota> list);


}
