package org.origami.ws.resource;

import org.origami.ws.entities.origami.RolUsuario;
import org.origami.ws.service.RolUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/origami/api/")
public class RolUsuarioResource {
    private static final Logger logger = Logger.getLogger(RolUsuarioResource.class.getName());

    @Autowired
    private RolUsuarioService service;

    @RequestMapping(value = "usuariosRol/finds", method = RequestMethod.GET)
    public ResponseEntity<?> findAllUsersByRol(@Valid RolUsuario data) {
        return new ResponseEntity<>(service.findAllUsuariosByRol(data), HttpStatus.OK);
    }

    @RequestMapping(value = "rolusuario/finds", method = RequestMethod.GET)
    public ResponseEntity<?> findAllRolUsuario(@Valid RolUsuario data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.INFO, "Exeception findAllRolUsuario {0}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "rolusuario/find", method = RequestMethod.GET)
    public ResponseEntity<?> findRolUsuario(@Valid RolUsuario data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @RequestMapping(value = "roles/usuario/save", method = RequestMethod.POST)
    public ResponseEntity<List<RolUsuario>> saveRolesUsuarios(@RequestBody List<RolUsuario> rolUsuario) {
        try {
            return new ResponseEntity<>(service.saveAll(rolUsuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "roles/usuario/delete", method = RequestMethod.PUT)
    public ResponseEntity<?> deleteRolUsuario(@RequestBody List<RolUsuario> rolUsuarios) {
        try {
            service.deleteAll(rolUsuarios);
            return new ResponseEntity<>(rolUsuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "roles/usuario/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody RolUsuario data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @RequestMapping(value = "roles/usuarios/find", method = RequestMethod.GET)
    public ResponseEntity<?> findUserTaskFinish(@Valid RolUsuario data, Pageable pageable) {
        try {
            Map<String, List> map = service.find(data, pageable);
            List<String> pages = map.get("pages");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("totalPages", pages.get(0));
            responseHeaders.add("rootSize", pages.get(1));
            return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

}
