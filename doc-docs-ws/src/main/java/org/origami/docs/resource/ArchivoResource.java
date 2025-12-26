package org.origami.docs.resource;

import org.origami.docs.model.ArchivoDto;
import org.origami.docs.model.ArchivoIndexDto;
import org.origami.docs.model.Data;
import org.origami.docs.model.ImagenNotaDto;
import org.origami.docs.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/origami/docs/")
public class ArchivoResource {

    @Autowired
    private ArchivoService service;

    @RequestMapping(value = "misDocumentos", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid ArchivoDto data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "documentosBuscar", method = RequestMethod.GET)
    public ResponseEntity<?> documentosBuscar(@Valid ArchivoDto data, Pageable pageable) {
        Map<String, List> map = service.find2(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "archivo/cargarDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> cargarDocumento(@RequestParam String formato, @RequestParam String usuario,
            @RequestParam String indexacion, @RequestPart MultipartFile document) {
        ArchivoDto response = service.cargarDocumento(formato, usuario, indexacion, document);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "archivo/consultar", method = RequestMethod.POST)
    public ResponseEntity<?> consultarArchivo(@RequestBody ArchivoDto dto) {
        try{
            ArchivoDto response = service.consultarArchivo(dto);
            return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "archivo/visualizar", method = RequestMethod.POST)
    public ResponseEntity<?> visualizarArchivo(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.visualizarArchivo(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "archivo/convertir", method = RequestMethod.POST)
    public ResponseEntity<?> convertirArchivo(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.convertirArchivo(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "archivo/eliminar", method = RequestMethod.POST)
    public ResponseEntity<?> eliminarArchivo(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.eliminarArchivo(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "archivo/actualizarDatosIndex", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarDatosIndex(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.actualizarDatosIndex(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "imagen/{imagen}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable String imagen) throws IOException {
        return service.consultarImagenes(imagen);
    }

    @RequestMapping(path = "archivo/agregarNota", method = RequestMethod.POST)
    public ResponseEntity<?> agregarNota(@RequestBody ImagenNotaDto dto) {
        Data response = service.agregarNota(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "archivo/imprimirNotas", method = RequestMethod.POST)
    public ResponseEntity<?> agregarNota(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.imprimirArchivo(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "archivo/busquedaAvanzada", method = RequestMethod.POST)
    public ResponseEntity<?> findAll(@RequestBody ArchivoIndexDto indexacion) {
        return new ResponseEntity<>(service.busquedaAvanzada(indexacion), HttpStatus.OK);
    }

    @RequestMapping(value = "archivo/sellos", method = RequestMethod.GET)
    public ResponseEntity<?> sellos() {
        return new ResponseEntity<>(service.obtenerSellos(), HttpStatus.OK);
    }
    
    @RequestMapping(path = "archivo/tramite/{numTramite}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarArchivoTramite(@PathVariable String numTramite) {
        try{
            ArchivoDto response = service.consultarArchivoTramite(numTramite);
            return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

}
