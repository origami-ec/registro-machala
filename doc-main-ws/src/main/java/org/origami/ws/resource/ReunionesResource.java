package org.origami.ws.resource;

import org.origami.ws.entities.origami.Reuniones;
import org.origami.ws.repository.origami.ReunionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origami/api/")
public class ReunionesResource {

    @Autowired
    private ReunionesRepository repository;

    @RequestMapping(value = "reuniones/find", method = RequestMethod.GET)
    public ResponseEntity<?> find() {
        Pageable pageable  = PageRequest.of(0, 100, Sort.Direction.DESC, "id");
        Page<Reuniones> result = repository.findAll(pageable);
        return new ResponseEntity<>(result.getContent(), HttpStatus.OK);
    }

    @RequestMapping(value = "reuniones/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Reuniones data) {
        return new ResponseEntity<>(repository.save(data), HttpStatus.OK);
    }

    @RequestMapping(value = "reuniones/eliminar", method = RequestMethod.POST)
    public ResponseEntity<?> eliminar(@RequestBody Reuniones data) {
        repository.delete(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
