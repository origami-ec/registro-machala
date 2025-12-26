package org.origami.ws.resource;

import com.sipios.springsearch.anotation.SearchSpec;
import org.origami.ws.entities.origami.TareasHistoricas;
import org.origami.ws.service.TareasHistoricasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/origami/api/")
public class TareasHistoricasResource {

    @Autowired
    private TareasHistoricasService service;


    @RequestMapping(value = "tareas/{terminado}/historicas", method = RequestMethod.GET)
    public ResponseEntity<?> search(@SearchSpec Specification<TareasHistoricas> data, Pageable pageable, @PathVariable("terminado") Boolean terminado) {
        Map<String, List> map = service.findAll(data, pageable, terminado);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "tareas/historicas/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid TareasHistoricas data, Pageable pageable) {
        Map<String, List> map = service.findAll(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "tareas/historicas/group", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        try {
            return new ResponseEntity<>(service.findAllGroupTareas(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tareas/historicas/groupReport", method = RequestMethod.GET)
    public ResponseEntity<?> reportTareas(@RequestBody List<TareasHistoricas> data) {
        try {
            return new ResponseEntity<>(service.reportTareas(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
