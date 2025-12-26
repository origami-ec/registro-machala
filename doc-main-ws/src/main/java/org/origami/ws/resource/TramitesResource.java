package org.origami.ws.resource;

import org.origami.ws.entities.origami.Tramites;
import org.origami.ws.service.TramitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping( "/origami/api/")
public class TramitesResource {

    @Autowired
    private TramitesService service;

    @RequestMapping(value = "tramites/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> search(@Valid Tramites data, Pageable pageable) {
        Map<String, List> map = service.findAll(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "analisis/tramites/{fechaDesde}/{fechaHasta}", method = RequestMethod.GET)
    public ResponseEntity<?> analisisTramite(@PathVariable String fechaDesde, @PathVariable String fechaHasta) {
        return new ResponseEntity<>(service.findAll(fechaDesde, fechaHasta), HttpStatus.OK);
    }

    @RequestMapping(value = "analisis/tramites/{fechaDesde}/{fechaHasta}/{usuario}/{tramite}/{tipo}", method = RequestMethod.GET)
    public ResponseEntity<?> analisisTramiteByUsuarioAndTramite(
            @PathVariable String usuario, @PathVariable Long tramite,
            @PathVariable String fechaDesde,
            @PathVariable String fechaHasta, @PathVariable String tipo) {
        return new ResponseEntity<>(service.findAllByUsuarioAndTramite(usuario, tramite, fechaDesde, fechaHasta, tipo), HttpStatus.OK);
    }
}
