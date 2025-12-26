/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.services.interfaces.RegCertificadoService;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class VerificacionQR implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private RegCertificadoService rcs;

    private String archivo;
    private String urlArchivo;
    private String rutaArchivo;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    protected void initView() {
        try {
            rutaArchivo = rcs.findByValidationCode2(archivo);
            ss.instanciarParametros();
            ss.setNombreDocumento(rutaArchivo);
            //urlArchivo = JsfUti.getHostContextUrl() + "DownLoadFiles";
            urlArchivo = SisVars.urlbase + "DownLoadFiles";
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

}
