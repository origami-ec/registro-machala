/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.documental.entities.LibRegistroMercantil;
import com.origami.documental.entities.LibRegistroPropiedad;

import com.origami.documental.models.Imagen;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import com.origami.documental.services.ArchivosService;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class CropImagesM implements Serializable {

    @Inject
    private ArchivosService archivoHist;

    private Long id;
    private Integer numRepertorio;
    private Integer numInscripcion;
    private Long fechaIns;
    private Long libro;
    private Boolean propiedad;
    private Date fechaInscripcion;
    private Boolean enableOCR = false;
    private String textAux;
    private Long idTransaccion;
    private Long idFilter;
    private Map<String, Object> pms;
    private SimpleDateFormat sdf;

    private List<Imagen> imagenes;
    private Integer pagina = 0;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    public void initView() {
        try {
            pms = new HashMap<>();
            fechaInscripcion = new Date(fechaIns);
            sdf = new SimpleDateFormat("yyyy");
            pms.put("repertorio", numRepertorio.toString());
            pms.put("inscripcion", numInscripcion);
            pms.put("libro", libro.toString());
            pms.put("anioIns", sdf.format(fechaInscripcion));
            /*if (propiedad) {
                lazyProp = new LazyModelDocs<>(LibRegistroPropiedad.class, pms);
            } else {
                lazyMerc = new LazyModelDocs<>(LibRegistroMercantil.class, pms);
            }*/
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        try {
            String archivo = imagenes.get(pagina).getArchivo();
            this.textAux = archivoHist.getTextOfImage(archivo, e.getX1(), e.getX2(),
                    e.getY1(), e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                // Mostramos el texto
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            System.out.println(e);
        }
    }

    public void documentoPropiedad(LibRegistroPropiedad rp) {
        try {
            pagina = 0;
            imagenes = archivoHist.separarTiff(rp.getImagen(), rp.getIdTran(), id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void documentoMercantil(LibRegistroMercantil rm) {
        try {
            pagina = 0;
            imagenes = archivoHist.separarTiff(rm.getImagen(), rm.getIdTran(), id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectPage(PageEvent event) {
        try {
            pagina = event.getPage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Long getFechaIns() {
        return fechaIns;
    }

    public void setFechaIns(Long fechaIns) {
        this.fechaIns = fechaIns;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Long getLibro() {
        return libro;
    }

    public void setLibro(Long libro) {
        this.libro = libro;
    }

    public Boolean getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Boolean propiedad) {
        this.propiedad = propiedad;
    }

    public Boolean getEnableOCR() {
        return enableOCR;
    }

    public void setEnableOCR(Boolean enableOCR) {
        this.enableOCR = enableOCR;
    }

    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Long getIdFilter() {
        return idFilter;
    }

    public void setIdFilter(Long idFilter) {
        this.idFilter = idFilter;
    }

    /*public LazyModelDocs<LibRegistroPropiedad> getLazyProp() {
        return lazyProp;
    }

    public void setLazyProp(LazyModelDocs<LibRegistroPropiedad> lazyProp) {
        this.lazyProp = lazyProp;
    }

    public LazyModelDocs<LibRegistroMercantil> getLazyMerc() {
        return lazyMerc;
    }

    public void setLazyMerc(LazyModelDocs<LibRegistroMercantil> lazyMerc) {
        this.lazyMerc = lazyMerc;
    }*/

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

}
