/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.documental.entities.LibTramites;

import com.origami.documental.models.Imagen;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
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
public class CropOmegaDocs implements Serializable {

    @Inject
    private ArchivosService archivoHist;

    private Long tramite;
    private Long transaccion;
    private String textAux;
    private Boolean enableOCR = false;
    private Map<String, Object> pms;

    private List<Imagen> imagenes;
    private Integer pagina = 0;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    public void initView() {
        try {
            pagina = 0;
            pms = new HashMap<>();
            pms.put("idTran", transaccion);
            //lazydcos = new LazyModelDocs<>(LibTramites.class, pms);
            imagenes = archivoHist.separarTiffTramite(transaccion, tramite);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void documentoPropiedad(LibTramites rp) {
        try {
            pagina = 0;
            imagenes = archivoHist.separarTiffTramite(rp.getIdTran(), tramite);
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
    
    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Long getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Long transaccion) {
        this.transaccion = transaccion;
    }

    public Boolean getEnableOCR() {
        return enableOCR;
    }

    public void setEnableOCR(Boolean enableOCR) {
        this.enableOCR = enableOCR;
    }

    /*public LazyModelDocs<LibTramites> getLazydcos() {
        return lazydcos;
    }

    public void setLazydcos(LazyModelDocs<LibTramites> lazydcos) {
        this.lazydcos = lazydcos;
    }*/

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

}
