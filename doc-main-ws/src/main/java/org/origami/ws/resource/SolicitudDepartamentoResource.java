package org.origami.ws.resource;

import org.origami.ws.entities.origami.SolicitudDepartamento;
import org.origami.ws.service.DepartamentoService;
import org.origami.ws.service.SolicitudDepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/origami/api/")
public class SolicitudDepartamentoResource {

    @Autowired
    private SolicitudDepartamentoService service;
    @Autowired
    private DepartamentoService departamentoService;


    @RequestMapping(value = "solicitudDepartamentos/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid SolicitudDepartamento data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudDepartamentos/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody SolicitudDepartamento data) {
        try {
            return new ResponseEntity<>(service.registrar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "solicitudDepartamentos/actualizarList", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody List<SolicitudDepartamento> data) {
        try {
            return new ResponseEntity<>(service.actualizarList(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "solicitudDepartamentos/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody SolicitudDepartamento data) {
        try {
            return new ResponseEntity<>(service.actualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "departamentoNombre/find/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<?> getDeparatamentoNombre(@PathVariable String codigo) {
        try {
            return new ResponseEntity<>(departamentoService.getDepartamentoNombre(codigo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }
}
