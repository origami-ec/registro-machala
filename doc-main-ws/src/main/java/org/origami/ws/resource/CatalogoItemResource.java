package org.origami.ws.resource;


import org.origami.ws.entities.origami.CatalogoItem;
import org.origami.ws.service.CatalogoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/origami/api/")
public class CatalogoItemResource {
    @Autowired
    private CatalogoItemService service;

    @RequestMapping(value = "catalogoItem/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody CatalogoItem data) {
        try {
            return new ResponseEntity<>(service.registrar(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "catalogoItem/find/{code}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarCatalogo(@PathVariable String code) {
        return new ResponseEntity<>(service.buscarCatalogo(code).get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "catalogoItem/buscar/{code}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarCatalogoItem(@PathVariable String code) {
        return new ResponseEntity<>(service.buscarCatalogoItem(code), HttpStatus.OK);
    }

    @RequestMapping(value = "catalogoItem/find", method = RequestMethod.POST)
    public ResponseEntity<?> buscarCatalogoItem(@Valid CatalogoItem data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

}
