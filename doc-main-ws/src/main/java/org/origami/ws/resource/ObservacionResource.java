package org.origami.ws.resource;

import org.origami.ws.entities.origami.HistoricoTramites;
import org.origami.ws.repository.origami.ObservacionRepository;
import org.origami.ws.entities.origami.Observaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/origami/api/")
public class ObservacionResource {

    @Autowired
    private ObservacionRepository observacionRepository;

    @RequestMapping(value = "observaciones/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid Observaciones data) {
        return new ResponseEntity<>(observacionRepository.findAll(Example.of(data)), HttpStatus.OK);
    }


    @RequestMapping(value = "create/observacion", method = RequestMethod.POST)
    public ResponseEntity<?> createObservacion(@RequestBody HistoricoTramites historico) {
        try {
            Observaciones observaciones = new Observaciones();
            observaciones.setEstado(Boolean.TRUE);
            observaciones.setFecCre(new Date());
            observaciones.setTarea(historico.getTarea());
            observaciones.setUserCre(historico.getNameUser());
            observaciones.setObservacion(historico.getObservacion());
            observaciones.setIdTramite(historico);
            observaciones.setIdProceso(Long.parseLong(historico.getIdProceso()));
            return new ResponseEntity<>(observacionRepository.save(observaciones), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "observaciones/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody Observaciones data) {
        try {
            return new ResponseEntity<>(observacionRepository.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "observaciones/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody Observaciones data) {
        try {
            return new ResponseEntity<>(observacionRepository.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
