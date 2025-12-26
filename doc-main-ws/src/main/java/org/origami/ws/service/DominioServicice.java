package org.origami.ws.service;

import org.origami.ws.entities.origami.Dominio;
import org.origami.ws.repository.origami.DominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DominioServicice {

    @Autowired
    private DominioRepository dominioRepository;

    public Dominio find(Dominio data) {
        Optional<Dominio> dataDB = dominioRepository.findOne(Example.of(data));
        return dataDB.orElse(null);
    }

    public List<Dominio> findAll(Dominio data) {
        return dominioRepository.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "id"));
    }

}
