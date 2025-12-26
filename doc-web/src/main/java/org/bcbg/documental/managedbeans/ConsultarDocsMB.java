/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.managedbeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class ConsultarDocsMB extends BpmManageBeanBaseRoot implements Serializable {

    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private String numTramite;
    private String detalleDocumento;
    private String tramite;
    private Boolean buscar;

    @PostConstruct

    public void init() {
        try {
            buscar = Boolean.FALSE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultarDocumentos() {
        if (Utils.isEmptyString(detalleDocumento)) {
            JsfUti.messageError(null, "Escriba el texto a buscar", "");
            return;
        }
        buscar = Boolean.TRUE;
        lazyArchivos = new LazyModelWS<>(SisVars.wsDocs + "documentosBuscar?detalleDocumento=" + detalleDocumento + "&sort=id,ASC", session.getToken());
        lazyArchivos.setEntitiArray(ArchivoDocs[].class);
        JsfUti.update("formMain");
    }

    public void visualizarDocumento(ArchivoDocs docs) {
        getPDF(docs.getRuta());
    }

    public LazyModelWS<ArchivoDocs> getLazyArchivos() {
        return lazyArchivos;
    }

    public void setLazyArchivos(LazyModelWS<ArchivoDocs> lazyArchivos) {
        this.lazyArchivos = lazyArchivos;
    }

    public String getDetalleDocumento() {
        return detalleDocumento;
    }

    public void setDetalleDocumento(String detalleDocumento) {
        this.detalleDocumento = detalleDocumento;
    }

    public Boolean getBuscar() {
        return buscar;
    }

    public void setBuscar(Boolean buscar) {
        this.buscar = buscar;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

}
