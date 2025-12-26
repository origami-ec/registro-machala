package org.origami.ws.service;

import org.origami.ws.entities.rrhh.RecursoHumano;
import org.origami.ws.repository.rrhh.RecursoHumanoRepository;
import org.origami.ws.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecursoHumanoService {

    @Autowired
    private RecursoHumanoRepository repository;

    public RecursoHumano find(RecursoHumano data) {
        return repository.findOne(Example.of(data)).orElse(null);
    }

    public RecursoHumano findByPersona(String cedula){
        return repository.findTopByPersonaRH_CedulaOrderByIdDesc(cedula);
    }

    public List<RecursoHumano> findAll(RecursoHumano data) {
        return repository.findAll(Example.of(data));
    }

    public List<RecursoHumano> consultarRrhhXDepartamento(Long departamento) {
        return repository.findAllByTipoLugarAndLugarTrabajo(Constantes.tipoLugarDep, departamento);
    }

    public List<RecursoHumano> consultarRrhhXDepartamento(Long tipoLugar, Long departamento) {
        return repository.findAllByTipoLugarAndLugarTrabajo(tipoLugar, departamento);
    }


    public Map<String, List> findAll(RecursoHumano data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<RecursoHumano> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }
}
