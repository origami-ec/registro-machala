package org.origami.ws.resource;

import org.origami.ws.entities.origami.MenuRol;
import org.origami.ws.service.PubGuiMenuRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/origami/api/")
public class MenuRolResource {

    @Autowired
    private PubGuiMenuRolService service;

    @RequestMapping(value = "menuRol/find", method = RequestMethod.GET)
    public ResponseEntity<List<MenuRol>> find(@Valid MenuRol data) {
        return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
    }
}
