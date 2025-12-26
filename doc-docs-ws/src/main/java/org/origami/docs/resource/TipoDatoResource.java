package org.origami.docs.resource;

import org.origami.docs.service.TipoDatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/origami/docs/")
public class TipoDatoResource {

    @Autowired
    private TipoDatoService service;

    @RequestMapping(value = "tipoDatos", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.listarTipoDatos(), HttpStatus.OK);
    }

}
