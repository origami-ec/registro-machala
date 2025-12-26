package org.origami.ws.resource;

import org.origami.ws.entities.origami.Rol;
import org.origami.ws.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/origami/api/")
public class RolResource {

    @Autowired
    private RolService service;

    @RequestMapping(value = "rol/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid Rol data) {
        return new ResponseEntity<>(service.findOne(data), HttpStatus.OK);
    }

    @RequestMapping(value = "roles/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Rol data) {
        return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
    }

    @RequestMapping(value = "roles", method = RequestMethod.GET)
    public ResponseEntity<List<Rol>> findAllRoles(@Valid Rol data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "rol/save", method = RequestMethod.POST)
    public ResponseEntity<?> findAllRoles(@RequestBody Rol rol) {
        return new ResponseEntity<>(service.saveRol(rol), HttpStatus.OK);
    }

}
