package org.origami.docs.service;

import org.origami.docs.entity.Tesauro;
import org.origami.docs.repository.TesauroRepository;
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
public class TesauroService {

    @Autowired
    private TesauroRepository repository;

    public Map<String, List> find(Tesauro data, Pageable pageable) {
        Map map = new HashMap<>();
        Page<Tesauro> result = repository.findAll(Example.of(data), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public List<Tesauro> consultar() {
        List<Tesauro> list = repository.findAllByEstado(Boolean.TRUE);
        return list;
    }

    public Tesauro consultar(String descripcion) {
        Tesauro tesauro = repository.findByPalabra(descripcion);
        return tesauro;
    }

    public Tesauro guardar(Tesauro tesauro) {
        repository.save(tesauro);
        return tesauro;
    }

    public Tesauro validarTesauroFormulario(Tesauro indexacion) {

        Tesauro index = repository.findByPalabra(indexacion.getPalabra().trim());
        return index;
    }




}
