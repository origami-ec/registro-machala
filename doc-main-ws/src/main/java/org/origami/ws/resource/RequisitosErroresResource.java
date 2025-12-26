package org.origami.ws.resource;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origami/api/")
public class RequisitosErroresResource {

    /*@Autowired
    private NotificacionService service;

    @RequestMapping(value = "requisitosErrores/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody List<RequisitosErroresDTO> data) {
        try {
            return new ResponseEntity<>(service.guardarRequisitos(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "requisitoError/updateStatus", method = RequestMethod.POST)
    public ResponseEntity<?> updateStatus(@RequestBody RequisitosErrores data) {
        try {
            return new ResponseEntity<>(service.updateRequisito(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "requisitosErrores/notificacion/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<RequisitosErrores>> getRequisitosObservacion(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.requisitosErroresByNotificacion(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }*/
}
