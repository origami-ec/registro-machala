package org.origami.ws.resource;

import org.origami.ws.entities.origami.TipoTramite;
import org.origami.ws.repository.origami.TipoTramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/origami/api/")
public class TipoTramiteResource {

    @Autowired
    private TipoTramiteRepository tipoTramiteRepository;


    @RequestMapping(value = "tiposTramite", method = RequestMethod.GET)
    public ResponseEntity<List<TipoTramite>> findAllTipoTramite(@Valid TipoTramite tipoTramite) {
        return new ResponseEntity<>(tipoTramiteRepository.findAll(Example.of(tipoTramite)), HttpStatus.OK);
    }

    @RequestMapping(value = "tiposTramiteBPM", method = RequestMethod.GET)
    public ResponseEntity<List<TipoTramite>> findAllTipoTramiteBPM() {
        return new ResponseEntity<>(tipoTramiteRepository.tramitesBpmn(), HttpStatus.OK);
    }

    @RequestMapping(value = "create/tipoTramite", method = RequestMethod.POST)
    public ResponseEntity<?> createTipoTramite(@RequestBody TipoTramite tipoTramite) {
        try {
            return new ResponseEntity<>(tipoTramiteRepository.save(tipoTramite), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tipo/tramite/id/{idTipoTramite}", method = RequestMethod.GET)
    public ResponseEntity<?> findByTipoTramiteId(@PathVariable Long idTipoTramite) {
        try {
            return new ResponseEntity<>(tipoTramiteRepository.findById(idTipoTramite), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tiposTramite/abr/{nombre}", method = RequestMethod.GET)
    public ResponseEntity<?> findTipoTramiteAbreviatura(@PathVariable String nombre) {
        return new ResponseEntity<>(tipoTramiteRepository.findAllByAbreviatura(nombre), HttpStatus.OK);
    }

    @RequestMapping(value = "tipo/tramite/validacion/{id}/{abreviatura}", method = RequestMethod.GET)
    public ResponseEntity<?> validacionTramite(@PathVariable Long id, @PathVariable String abreviatura) {
        try {
            return new ResponseEntity<>(tipoTramiteRepository.buquedaNotAbreviatrua(abreviatura, id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tipo/tramite/find", method = RequestMethod.POST)
    public ResponseEntity<?> validacionTramite(@Valid TipoTramite data) {
        try {
            return new ResponseEntity<>(tipoTramiteRepository.findOne(Example.of(data)).orElse(null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
