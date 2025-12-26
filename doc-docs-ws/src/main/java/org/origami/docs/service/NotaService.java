package org.origami.docs.service;

import org.origami.docs.entity.Nota;
import org.origami.docs.mappers.NotaMapper;
import org.origami.docs.model.NotaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaService {


    @Autowired
    private NotaMapper notaMapper;

    public NotaDto guardar(NotaDto notaDto) {
        Nota nota = notaMapper.toEntity(notaDto);
    //    nota = notaRepository.save(nota);
        return notaMapper.toDto(nota);
    }

}
