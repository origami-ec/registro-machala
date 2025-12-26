package org.origami.ws.resource;

import com.sipios.springsearch.anotation.SearchSpec;
import org.origami.ws.entities.origami.TareasActivas;
import org.origami.ws.repository.origami.TareasActivasRepository;
import org.origami.ws.models.Data;
import org.origami.ws.service.TareasActivasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
public class TareasActivasResource {
    @Autowired
    private TareasActivasService service;
    @Autowired
    private TareasActivasRepository tareasActivasRepository;

    @RequestMapping(value = "tareaActiva/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid TareasActivas data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "tareasActivas/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid TareasActivas data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tareasActivas", method = RequestMethod.GET)
    public ResponseEntity<?> findAllPagingAndSorting(@Valid TareasActivas data, Pageable pageable) {
        Map<String, List> map = service.findPagingAndSorting(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "tareasActivas/searchUrl", method = RequestMethod.GET)
    public ResponseEntity<?> search(@SearchSpec Specification<TareasActivas> data, Pageable pageable) {
        Map<String, List> map = service.findPagingAndSorting(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "tareasActivas/cantidad", method = RequestMethod.POST)
    public ResponseEntity<?> solicitudesVentanilla(@RequestBody Data data) {
        return new ResponseEntity<>(service.cantidadTramiteUser(data), HttpStatus.OK);
    }

}
