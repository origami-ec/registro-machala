package org.origami.docs.service;

import org.origami.docs.entity.Indexacion;
import org.origami.docs.repository.IndexacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexacionService {

    @Autowired
    private IndexacionRepository repository;

    public Map<String, List> find(Indexacion data, Pageable pageable) {
        Map map = new HashMap<>();
        Page<Indexacion> result = repository.findAll(Example.of(data), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public List<Indexacion> consultar() {
        List<Indexacion> list = repository.findAllByEstado(Boolean.TRUE);
        return list;
    }

    public Indexacion consultar(String descripcion) {
        Indexacion indexacion = repository.findByDescripcion(descripcion);
        return indexacion;
    }

    public Indexacion guardar(Indexacion indexacion) {
        repository.save(indexacion);
        return indexacion;
    }

    public Indexacion validarIndexacionFormulario(Indexacion indexacion) {
        Indexacion index = repository.findByDescripcion(indexacion.getDescripcion().trim());
        return index;
    }

}

