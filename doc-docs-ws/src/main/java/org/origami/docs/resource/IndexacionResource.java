package org.origami.docs.resource;

import org.origami.docs.entity.Indexacion;
import org.origami.docs.service.IndexacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/origami/docs/")
public class IndexacionResource {

    @Autowired
    private IndexacionService service;

    @RequestMapping(value = "indexaciones", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Indexacion data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));

        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/validar", method = RequestMethod.POST)
    public ResponseEntity<?> consultar(@RequestBody Indexacion indexacion) {
        Indexacion response = service.validarIndexacionFormulario(indexacion);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/consultar", method = RequestMethod.GET)
    public ResponseEntity<?> consultar() {
        return new ResponseEntity<>(service.consultar(), HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/descripcion/{descripcion}", method = RequestMethod.GET)
    public ResponseEntity<?> getByDescripcion(@PathVariable String descripcion) {
        return new ResponseEntity<>(service.consultar(descripcion), HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody Indexacion indexacion) {
        Indexacion response = service.guardar(indexacion);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
