package org.origami.ws.resource;

import org.origami.ws.entities.origami.SolicitudDocumentos;
import org.origami.ws.service.SolicitudDocumentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/origami/api/")
public class SolicitudDocumentosResource {

    @Autowired
    private SolicitudDocumentosService service;


    @RequestMapping(value = "solicitudDocumentos/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid SolicitudDocumentos data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDocumentos/findOne", method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@Valid SolicitudDocumentos data) {
        return new ResponseEntity<>(service.findOne(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDocumentos/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody List<SolicitudDocumentos> data) {
        return new ResponseEntity<>(service.guardar(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDocumento/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody SolicitudDocumentos data) {
        return new ResponseEntity<>(service.guardar(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDocumento/eliminar", method = RequestMethod.PUT)
    public ResponseEntity<?> eliminar(@RequestBody SolicitudDocumentos data) {
        service.eliminar(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDocumentos/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody List<SolicitudDocumentos> data) {
        return new ResponseEntity<>(service.actualizar(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDocumento/Ventanilla/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardarDocumentosSolicitudAndVentanilla(@RequestBody SolicitudDocumentos data) {
        try {
            return new ResponseEntity<>(service.updateDocRequisitos(data), HttpStatus.OK);
        } catch (Exception e) {
            e.getMessage();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
