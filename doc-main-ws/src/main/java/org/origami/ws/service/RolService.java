package org.origami.ws.service;

import org.origami.ws.entities.origami.Rol;
import org.origami.ws.repository.origami.RolRepository;
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
public class RolService {

    @Autowired
    private RolRepository rolRepository;


    public Rol findOne(Rol data){
        return rolRepository.findOne(Example.of(data)).get();
    }

    public List<Rol> findAll(Rol data){
        return rolRepository.findAll(Example.of(data));
    }

    public Map<String, List> find(Rol data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<Rol> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAll();
        customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = rolRepository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(rolRepository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Rol saveRol(Rol rol){
        rol.setDirector(Boolean.FALSE);
        rol.setEsSubDirector(Boolean.FALSE);
        return rolRepository.save(rol);
    }
}
