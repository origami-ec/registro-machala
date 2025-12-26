package org.origami.ws.resource;

import org.origami.ws.entities.security.Usuario;
import org.origami.ws.mappers.UsuarioMapper;
import org.origami.ws.repository.security.UsuarioRepository;
import org.origami.ws.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/origami/api/")
public class UserResource {

    private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Autowired
    private UsuarioService service;
    @Autowired
    private UsuarioRepository ur;
    @Autowired
    private UsuarioMapper um;

    @RequestMapping(value = "iniciarSesion", method = RequestMethod.POST)
    public ResponseEntity<?> iniciarSesion(@RequestBody Usuario user) {
        try {
            Usuario u = service.iniciarSesion(user);
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "user/find/username/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable String username) {
        try {
            return new ResponseEntity<>(service.findUsername(username), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuarios", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> findAllUser(@Valid Usuario data, Pageable pageable) {
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

    @RequestMapping(value = "usuario/find", method = RequestMethod.GET)
    public ResponseEntity<?> findUser(@Valid Usuario data) {
        try {
            return new ResponseEntity<>(service.findOne(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "usuario/findAll", method = RequestMethod.POST)
    public ResponseEntity<?> findAllUser(@Valid Usuario data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }



    @RequestMapping(value = "create/validate", method = RequestMethod.POST)
    public ResponseEntity<?> validarUser(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(service.validate(usuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "usuarios/tareasPendienteFinsih", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> findUserTaskFinish(@Valid Usuario data, Pageable pageable) {
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

    @RequestMapping(value = "usuarios/find/all", method = RequestMethod.GET)
    public ResponseEntity<?> findAllUsers() {
        try {
            return new ResponseEntity<>(um.toListDTO(ur.findAllUsuarios()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }




}
