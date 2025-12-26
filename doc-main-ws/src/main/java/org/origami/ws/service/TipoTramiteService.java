package org.origami.ws.service;

import org.origami.ws.entities.origami.TipoTramite;
import org.origami.ws.repository.origami.TipoTramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipoTramiteService {

    @Autowired
    private TipoTramiteRepository repository;

    public TipoTramite guardar(TipoTramite data) {
        return repository.save(data);
    }

    public TipoTramite find(TipoTramite data) {
        Optional<TipoTramite> dataBD = repository.findOne(Example.of(data));
        return dataBD.orElse(null);
    }
   public TipoTramite findTipoTramite(Long id) {
        return repository.findTipoTramite(id);
    }

}
