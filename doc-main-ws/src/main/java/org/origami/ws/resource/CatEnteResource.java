package org.origami.ws.resource;

import org.origami.ws.entities.origami.Persona;
import org.origami.ws.repository.origami.PersonaRepository;
import org.origami.ws.service.CatEnteService;
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
public class CatEnteResource {

    @Autowired
    private CatEnteService service;
    @Autowired
    private PersonaRepository repository;

    @RequestMapping(value = "persona/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid Persona data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "persona/documento/{documento}", method = RequestMethod.GET)
    public ResponseEntity<?> findByCedula(@PathVariable String documento) {
        return new ResponseEntity<>(repository.findTopByIdentificacionOrderByIdDesc(documento), HttpStatus.OK);
    }

    @RequestMapping(value = "persona/usuario/{usuario}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarPersonaXusuario(@PathVariable String usuario) {
        return new ResponseEntity<>(service.buscarPersonaXusuario(usuario), HttpStatus.OK);
    }

    @RequestMapping(value = "personas/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid Persona data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "personas/taskFinisihTime", method = RequestMethod.GET)
    public ResponseEntity<?> findUserPendienteTaskFinsih(@Valid Persona data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "personas/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody Persona data) {
        try {
            return new ResponseEntity<>(service.registrar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "personas/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody Persona data) {
        try {
            return new ResponseEntity<>(service.actualizar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


}
