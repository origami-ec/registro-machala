package org.origami.ws.resource;

import java.util.List;

import org.origami.ws.dto.SolicitudServiciosDTO;
import org.origami.ws.entities.origami.Tramites;
import org.origami.ws.models.Data;
import org.origami.ws.models.DatosReporte;
import org.origami.ws.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origami/api/")
public class ReporteResource {

    @Autowired
    private ReporteService service;

    @GetMapping(produces = "application/pdf", value = "reportes/inicioTramite/ticket/{solicitud}")
    public ResponseEntity<byte[]> reporteTicketTramite(@PathVariable Long solicitud) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=TicketTramite.pdf");
            return new ResponseEntity<>(service.getReporteTicketTramite(solicitud), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/pdf", value = "reportes/inicioTramite/reimprimirTicket/{solicitud}")
    public ResponseEntity<byte[]> reporteReimprimirTicketTramite(@PathVariable Long solicitud) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=TicketTramite.pdf");
            return new ResponseEntity<>(service.getReporteReimprimirTicketTramite(solicitud), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "reportes/informes", method = RequestMethod.POST)
    public ResponseEntity<?> reporteInformesSolicitudSave(@RequestBody Data data) {
        try {
            return new ResponseEntity<>(service.getInformeSolicitud(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/pdf", value = "reportes/tareaspendientes/usuario/", method = RequestMethod.POST)
    public ResponseEntity<byte[]> reporteTareasPendientes(@RequestBody DatosReporte data) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReporteBandejaTareas.pdf");
            return new ResponseEntity<>(service.getReporteBandejaTareas(data), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/pdf", value = "reportes/tareasrealizadas/usuario/", method = RequestMethod.POST)
    public ResponseEntity<byte[]> reporteTareasRealizadas(@RequestBody DatosReporte data) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReporteBandejaTareas.pdf");
            return new ResponseEntity<>(service.getReporteTareasRealizadas(data), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/pdf", value = "reportes/tramites/{nombreReporte}", method = RequestMethod.POST)
    public ResponseEntity<byte[]> reporteTramites(@RequestBody List<Tramites> data, @PathVariable String nombreReporte) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=ReporteBandejaTareas.pdf");
            return new ResponseEntity<>(service.getReporteAnalisis(data, nombreReporte), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/pdf", value = "reportes/solicitudServicio/{nombreReporte}", method = RequestMethod.POST)
    public ResponseEntity<byte[]> reporteSolicitudServicios(@RequestBody SolicitudServiciosDTO data, @PathVariable String nombreReporte) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename="+nombreReporte+".pdf");
            return new ResponseEntity<>(service.getReporteSolicitudServicio(data, nombreReporte), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(produces = "application/pdf", value = "reportes/notificacion/{notificacion}")
    public ResponseEntity<byte[]> reporteNotificacion(@PathVariable Long notificacion) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=Notificacion.pdf");
            return new ResponseEntity<>(service.getReporteNotificacion(notificacion), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
