/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.session.ServletSession;
import org.bcbg.util.Utils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class AyudaMB implements Serializable {

    @Inject
    protected ServletSession ss;
    private Boolean visualizarDoc;

    @PostConstruct
    public void init() {
        initDocument();
    }

    public void initDocument() {
        visualizarDoc = Boolean.TRUE;
        ss.instanciarParametros();
        ss.setNombreDocumento("Manual de usuario");
        ss.setUrlWebService(SisVars.wsMedia + "resource/pdf/" + Utils.getFilterRuta(SisVars.nombreManualVentanilla) + "/descarga/" + SisVars.VIEW_DOC);
    }

    public Boolean getVisualizarDoc() {
        return visualizarDoc;
    }

    public void setVisualizarDoc(Boolean visualizarDoc) {
        this.visualizarDoc = visualizarDoc;
    }

}
