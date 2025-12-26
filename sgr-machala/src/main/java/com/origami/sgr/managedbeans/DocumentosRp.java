/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DocumentosRp implements Serializable {

    @Inject
    private ServletSession ss;

    private String rutaArchivo;

    @PostConstruct
    protected void iniView() {
        try {

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void descargarDocumento(String url) {
        JsfUti.redirectNewTab(SisVars.urlbase + "DescargarDocsRepositorio?id=" + url);
    }

    public void visualizarDocumento(String nombre) {
        try {
            rutaArchivo = SisVars.rutaDocumentos + nombre + ".pdf";
            ss.instanciarParametros();
            ss.setNombreDocumento(rutaArchivo);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
