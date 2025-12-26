package ec.gob.email.controller;

import ec.gob.email.config.ApplicationProperties;
import ec.gob.email.entity.Correo;
import ec.gob.email.model.CorreoDTO;
import ec.gob.email.model.CorreoArchivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ec.gob.email.services.CorreoService;

import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asgard/api/")
public class AppController {

    @Autowired
    private CorreoService correoService;
    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "enviarCorreov1", method = RequestMethod.POST)
    public ResponseEntity<?> enviarCorreoV1(@RequestBody CorreoDTO correoDTO) {
        try {
            Boolean send = Boolean.FALSE;
            if (correoDTO.getArchivos() != null) {
                correoDTO.setAdjuntos(convertToFiles(correoDTO.getArchivos()));
            } else {
                correoDTO.setAdjuntos(null);
            }
            if (!correoDTO.getDestinatario().isEmpty()) {
                if (correoDTO.getDestinatario().contains(";")) {
                    String[] destinatario = correoDTO.getDestinatario().split(";");
                    if (destinatario.length > 1) {
                        for (String s : destinatario) {
                            correoDTO.setDestinatario("");
                            correoDTO.setDestinatario(s);
                            correoService.enviarCorreo(correoDTO);
                        }
                    }
                } else {
                    correoService.enviarCorreo(correoDTO);
                }
                return new ResponseEntity<>(send ? "OK" : "NO", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("DESTINATARIO VACIO", HttpStatus.BAD_REQUEST);
            }
        } catch (MailException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
    
    @RequestMapping(value = "enviarCorreo", method = RequestMethod.POST)
    public ResponseEntity<?> enviarCorreo(@RequestBody CorreoDTO correoDTO) {
        try {
            Boolean send = Boolean.FALSE;
            if (correoDTO.getArchivos() != null) {
                correoDTO.setAdjuntos(convertToFiles(correoDTO.getArchivos()));
            } else {
                correoDTO.setAdjuntos(null);
            }
            if (!correoDTO.getDestinatario().isEmpty()) {
                if (correoDTO.getDestinatario().contains(";")) {
                    String[] destinatario = correoDTO.getDestinatario().split(";");
                    if (destinatario.length > 1) {
                        for (String s : destinatario) {
                            correoDTO.setDestinatario("");
                            correoDTO.setDestinatario(s);
                            correoService.enviarCorreoV2(correoDTO);
                        }
                    }
                } else {
                    correoService.enviarCorreoV2(correoDTO);
                }
                return new ResponseEntity<>(send ? "OK" : "NO", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("DESTINATARIO VACIO", HttpStatus.BAD_REQUEST);
            }
        } catch (MailException e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }


    private List<File> convertToFiles(List<CorreoArchivoDTO> correoArchivoDTOS) {
        try {
            List<File> files = new ArrayList<>();
            File file;
            for (CorreoArchivoDTO correoArchivoDTO : correoArchivoDTOS) {
                System.out.println(correoArchivoDTO.getNombreArchivo());
                if (correoArchivoDTO.getNombreArchivo() != null && !correoArchivoDTO.getNombreArchivo().isEmpty()) {
                    file = new File(correoArchivoDTO.getNombreArchivo());
                    System.out.println("File exists " + file.exists());
                    if (file.exists()) {
                        files.add(file);
                    }
                }
            }
            return files;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "correo/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> search(@Valid Correo data, Pageable pageable) {
        Map<String, List> map = correoService.findAll(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "correo/reenviar", method = RequestMethod.POST)
    public ResponseEntity<?> updateNotificacion(@RequestBody Correo data) {
        try {
            correoService.reenviarCorreo(data);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
