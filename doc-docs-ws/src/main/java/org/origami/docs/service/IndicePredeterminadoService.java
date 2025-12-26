package org.origami.docs.service;

import org.origami.docs.entity.IndicePredeterminado;
import org.origami.docs.repository.IndicePredeterminadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicePredeterminadoService {
    @Autowired
    private IndicePredeterminadoRepository repository;

    public List<IndicePredeterminado> listarIndicesPredeterminado(){
        return repository.findAll();
    }

}
