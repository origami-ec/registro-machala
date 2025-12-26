package org.origami.docs.resource;

import org.origami.docs.entity.Formato;
import org.origami.docs.service.FormatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/origami/docs/")
public class FormatoResource {

    @Autowired
    private FormatoService service;

    @RequestMapping(value = "formatos", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(Pageable pageable) {
        Map<String, List> map = service.find(pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "formato/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guardarFormato(@RequestBody Formato formato) {
        Formato response = service.guardar(formato);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


}
