package org.origami.docs.resource;

import org.origami.docs.entity.Tesauro;
import org.origami.docs.service.TesauroService;
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
@RequestMapping("/origami/docs/")
public class TesauroResource {

    @Autowired
    private TesauroService service;

    @RequestMapping(value = "tesauro", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Tesauro data, Pageable pageable) {
        Map<String, List> map = service.find(data,pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));

        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "tesauro/validar", method = RequestMethod.POST)
    public ResponseEntity<?> consultar(@RequestBody Tesauro tesauro) {
        Tesauro response = service.validarTesauroFormulario(tesauro);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "tesauro/consultar", method = RequestMethod.GET)
    public ResponseEntity<?> consultar() {
        return new ResponseEntity<>(service.consultar(), HttpStatus.OK);
    }

    @RequestMapping(path = "tesauro/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody Tesauro indexacion) {
        Tesauro response = service.guardar(indexacion);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


}
