/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexCampoDto;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.Nota;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class ArchivosMasivosMB extends BpmManageBeanBaseRoot implements Serializable {

    private List<UploadedFile> file;
    private Date fecha;
    private ArchivoDocs archivo;
    private Nota nota;

    private List<Indexacion> indices;
    private Indexacion indice;

    @PostConstruct
    public void init() {
        fecha = new Date();
        nota = new Nota();
        indices = documentalService.getIndices();
        file = new ArrayList<>();
    }

    public void cargarArchivo(FileUploadEvent event) {
        this.file = null;
        System.out.println("event.getFile().getContentType():  " + event.getFile().getContentType());
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            System.out.println("file: " + file.getContentType());
            this.file.add(file);
            JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
        }
    }

    public void grabarArchivo() {
        if (indice == null) {
            JsfUti.messageError(null, Messages.escojaIndice, "");
            return;
        }
        if (Utils.isEmptyString(indice.getDescripcionArchivo())) {
            JsfUti.messageError(null, Messages.descripcionArchivo, "");
            return;
        }
        for (IndexacionCampo ic : indice.getCampos()) {
            if (ic.getObligatorio()) {
                if (Utils.isEmptyString(ic.getDetalle())) {
                    JsfUti.messageError(null, Messages.camposObligatorios, "");
                    return;
                }
            }
        }
        if (file != null) {

            ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
            archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo());
            archivoIndex.setEstado(Boolean.TRUE);
            archivoIndex.setTipoIndexacion(indice.getDescripcion());
            List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
            for (IndexacionCampo ic : indice.getCampos()) {
                detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
            }
            archivoIndex.setDetalles(detalles);
            Boolean ok = Boolean.FALSE;
            for (UploadedFile up : file) {
                archivo = documentalService.guardarArchivo(up, archivoIndex);
                if (archivo != null) {
                    ok = Boolean.TRUE;
                } else {
                    ok = Boolean.FALSE;
                    break;
                }
            }
            if (ok) {
                file = new ArrayList<>();
                JsfUti.messageError(null, Messages.correcto, "");
            } else {
                JsfUti.messageError(null, Messages.intenteNuevamente, "");
            }
        } else {
            JsfUti.messageError(null, Messages.subirArchivo, "");
        }

    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {

    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public List<Indexacion> getIndices() {
        return indices;
    }

    public void setIndices(List<Indexacion> indices) {
        this.indices = indices;
    }

    public Indexacion getIndice() {
        return indice;
    }

    public void setIndice(Indexacion indice) {
        this.indice = indice;
    }

}
