package org.origami.ws.resource;

import org.origami.ws.entities.origami.Valores;
import org.origami.ws.repository.origami.SecuenciaTramitesRepository;
import org.origami.ws.repository.origami.ValoresRepository;
import org.origami.ws.entities.origami.SecuenciaTramites;
import org.origami.ws.service.SecuenciaGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origami/api/")
public class ValoresResource {

    @Autowired
    private ValoresRepository valoresRepository;
    @Autowired
    private SecuenciaGeneralService secuenciaGeneralService;
    @Autowired
    private SecuenciaTramitesRepository secuenciaTramitesRepository;


    @RequestMapping(value = "valores/find", method = RequestMethod.GET)
    public ResponseEntity<?> find() {
        return new ResponseEntity<>(valoresRepository.findAllByOrderByIdDesc(), HttpStatus.OK);
    }

    @RequestMapping(value = "valores/find/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable String codigo) {
        return new ResponseEntity<>(valoresRepository.findAllByCodeContains(codigo), HttpStatus.OK);
    }

    @RequestMapping(value = "valores/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Valores data) {
        return new ResponseEntity<>(valoresRepository.save(data), HttpStatus.OK);
    }

    @RequestMapping(value = "valores/eliminar", method = RequestMethod.POST)
    public ResponseEntity<?> eliminar(@RequestBody Valores data) {
        valoresRepository.delete(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = "valor/code/{code}", method = RequestMethod.GET)
    public ResponseEntity<?> findValorCode(@PathVariable String code) {
        return new ResponseEntity<>(valoresRepository.findByCode(code), HttpStatus.OK);
    }

    @RequestMapping(value = "secuencial/tramite/code/{code}", method = RequestMethod.POST)
    public ResponseEntity<?> secuencialTramite(@PathVariable String code) {
        return new ResponseEntity<>(secuenciaGeneralService.getSecuenciaGeneralByAnio(code), HttpStatus.OK);
    }

    @RequestMapping(value = "secuenciaTramites/save", method = RequestMethod.POST)
    public ResponseEntity<?> secuenciaTramite(@RequestBody SecuenciaTramites secuencia) {
        return new ResponseEntity<>(secuenciaTramitesRepository.save(secuencia), HttpStatus.OK);
    }
}
