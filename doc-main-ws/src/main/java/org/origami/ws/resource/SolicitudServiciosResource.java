package org.origami.ws.resource;

import org.origami.ws.dto.SolicitudServiciosDTO;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.origami.ws.service.SolicitudServiciosService;
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
@RequestMapping("/origami/api/")
public class SolicitudServiciosResource {

    @Autowired
    private SolicitudServiciosService service;


    @RequestMapping(value = "solicitudServicio/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid SolicitudServicios data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudServicios/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid SolicitudServicios data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudServicios/mapper", method = RequestMethod.GET)
    public ResponseEntity<?> mapper(@Valid SolicitudServiciosDTO data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "solicitudServicios/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody SolicitudServicios data) {
        try {
            return new ResponseEntity<>(service.registrar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "solicitudServicios/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody SolicitudServicios data) {
        try {
            return new ResponseEntity<>(service.actualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "solicitudServicios/eliminar", method = RequestMethod.PUT)
    public ResponseEntity<?> eliminar(@RequestBody SolicitudServicios data) {
        try {
            service.eliminar(data);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "solicitudServicios/notificarSolicitudPago", method = RequestMethod.PUT)
    public ResponseEntity<?> notificarSolicitudPago(@RequestBody SolicitudServiciosDTO data) {
        return new ResponseEntity<>(service.notificarSolicitudPago(data), HttpStatus.OK);
    }


}
