package org.origami.ws.resource;

import org.origami.ws.entities.origami.ServiciosDepartamento;
import org.origami.ws.service.ServiciosDepartamentoService;
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
@RequestMapping("/origami/api/")
public class ServiciosDepartamentoResource {

    @Autowired
    private ServiciosDepartamentoService service;

    @RequestMapping(value = "serviciosDepartamentoItem/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid ServiciosDepartamento data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "serviciosDepartamentoItems/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid ServiciosDepartamento data, Pageable pageable) {
        Map<String, List> map = service.find( data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "serviciosDepartamentoItems/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody ServiciosDepartamento data) {
        try {
            return new ResponseEntity<>(service.registrarActualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "serviciosDepartamentoItems/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody ServiciosDepartamento data) {
        try {
            return new ResponseEntity<>(service.registrarActualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "serviciosDepartamentoItems/eliminar", method = RequestMethod.PUT)
    public ResponseEntity<?> eliminar(@RequestBody ServiciosDepartamento data) {
        try {
            service.eliminar(data);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
