package org.origami.docs.service;

import org.origami.docs.entity.TipoDato;
import org.origami.docs.repository.TipoDatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDatoService {
    @Autowired
    private TipoDatoRepository repository;

    public List<TipoDato> listarTipoDatos(){
        return repository.findAll();
    }

}
