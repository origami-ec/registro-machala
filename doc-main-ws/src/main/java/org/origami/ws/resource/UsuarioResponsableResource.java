package org.origami.ws.resource;

import org.origami.ws.dto.UsuarioResponsableDto;
import org.origami.ws.entities.origami.UsuarioResponsable;
import org.origami.ws.entities.origami.UsuarioResponsableServicio;
import org.origami.ws.service.UsuarioResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/origami/api/")
public class UsuarioResponsableResource {

    @Autowired
    private UsuarioResponsableService service;

    @RequestMapping(value = "departamentoXusuario/{usuario}", method = RequestMethod.GET)
    public ResponseEntity<?> departamentoXusuario(@PathVariable String usuario) {
        try {
            return new ResponseEntity<>(service.usuariosXdepartamentos(usuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuariosXdepartamentos/{departamento}", method = RequestMethod.GET)
    public ResponseEntity<?> usuariosXdepartamentos(@PathVariable Long departamento) {
        try {
            return new ResponseEntity<>(service.consultarUsuariosXdepartamento(departamento), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usersXdepartamentos/{departamento}", method = RequestMethod.GET)
    public ResponseEntity<?> usersXdepartamentos(@PathVariable Long departamento) {
        try {
            return new ResponseEntity<>(service.usersXdepartamentos(departamento), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usersXdepartamentosXrevisores/{departamento}", method = RequestMethod.GET)
    public ResponseEntity<?> usersXdepartamentosXrevisores(@PathVariable Long departamento) {
        try {
            return new ResponseEntity<>(service.usersXdepartamentosXrevisores(departamento), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuariosXdepartamentosXresponsable/{departamento}", method = RequestMethod.GET)
    public ResponseEntity<?> usuariosXdepartamentosXresponsable(@PathVariable Long departamento) {
        try {
            return new ResponseEntity<>(service.consultarUsuariosXdepartamentoXresponsable(departamento), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "obtener/usuarios/responsables/paginado", method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioResponsable>> obtenerUsuariosResponsables(@RequestBody UsuarioResponsable data, Pageable pageable) {
        try {
            Map<String, List> map = service.obtenerUsuariosResponsables(data, pageable);
            List<String> pages = map.get("pages");
            HttpHeaders headers = new HttpHeaders();
            headers.add("totalPages", pages.get(0));
            headers.add("rootSize", pages.get(0));
            return new ResponseEntity<>(map.get("result"), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "buscar/usuario/responsable", method = RequestMethod.POST)
    public ResponseEntity<UsuarioResponsable> buscarUsuarioResponsable(@RequestBody UsuarioResponsable data) {
        try {
            return new ResponseEntity<>(service.buscarUsuarioResponsable(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "obtener/usuarios/responsables", method = RequestMethod.POST)
    public ResponseEntity<List<UsuarioResponsable>> obtenerUsuariosResponsables(@RequestBody UsuarioResponsable data) {
        try {
            return new ResponseEntity<>(service.obtenerResponsables(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "guardar/usuario/responsable", method = RequestMethod.PUT)
    public ResponseEntity<?> guardarUsuarioResponsable(@RequestBody UsuarioResponsableDto data) {
        try {
            return new ResponseEntity<>(service.guardarUsuarioResponsable(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "guardar/usuario/responsableServicio", method = RequestMethod.POST)
    public ResponseEntity<?> guardarUsuarioResponsableServicio(@RequestBody UsuarioResponsableServicio data) {
        try {
            return new ResponseEntity<>(service.guardarUsuarioResponsableServicio(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "eliminar/usuario/responsableServicio", method = RequestMethod.POST)
    public ResponseEntity<?> eliminarUsuarioResponsableServicio(@RequestBody UsuarioResponsableServicio data) {
        try {
            return new ResponseEntity<>(service.eliminarResponsableServicio(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
