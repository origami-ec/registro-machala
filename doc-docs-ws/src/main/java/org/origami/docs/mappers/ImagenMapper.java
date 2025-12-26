package org.origami.docs.mappers;

import org.mapstruct.Mapper;
import org.origami.docs.entity.Imagen;
import org.origami.docs.model.ImagenDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    ImagenDto toDto(Imagen entity);

    Imagen toEntity(ImagenDto dto);

    List<ImagenDto> toDto(List<Imagen> list);
}
