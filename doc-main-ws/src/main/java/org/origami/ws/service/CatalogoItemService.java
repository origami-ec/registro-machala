package org.origami.ws.service;

import org.origami.ws.entities.origami.Catalogo;
import org.origami.ws.entities.origami.CatalogoItem;
import org.origami.ws.repository.origami.CatalogoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoItemService {
    @Autowired
    private CatalogoItemRepository service;

    public CatalogoItem registrar(CatalogoItem data) {
        try {
            return service.save(data);
        } catch (Exception e) {
            return null;
        }

    }

    public List<Catalogo> buscarCatalogo(String code) {
        try {
            return service.findBusquedaCatalogo(code);
        } catch (Exception e) {
            return null;
        }

    }

    public List<CatalogoItem> buscarCatalogoItem(String code) {
        try {
            return service.findByCatalogo_Nombre(code);
        } catch (Exception e) {
            return null;
        }
    }

    public CatalogoItem find(CatalogoItem data) {
        try {
            return service.findOne(Example.of(data)).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

}
