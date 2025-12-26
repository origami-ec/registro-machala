package org.origami.ws.resource;

import org.origami.ws.entities.origami.Dominio;
import org.origami.ws.service.DominioServicice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping( "/origami/api/")
public class DominioResource {

    @Autowired
    private DominioServicice dominioService;

    @RequestMapping(value = "dominio/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Dominio data) {
        return new ResponseEntity<>(dominioService.findAll(data), HttpStatus.OK);
    }
}
