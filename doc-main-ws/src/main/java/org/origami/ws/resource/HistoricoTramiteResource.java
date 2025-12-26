package org.origami.ws.resource;

import org.origami.ws.entities.origami.HistoricoTramites;
import org.origami.ws.service.HistoricoTramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/origami/api/")
public class HistoricoTramiteResource {

    @Autowired
    private HistoricoTramiteService service;

    @RequestMapping(value = "historicoTramite/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> saveHistoricoTramite(@RequestBody HistoricoTramites historicoTramites) {
        return new ResponseEntity<>(service.registrar(historicoTramites, null, Boolean.FALSE), HttpStatus.OK);
    }

    @RequestMapping(value = "historicoTramite/actualizar", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarHistoricoTramite(@RequestBody HistoricoTramites historicoTramites) {
        return new ResponseEntity<>(service.actualizar(historicoTramites), HttpStatus.OK);
    }

    @RequestMapping(value = "historicoTramite/actualizarProcessInstance", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarProcessInstance(@RequestBody HistoricoTramites historicoTramites) {
        return new ResponseEntity<>(service.actualizarProcessInstance(historicoTramites), HttpStatus.OK);
    }


    @RequestMapping(value = "historicoTramite/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid HistoricoTramites data) {
        try {
            return new ResponseEntity<>(service.find(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "historicoTramite/codigo/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable String codigo) {
        try {
            return new ResponseEntity<>(service.findProcessInstace(codigo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "historicoTramite/findProcessId/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findProcessId(@PathVariable String id) {
        try {
            return new ResponseEntity<>(service.findProcessId(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "historicoTramite/findProcessTemp/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findProcessTemp(@PathVariable String id) {
        try {
            return new ResponseEntity<>(service.findProcessTemp(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
