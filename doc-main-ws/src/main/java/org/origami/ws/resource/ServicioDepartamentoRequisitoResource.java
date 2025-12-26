package org.origami.ws.resource;

import org.origami.ws.entities.origami.ServiciosDepartamentoRequisitos;
import org.origami.ws.models.Data;
import org.origami.ws.service.ServiciosDepartamentoItemsRequisitoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/origami/api/")
public class ServicioDepartamentoRequisitoResource {

    @Autowired
    private ServiciosDepartamentoItemsRequisitoServices service;

    @RequestMapping(value = "serviciosDepartamentoItemRequisito/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid ServiciosDepartamentoRequisitos data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "requisitos/tramite/{tramiteId}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Long tramiteId) {
        return new ResponseEntity<>(service.findDTO(tramiteId), HttpStatus.OK);
    }

    @RequestMapping(value = "serviciosDepartamentoItemsRequisitos/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid ServiciosDepartamentoRequisitos data) {
        return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
    }


    @RequestMapping(value = "serviciosDepartamentoItemsRequisitos/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody ServiciosDepartamentoRequisitos data) {
        try {
            return new ResponseEntity<>(service.registrarActualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "serviciosDepartamentoItemsRequisitos/findAbrev", method = RequestMethod.POST)
    public ResponseEntity<?> getRequisitosByServicioDepartamentoAbrev(@RequestBody Data data) {
        try {
            return new ResponseEntity<>(service.findRequisitosByServicioDepartamentoAbreviatura(data.getData()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "serviciosDepartamentoItemsRequisitos/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody ServiciosDepartamentoRequisitos data) {
        try {
            return new ResponseEntity<>(service.registrarActualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "serviciosDepartamentoItemsRequisitos/eliminar", method = RequestMethod.PUT)
    public ResponseEntity<?> eliminar(@RequestBody ServiciosDepartamentoRequisitos data) {
        try {
            service.eliminar(data);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
