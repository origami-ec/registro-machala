package org.origami.ws.service;

import org.origami.ws.entities.origami.ServiciosDepartamentoRequisitos;
import org.origami.ws.repository.origami.ServiciosDepartamentoItemsRequisitoRepository;
import org.origami.ws.dto.ServiciosDepartamentoItemsRequisitosDTO;
import org.origami.ws.mappers.ServiciosDepartamentoRequisitosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiciosDepartamentoItemsRequisitoServices {
    @Autowired
    private ServiciosDepartamentoItemsRequisitoRepository repository;
    @Autowired
    private ServiciosDepartamentoRequisitosMapper mapper;

    public List<ServiciosDepartamentoItemsRequisitosDTO> findDTO(Long id) {
        return mapper.toDto(repository.findByTipoTramite_Id(id));
    }

    public List<ServiciosDepartamentoRequisitos> findRequisitosByServicioDepartamentoAbreviatura(String abrev) {
        return repository.findAllByServiciosDepartamento_Abreviatura(abrev);
    }

    public ServiciosDepartamentoRequisitos find(ServiciosDepartamentoRequisitos data) {
        return repository.findOne(Example.of(data)).get();
    }

    public List<ServiciosDepartamentoRequisitos> findAll(ServiciosDepartamentoRequisitos data) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return repository.findAll(Example.of(data, matcher), Sort.by(Sort.Direction.ASC, "orden"));
    }

    public ServiciosDepartamentoRequisitos registrarActualizar(ServiciosDepartamentoRequisitos data) {
        System.out.println("actualizar requisito " + data.toString());
        return repository.save(data);
    }

    public void eliminar(ServiciosDepartamentoRequisitos data) {
        repository.delete(data);
    }
}
