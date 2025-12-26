package org.origami.docs.resource;

import org.origami.docs.entity.FirmaElectronica;
import org.origami.docs.service.FirmaElectronicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController
@RequestMapping("/origami/docs/")
public class FirmaElectronicaResource {

    @Autowired
    private FirmaElectronicaService service;



    @RequestMapping(value = "firmaElectronica/find", method = RequestMethod.GET)
    public ResponseEntity<?> find(@Valid FirmaElectronica data) {
        return new ResponseEntity<>(service.buscarFirma(data), HttpStatus.OK);
    }


    @RequestMapping(value = "firmaElectronicas/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            return new ResponseEntity<>(service.guardarFirma(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "firmaElectronicas/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            return new ResponseEntity<>(service.guardarFirma(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

}
