package org.origami.ws.resource;

import org.origami.ws.repository.origami.ServicioDisponibleRepository;
import org.origami.ws.entities.origami.ServicioDisponible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/origami/api/")
public class ServicioDisponibleResource {

    @Autowired
    private ServicioDisponibleRepository servicioDisponibleRepository;

    @RequestMapping(value = "servicioDisponible/find", method = RequestMethod.GET)
    public ResponseEntity<ServicioDisponible> find() {
        return new ResponseEntity<>(servicioDisponibleRepository.findById(1).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "servicioDisponible/save", method = RequestMethod.POST)
    public ResponseEntity<ServicioDisponible> save(@RequestBody ServicioDisponible data) {
        return new ResponseEntity<>(servicioDisponibleRepository.save(data), HttpStatus.OK);
    }

}
