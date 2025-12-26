package org.bcbg.resources;

import org.bcbg.model.*;
import org.bcbg.rubrica.sign.FirmaElectronicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bcbg/api/")
public class FirmaElectronicaResource {

    @Autowired
    private FirmaElectronicaService service;

    @RequestMapping(value = "firmaElectronica/consultarTokens", method = RequestMethod.GET)
    public ResponseEntity<?> consultarTokens() {
        try {
            return new ResponseEntity<>(service.getAllTokens(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/firmarDesk", method = RequestMethod.POST)
    public ResponseEntity<?> firmarToken(@RequestBody FirmaDocDesk firmaDocDesk) {
        try {
            FirmaElectronica fe = service.firmarDocumentoDesk(firmaDocDesk);
            fe.setArchivoDesk(null);
            return new ResponseEntity<>(fe, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/generar", method = RequestMethod.POST)
    public ResponseEntity<?> generar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            if (firmaElectronica.getEsToken() == null) {
                firmaElectronica.setEsToken(Boolean.TRUE);
            }
            FirmaElectronica fe = !firmaElectronica.getEsToken() ?
                    service.firmarDocumentoArchivo(firmaElectronica, Boolean.FALSE) :
                    service.firmarDocumentoToken(firmaElectronica);
            return new ResponseEntity<>(fe, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/validar", method = RequestMethod.POST)
    public ResponseEntity<?> validar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            System.out.println("validarCertificado");
            return new ResponseEntity<>(service.validarCertificado(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            firmaElectronica.setUid(null);
            firmaElectronica.setEstadoFirma(e.getMessage());
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/verificarDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> verificarDocumento(@RequestBody FirmaElectronica firmaElectronica) {
        try {

            return new ResponseEntity<>(service.verificarDocumento(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/generarDocumentoAnterior", method = RequestMethod.POST)
    public ResponseEntity<?> generar(@RequestBody FirmaElectronicaDocumento firmaElectronica) {
        try {
            if (firmaElectronica.getFirmaElectronica().getEsToken() == null) {
                firmaElectronica.getFirmaElectronica().setEsToken(Boolean.TRUE);
            }
            firmaElectronica.getFirmaElectronica().setFechaEmision(firmaElectronica.getFechaFirmar());
            FirmaElectronica fe = service.firmarDocumentoArchivo(firmaElectronica.getFirmaElectronica(), Boolean.FALSE);
            //service.cambiarHora(firmaElectronica.getFechaFirmar());
            return new ResponseEntity<>(fe, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    /*
        Metodo que genera en maquina local el documento
    */
    @RequestMapping(value = "firmaElectronica/generarFirmaDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> generarDocumento(@RequestBody FirmaDocumento FirmaDocumento) {
        try {
            FirmaDocumento firmaElectronica = service.generarDocumentoLocal(FirmaDocumento);
            //System.out.println("firmaElectronica: " + firmaElectronica.toString());
            firmaElectronica.setArchivoFirmado(null);
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }


    /*
    Metodo que genera en maquina local el documento
     */
    @RequestMapping(value = "firmaElectronica/subirDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> subirDocumento(@RequestBody FirmaDocumento FirmaDocumento) {
        try {
            FirmaDocumento firmaElectronica = service.subirDocumento(FirmaDocumento);
            //System.out.println("firmaElectronica: " + firmaElectronica.toString());
            firmaElectronica.setArchivoFirmado(null);
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "firmaElectronica/generarScan", method = RequestMethod.POST)
    public ResponseEntity<?> generarScan(@RequestBody DocumentosScan documentosScan) {
        try {
            DocumentosScan imgs = service.guardarDocumentoScan(documentosScan);
            return new ResponseEntity<>(imgs, imgs!=null ?HttpStatus.OK:HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
