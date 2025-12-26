package org.origami.ws.resource;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;

import org.origami.ws.entities.origami.Menu;
import org.origami.ws.entities.origami.MenuRol;
import org.origami.ws.entities.origami.MenuTipoAcceso;
import org.origami.ws.entities.origami.Menubar;
import org.origami.ws.service.PubGuiMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/origami/api/")
public class MenuResource {

    private static final Logger logger = Logger.getLogger(MenuResource.class.getName());

    @Autowired
    private PubGuiMenuService menuService;

    @RequestMapping(value = "menu/find", method = RequestMethod.GET)
    public ResponseEntity<Menu> findAllMenu(@Valid Menu data) {
        return new ResponseEntity<>(menuService.findMenu(data), HttpStatus.OK);
    }

    @RequestMapping(value = "menu/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<Menu>> findMenuAll(@Valid Menu data) {
        return new ResponseEntity<>(menuService.findMenuAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "menus/find", method = RequestMethod.GET)
    public ResponseEntity<List<Menu>> findAllMenu(@Valid Menu data, Pageable pageable) {
        return new ResponseEntity<>(menuService.findAllMenu(data, pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "menuBar/find", method = RequestMethod.GET)
    public ResponseEntity<Menubar> findMenuBar(@Valid Menubar data) {
        return new ResponseEntity<>(menuService.findMenuBar(data), HttpStatus.OK);
    }

    @RequestMapping(value = "menusBar/find", method = RequestMethod.GET)
    public ResponseEntity<List<Menubar>> findAllMenuBar(@Valid Menubar data) {
        try {
            return new ResponseEntity<>(menuService.findAllMenuBar(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "menusBar/find/{user}", method = RequestMethod.GET)
    public ResponseEntity<List<Menubar>> findAllMenuBar(@PathVariable String user) {
        try {
            return new ResponseEntity<>(menuService.findAllMenuBar(user), HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception findAllMenuBar {0}" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "menusTipoAcceso/find", method = RequestMethod.GET)
    public ResponseEntity<List<MenuTipoAcceso>> findAllMenuTipoAcceso(@Valid MenuTipoAcceso data) {
        return new ResponseEntity<>(menuService.findAllMenuTipoAcceso(data), HttpStatus.OK);
    }

    @RequestMapping(value = "menusRol/find", method = RequestMethod.GET)
    public ResponseEntity<List<MenuRol>> findAllMenuRol(@Valid MenuRol data) {
        return new ResponseEntity<>(menuService.findAllMenuRol(data), HttpStatus.OK);
    }

    @RequestMapping(value = "menus/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> registrar(@RequestBody Menu data) {
        try {
            return new ResponseEntity<>(menuService.registrarActualizarMenu(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "menus/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizarMenu(@RequestBody Menu data) {
        try {
            return new ResponseEntity<>(menuService.registrarActualizarMenu(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "menus/eliminar", method = RequestMethod.PUT)
    public ResponseEntity<?> eliminarMenu(@RequestBody Menu data) {
        try {
            menuService.eliminarMenu(data);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "menusRol/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardararRol(@RequestBody MenuRol data) {
        try {
            return new ResponseEntity<>(menuService.guardarRol(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "menusRol/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizarRol(@RequestBody MenuRol data) {
        try {
            return new ResponseEntity<>(menuService.guardarRol(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "menusRol/delete", method = RequestMethod.PUT)
    public ResponseEntity<?> deleteMenuRol(@RequestBody MenuRol data) {
        try {
            menuService.deleteMenuRol(data);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

}
