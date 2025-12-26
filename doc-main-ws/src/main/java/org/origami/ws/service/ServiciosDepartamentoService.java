package org.origami.ws.service;

import org.origami.ws.entities.origami.ServiciosDepartamento;
import org.origami.ws.repository.origami.ServiciosDepartamentoItemRepository;

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
public class ServiciosDepartamentoService {
    @Autowired
    private ServiciosDepartamentoItemRepository repository;

    public ServiciosDepartamento find(ServiciosDepartamento data) {
        return repository.findOne(Example.of(data, ExampleMatcher.matchingAll())).get();
    }

    public Map<String, List> find(ServiciosDepartamento data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();

        Page<ServiciosDepartamento> result = repository.findAll(Example.of(data, matcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, matcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public ServiciosDepartamento registrarActualizar(ServiciosDepartamento data) {
        return repository.save(data);
    }

    public void eliminar(ServiciosDepartamento data) {
        repository.delete(data);
    }

}
