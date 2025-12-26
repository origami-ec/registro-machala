package org.origami.docs.service;

import org.origami.docs.entity.Color;
import org.origami.docs.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {

    @Autowired
    private ColorRepository repository;

    public List<Color> findAll(){
        return repository.findAll();
    }


}
