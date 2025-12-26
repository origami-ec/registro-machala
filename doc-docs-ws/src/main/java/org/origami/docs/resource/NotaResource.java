package org.origami.docs.resource;

import org.origami.docs.model.NotaDto;
import org.origami.docs.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/origami/docs/")
public class NotaResource {

    @Autowired
    private NotaService service;

    @RequestMapping(path = "nota/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guaerdarNota(@RequestBody NotaDto notaDto) {
        System.out.println("guaerdarNota");
        NotaDto response = service.guardar(notaDto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


}
