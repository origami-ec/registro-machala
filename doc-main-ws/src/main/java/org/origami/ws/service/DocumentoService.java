package org.origami.ws.service;

import org.origami.ws.entities.origami.SolicitudDepartamento;
import org.origami.ws.config.ApplicationProperties;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.origami.ws.dto.DataTramite;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.origami.ws.entities.security.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentoService {

    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private UsuarioService usuarioService;

    public byte[] generatedReportTramite(SolicitudDepartamento solicitudDepartamento) {
        try {
            SolicitudServicios solicitudServicios = solicitudDepartamento.getSolicitud();
            Usuario usuario = usuarioService.findOne(new Usuario(solicitudServicios.getUsuarioCreacion(), null, null, null));
            DataTramite tramite = new DataTramite(solicitudDepartamento.getDepartamento().getNombre(),
                    //solicitudServicios.getTramite().getNumTramite(),
                    solicitudServicios.getTramite().getCodigo(), solicitudServicios.getRepresentante(), solicitudServicios.getFechaCreacion(), solicitudServicios.getTipoServicio() != null ? solicitudServicios.getTipoServicio().getNombre() : solicitudServicios.getTipoTramite().getDescripcion(), solicitudServicios.getTipoServicio() != null ? solicitudServicios.getTipoServicio().getNombre() : "", usuario.getRecursoHumano().getPersona().getDetalleNombre(),solicitudServicios.getCampoReferencia());
            tramite.setObservacion(solicitudServicios.getDescripcionInconveniente() != null ? solicitudServicios.getDescripcionInconveniente() : "");
            tramite.setAsunto(solicitudServicios.getAsunto() != null ? solicitudServicios.getAsunto() : "");
            Map par = new HashMap();
            par.putAll(getUrlsImagenes());
            par.put("qrCode", properties.getUrlZull() + "reportes/inicioTramite/ticket/" + solicitudServicios.getId());
            return this.generarPDF(armarRutaJasper("TicketTramite"), tramite, par);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] generarPDF(String resourceLocation, Object dataSource, Map parameters) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile(resourceLocation);
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(dataSource));
        JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, jrBeanCollectionDataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public String armarRutaJasper(String nombre) {
        String os = System.getProperty("os.name");
        if (!properties.getActiveProfile().contains("prod")) {
            return "classpath:reportes/" + nombre + ".jasper";
        } else {
            return "C:\\origamigt\\services\\origami-ws\\reportes\\" + nombre + ".jasper";
        }

    }


    public  Map getUrlsImagenes() {
        String os = System.getProperty("os.name");
        Map map = new HashMap();
        if (!properties.getActiveProfile().contains("prod")) {
            map.put("HEADER_URL", "classpath:imagenes/cabecera.png");
            map.put("WATERMARK_URL", "classpath:imagenes/marca_agua.png");
            map.put("FOOTER_URL", "classpath:imagenes/pie_pagina.png");
        } else {
            map.put("HEADER_URL", "C:\\origamigt\\services\\origami-ws\\imagenes\\cabecera.png");
            map.put("WATERMARK_URL", "C:\\origamigt\\services\\origami-ws\\imagenes\\marca_agua.png");
            map.put("FOOTER_URL", "C:\\origamigt\\services\\origami-ws\\imagenes\\pie_pagina.png");
        }
        return map;
    }
}
