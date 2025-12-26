package org.origami.docs.service;

import org.origami.docs.entity.Formato;
import org.origami.docs.repository.FormatoRepository;
import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormatoService {

    @Autowired
    private FormatoRepository repository;


    public Map<String, List> find(Pageable pageable) {
        Map map = new HashMap<>();
        Page<Formato> result = repository.findAll(pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count()));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Formato guardar(Formato formato) {
        formato.setFecha(Utils.getDate(new Date()));
        return repository.save(formato);
    }


}
