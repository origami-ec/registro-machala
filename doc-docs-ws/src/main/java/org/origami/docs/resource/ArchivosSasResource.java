package org.origami.docs.resource;


import org.apache.commons.io.FileUtils;
import org.origami.docs.model.ArchivoDto;
import org.origami.docs.service.ArchivosSasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ArchivosSasResource {

    @Autowired
    private ArchivosSasService service;

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
    public ResponseEntity<?> cargarDocumento(@RequestParam String formato, @RequestParam String usuario, @RequestParam String indexacion, @RequestPart MultipartFile document, @RequestParam(required = false) Boolean generarImagenes) {
        ArchivoDto response = service.cargarDocumento(formato, usuario, indexacion, document, generarImagenes);
        System.out.println("Response: " + response);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(path = "archivo/consultar", method = RequestMethod.POST)
    public ResponseEntity<?> consultarArchivo(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.consultarArchivo(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "archivo/consultarList", method = RequestMethod.POST)
    public ResponseEntity<?> consultarArchivos(@RequestBody ArchivoDto dto) {
        List<ArchivoDto> response = service.consultarArchivos(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "archivo/consultarXreferencia", method = RequestMethod.POST)
    public ResponseEntity<?> consultarXreferencia(@RequestBody ArchivoDto dto) {
        List<ArchivoDto> response = service.consultarXreferencia(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "archivo/eliminar", method = RequestMethod.POST)
    public ResponseEntity<?> eliminarArchivo(@RequestBody ArchivoDto dto) {
        ArchivoDto response = service.eliminarArchivo(dto);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "archivo/fileToString", method = RequestMethod.POST)
    public ResponseEntity<?> convertFileToString(@RequestBody File archivo) {
        return new ResponseEntity<>(service.convertFileToString(archivo), HttpStatus.OK);
    }

    @RequestMapping(value = "archivo/fileToBytes", method = RequestMethod.POST)
    public ResponseEntity<?> convertFileToBytes(@RequestBody File archivo) {
        return new ResponseEntity<>(service.convertFileToBytes(archivo), HttpStatus.OK);
    }

    @RequestMapping(path = "archivo/consultarDocumento/{archivoId}", method = RequestMethod.GET)
    public void consultarArchivo(@PathVariable String archivoId, HttpServletResponse response) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        ArchivoDto dto = service.consultarArchivo(new ArchivoDto(archivoId));
        byte[] archivo = FileUtils.readFileToByteArray(new File(dto.getRuta()));
        if (archivo != null) {
            response.setContentType(dto.getFormato().equals("IMG") ? MediaType.IMAGE_JPEG_VALUE : MediaType.APPLICATION_PDF_VALUE);
            response.setContentLength(archivo.length);
            response.setHeader("Content-disposition", "inline; filename=" + dto.getNombre());
            headers.setContentLength(archivo.length);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            response.getOutputStream().write(archivo);
        }
        response.getOutputStream();
    }

    @RequestMapping(path = "archivo/consultarXruta/{ruta}", method = RequestMethod.GET)
    public void consultarXruta(@PathVariable String ruta, HttpServletResponse response) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        String ruta64 = new String(base64.decode(ruta.getBytes()));
        ArchivoDto archivoId = new ArchivoDto();
        archivoId.setRuta(ruta64);
        ArchivoDto dto = service.consultarArchivo(archivoId);
        byte[] archivo = FileUtils.readFileToByteArray(new File(dto.getRuta()));
        if (archivo != null) {
            response.setContentType(dto.getFormato().equals("IMG") ? MediaType.IMAGE_JPEG_VALUE : MediaType.APPLICATION_PDF_VALUE);
            response.setContentLength(archivo.length);
            response.setHeader("Content-disposition", "inline; filename=" + dto.getNombre());
            headers.setContentLength(archivo.length);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            response.getOutputStream().write(archivo);
        }
        response.getOutputStream();
    }

    @PostMapping(path = "reemplazar/archivo")
    public ResponseEntity<?> reemplazarArchivo(@RequestBody ArchivoDto dto) {
        Boolean response = service.reemplazarArchivo(dto);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", response);

        responseBody.put("message", response ? "Archivo reemplazado con éxito" : "Error al reemplazar el archivo");

        return new ResponseEntity<>(responseBody, response ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
