package org.origami.ws.resource;

import org.origami.ws.entities.origami.Documentos;
import org.origami.ws.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/origami/api/")
public class DocumentosResource {

    @Autowired
    private DocumentsService documentoService;

    @RequestMapping(value = "documents/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guardarDocumento(@RequestBody Documentos data) {
        try {
            return new ResponseEntity<>(documentoService.guardarDocumento(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "documents/consultar/{usuario}", method = RequestMethod.GET)
    public ResponseEntity<List<Documentos>> consultar(@PathVariable String usuario) {
        return new ResponseEntity<>(documentoService.consultarDocumentos(usuario), HttpStatus.OK);
    }

    @RequestMapping(value = "documents/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> pdfDocuments(@PathVariable Long id) {

        byte[] pdfContents = null;
        try {
            pdfContents = documentoService.consultarPDF(id);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline" + "; filename=" + "document.pdf");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
