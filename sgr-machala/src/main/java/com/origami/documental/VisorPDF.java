/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental;

import com.origami.config.SisVars;
import com.origami.documental.lazy.LazyModelWS;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.InfoNotasPdf;
import com.origami.documental.services.DocumentalService;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class VisorPDF implements Serializable {

    @Inject
    protected UserSession us;
    @Inject
    private Entitymanager em;
    @Inject
    private ServletSession ss;
    @Inject
    private DocumentalService doc;
    @Inject
    private RegistroPropiedadServices reg;

    private Long idMov;
    private String archivo;
    private String urlArchivo;
    private String urlAnotaciones;
    private String periodo;
    private String libro;
    private String inscripcion;
    private String visorUrl;
    private RegMovimiento movimiento;

    private ArchivoDocs archivoDoc;
    private List<ArchivoDocs> listaArchivo;
    private LazyModelWS<ArchivoDocs> lazyArchivos;

    private List<InfoNotasPdf> anotaciones;
    private List<RegMovimientoMarginacion> marginaciones;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    protected void initView() {
        try {
            archivoDoc = new ArchivoDocs();
            movimiento = new RegMovimiento();
            if (idMov != null) {
                movimiento = em.find(RegMovimiento.class, idMov);
                if (movimiento == null) {
                    movimiento = new RegMovimiento();
                } else {
                    marginaciones = reg.getRegMovMargByIdMov(idMov);
                }
            }

            if (movimiento.getTramite() != null) {
                archivoDoc = doc.consultarArchivoTramite(movimiento.getNumeroTramite());
                if (archivoDoc != null && archivoDoc.getRuta() != null) {
                    visorUrl = Utils.getUrlViewPdf(archivoDoc.getRuta());
                }
            }

            inscripcion = Utils.completarCadenaConCeros(inscripcion, 6);
            archivo = periodo + libro + inscripcion + ".pdf";
            urlArchivo = SisVars.urlOrigamiMedia + "libros/archivo/app-libros-" + periodo + "-" + libro + "-" + archivo;
            urlAnotaciones = SisVars.urlOrigamiMedia + "anotaciones/app-libros-" + periodo + "-" + libro + "-" + archivo;
            anotaciones = doc.getNotasPdf(urlAnotaciones);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(String inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getUrlAnotaciones() {
        return urlAnotaciones;
    }

    public void setUrlAnotaciones(String urlAnotaciones) {
        this.urlAnotaciones = urlAnotaciones;
    }

    public List<InfoNotasPdf> getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(List<InfoNotasPdf> anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Long getIdMov() {
        return idMov;
    }

    public void setIdMov(Long idMov) {
        this.idMov = idMov;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public String getVisorUrl() {
        return visorUrl;
    }

    public void setVisorUrl(String visorUrl) {
        this.visorUrl = visorUrl;
    }

    public List<RegMovimientoMarginacion> getMarginaciones() {
        return marginaciones;
    }

    public void setMarginaciones(List<RegMovimientoMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }

    public ArchivoDocs getArchivoDoc() {
        return archivoDoc;
    }

    public void setArchivoDoc(ArchivoDocs archivoDoc) {
        this.archivoDoc = archivoDoc;
    }

}
