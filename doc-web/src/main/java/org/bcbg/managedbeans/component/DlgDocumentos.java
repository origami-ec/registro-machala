/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexCampoDto;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI
 */
@Named()
@ViewScoped
public class DlgDocumentos extends BpmManageBeanBaseRoot implements Serializable {

    private List<Indexacion> indices;
    private List<ArchivoDocs> archivosBusqueda;
    private ArchivoDocs archivo;
    private Indexacion indice;
    private String numTramite;

    @PostConstruct
    public void init() {
        try {
            indice = new Indexacion();
            indices = documentalService.getIndices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visualizarDocumento(ArchivoDocs docs) {
        if (!docs.getFormato().startsWith("image")) {
            getPDF(docs.getRuta());
        } else {
            JsfUti.redirectNewTab(Utils.getUrlViewImage(docs.getRuta()));
        }
    }

    public void seleccionarDocumento(ArchivoDocs docs) {
        PrimeFaces.current().dialog().closeDynamic(docs);
    }

    public void buscarArchivos() {
        System.out.println("buscarArchivos");
        if (indice == null || indice.get_id() == null) {
            JsfUti.messageError(null, Messages.escojaIndice, "");
            return;
        }
//        if (Utils.isEmptyString(indice.getDescripcionArchivo())) {
//            JsfUti.messageError(null, Messages.descripcionArchivo, "");
//            return;
//        }
//        
//        
//        
        archivosBusqueda = new ArrayList<>();
        ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
        archivoIndex.setNumTramite(numTramite.trim());
        archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo().trim());
        archivoIndex.setEstado(Boolean.TRUE);
        archivoIndex.setTipoIndexacion(indice.getDescripcion());
        List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
        for (IndexacionCampo ic : indice.getCampos()) {
            detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
        }
        archivoIndex.setDetalles(detalles);

        archivosBusqueda = documentalService.busquedaAvanzada(archivoIndex);
        if (archivosBusqueda == null) {
            JsfUti.messageError(null, "No se encontraron datos", "");
            archivosBusqueda = new ArrayList<>();
        }
    }

    public List<Indexacion> getIndices() {
        return indices;
    }

    public void setIndices(List<Indexacion> indices) {
        this.indices = indices;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Indexacion getIndice() {
        return indice;
    }

    public void setIndice(Indexacion indice) {
        this.indice = indice;
    }

    public List<ArchivoDocs> getArchivosBusqueda() {
        return archivosBusqueda;
    }

    public void setArchivosBusqueda(List<ArchivoDocs> archivosBusqueda) {
        this.archivosBusqueda = archivosBusqueda;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }
}
