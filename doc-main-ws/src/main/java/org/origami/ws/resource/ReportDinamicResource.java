package org.origami.ws.resource;

import org.origami.ws.models.Data;
import org.origami.ws.models.DatosReporte;
import org.origami.ws.service.reports.ReportDinamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/origami/api/")
public class ReportDinamicResource {
    @Autowired
    private ReportDinamicService service;


    @RequestMapping( value = "reporte/dinamico", method = RequestMethod.POST)
    public ResponseEntity<Data> reporteTareasPendientes(@RequestBody DatosReporte data) {
        try {

            return new ResponseEntity<>(service.generarReporteDinamico(data),  HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
