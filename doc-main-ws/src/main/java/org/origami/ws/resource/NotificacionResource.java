package org.origami.ws.resource;

import org.origami.ws.entities.origami.Notificacion;
import org.origami.ws.repository.origami.NotificacionRepository;
import org.origami.ws.repository.origami.TipoNotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origami/api/")
public class NotificacionResource {

    @Autowired
    private NotificacionRepository nr;
    @Autowired
    private TipoNotificacionRepository tnr;

    @RequestMapping(value = "notificacion/tipoNotificaciones", method = RequestMethod.GET)
    public ResponseEntity<?> getTipoNotificaciones() {
        try {
            return new ResponseEntity<>(tnr.getAllByEstadoIsTrueAndClaseOrderByNombre(1), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "notificacion/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getNotificacionById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(nr.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @RequestMapping(value = "notificacion/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> persistir(@RequestBody Notificacion data) {
        try {
            return new ResponseEntity<>(nr.save(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "notificacion/update", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody Notificacion data) {
        try {
            return new ResponseEntity<>(nr.save(data), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
