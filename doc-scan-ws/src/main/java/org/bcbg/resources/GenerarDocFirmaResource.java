package org.bcbg.resources;

import org.bcbg.model.FirmaDocDesk;
import org.bcbg.model.Imagenes;
import org.bcbg.rubrica.sign.GenerarDocFirmaService;
import org.bcbg.service.GestorDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bcbg/api/")
public class GenerarDocFirmaResource {
    @Autowired
    private GenerarDocFirmaService service;

    @Autowired
    private GestorDocService gestorDocService;

    @RequestMapping(value = "firmaElectronica/consultarImpresoras", method = RequestMethod.GET)
    public ResponseEntity<?> consultarTokens() {
        try {
            return new ResponseEntity<>(gestorDocService.impresorasConectadas(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @RequestMapping(value = "firmaElectronica/generarDoc/generar", method = RequestMethod.POST)
    public ResponseEntity<?> generarImagensPDF(@RequestBody FirmaDocDesk firmaDocDesk) {
        try {
            List<Imagenes> imgs = service.pdfToImagen(firmaDocDesk.getArchivo(), firmaDocDesk.getPagina());
            return new ResponseEntity<>(imgs, imgs!=null ?HttpStatus.OK:HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "firmaElectronica/docImagen/{imagen}", method = RequestMethod.GET)
    public @ResponseBody byte[] docImagen(@PathVariable String imagen) {
        return service.docImagen(imagen);
    }






}
