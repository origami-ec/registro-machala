/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.origami.ws.resource;

import java.util.List;

import org.origami.ws.repository.origami.AppsRepository;
import org.origami.ws.entities.origami.Apps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Origami
 */
@RestController
@RequestMapping("/origami/api/")
public class AppsResource {

    @Autowired
    private AppsRepository appsRepository;

    @RequestMapping(value = "apps/find", method = RequestMethod.GET)
    public ResponseEntity<List<Apps>> find() {
        return new ResponseEntity<>(appsRepository.findAll(), HttpStatus.OK);
    }

}
