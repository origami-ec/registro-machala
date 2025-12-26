package org.origami.docs.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.origami.docs.entity.Archivo;
import org.origami.docs.model.ArchivoDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArchivoMapper {

    @Mapping(source = "entity._id", target = "id")
    ArchivoDto toDto(Archivo entity);

    @Mapping(source = "dto.id", target = "_id")
    Archivo toEntity(ArchivoDto dto);

    List<ArchivoDto> toDto(List<Archivo> list);

}
