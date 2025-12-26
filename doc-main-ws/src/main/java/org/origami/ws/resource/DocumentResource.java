package org.origami.ws.resource;

import org.origami.ws.repository.origami.SolicitudDepartamentoRepository;
import org.origami.ws.entities.origami.SolicitudDepartamento;
import org.origami.ws.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/origami/api/")
public class DocumentResource {

    @Autowired
    private SolicitudDepartamentoRepository solicitudDepartamentoRepository;
    @Autowired
    private DocumentoService service;

    @GetMapping(produces = "application/pdf", value = "documento/solicitud/{idSolicitud}")
    public ResponseEntity<byte[]> viewReport(@PathVariable Long idSolicitud) {
        try {
            SolicitudDepartamento solicitudDepartamento = solicitudDepartamentoRepository.getById(idSolicitud);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=Tramite_" +
                    solicitudDepartamento.getSolicitud().getTramite().getCodigo() + ".pdf");
            return new ResponseEntity<>(service.generatedReportTramite(solicitudDepartamento), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
