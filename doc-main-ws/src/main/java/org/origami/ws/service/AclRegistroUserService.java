package org.origami.ws.service;

import org.origami.ws.entities.origami.RegistroUsuario;
import org.origami.ws.repository.origami.AclRegistroUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AclRegistroUserService {

    @Autowired
    private AclRegistroUserRepository repository;

    public RegistroUsuario find(RegistroUsuario data) {
        return repository.findOne(Example.of(data)).orElse(null);
    }

    public List<RegistroUsuario> findAll(RegistroUsuario data) {
        return repository.findAll(Example.of(data));
    }

    public RegistroUsuario save(RegistroUsuario data) {
        return repository.save(data);
    }
}
