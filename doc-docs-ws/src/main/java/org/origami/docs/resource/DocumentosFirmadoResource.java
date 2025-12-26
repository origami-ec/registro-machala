package org.origami.docs.resource;

import org.origami.docs.entity.DocumentoFirmado;
import org.origami.docs.model.DocumentoFirmadoDTO;
import org.origami.docs.service.DocumentoService;
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
public class DocumentosFirmadoResource {

    @Autowired
    private DocumentoService service;

    @RequestMapping(path = "documentosPendientes/{usuario}", method = RequestMethod.GET)
    public ResponseEntity<?> documentosPendienteFirma(@PathVariable String usuario) {
        return new ResponseEntity<>(service.findAllByUser(usuario), HttpStatus.OK);
    }

    @RequestMapping(value = "documentoFirmado/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody DocumentoFirmado documentoFirmado) {
        try {
            return new ResponseEntity<>(service.guardar(documentoFirmado), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "documentoFirmado/guardarDesk", method = RequestMethod.POST)
    public ResponseEntity<?> guardarDesk(@RequestBody DocumentoFirmado documentoFirmado) {
        try {
            return new ResponseEntity<>(service.guardar(documentoFirmado), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "documentoFirmados", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid DocumentoFirmadoDTO data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

}
