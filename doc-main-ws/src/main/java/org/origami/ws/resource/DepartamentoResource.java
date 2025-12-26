package org.origami.ws.resource;

import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.service.DepartamentoService;
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
public class DepartamentoResource {

    @Autowired
    private DepartamentoService service;


    @RequestMapping(value = "departamentos/serviciosDepartamento/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<Departamento>> findAllDeptByServiciosDepartamento() {
        return new ResponseEntity<>(service.findAllDepartamentoByServiciosDepartamento(), HttpStatus.OK);
    }

    @RequestMapping(value = "departamentos/sincronizar", method = RequestMethod.GET)
    public ResponseEntity<?> sincronizarDeps() {
        return new ResponseEntity<>(service.sincronizarDeps(), HttpStatus.OK);
    }

    @RequestMapping(value = "departamentos/find", method = RequestMethod.GET)
    public ResponseEntity<List<Departamento>> find(@Valid Departamento data) {
        return new ResponseEntity<>(service.findAllDepartamento(data), HttpStatus.OK);
    }

    @RequestMapping(value = "departamentosHijos", method = RequestMethod.GET)
    public ResponseEntity<List<Departamento>> find() {
        return new ResponseEntity<>(service.findAllDepartamento(), HttpStatus.OK);
    }

    @RequestMapping(value = "direccionConUsuario", method = RequestMethod.GET)
    public ResponseEntity<List<Departamento>> findDepartamentosConUsuario() {
        return new ResponseEntity<>(service.findAllDepartamentoWithUser(), HttpStatus.OK);
    }

    @RequestMapping(value = "find/direccion/{idDepartamento}", method = RequestMethod.POST)
    public ResponseEntity<?> findDireccion(@PathVariable Long idDepartamento) {
        return new ResponseEntity<>(service.findAllDireccion(idDepartamento), HttpStatus.OK);
    }

    @RequestMapping(value = "departamento/find", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Departamento data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "departamentos", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Departamento data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "departamento/padres", method = RequestMethod.GET)
    public ResponseEntity<?> getArchivadoresPadres() {
        try {
            return new ResponseEntity<>(service.getPadresDepartamentos(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "departamento/hijos/{padre}", method = RequestMethod.GET)
    public ResponseEntity<?> getArchivadoresHijos(@PathVariable Long padre) {
        try {
            return new ResponseEntity<>(service.getHijosDepartamentos(padre), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "departamento/hijosActivos/{padre}", method = RequestMethod.GET)
    public ResponseEntity<?> getArchivadoresHijosByEstado(@PathVariable Long padre) {
        try {
            return new ResponseEntity<>(service.getHijosDepartamentosByEstado(padre), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
