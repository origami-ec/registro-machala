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
import org.apache.commons.lang3.StringUtils;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexCampoDto;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.Nota;
import org.bcbg.documental.models.Tesauro;
import org.bcbg.entities.Documentos;
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
public class ArchivosMB extends BpmManageBeanBaseRoot implements Serializable {

    private UploadedFile file;
    private Date fecha;
    private ArchivoDocs archivo;
    private Nota nota;

    private List<Documentos> documentosScan;
    private Documentos archivoDocuments;

    private List<Indexacion> indices;
    private Indexacion indice;
    private List<UploadedFile> archivos;
    private List<Tesauro> sugerencias;

    @PostConstruct
    public void init() {
        archivos = new ArrayList();
        fecha = new Date();
        nota = new Nota();
        indices = documentalService.getIndices();
        documentosScan = documentalService.getDocumentosScan();
        sugerencias = new ArrayList<>();
    }

    public void cargarArchivo(FileUploadEvent event) {
        this.file = null;
        System.out.println("event.getFile().getContentType():  " + event.getFile().getContentType());
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            System.out.println("file: " + file.getContentType());
            this.file = file;
            JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
        }
    }

    public void cargarArchivoMasivo(FileUploadEvent event) {

        if (event != null) {
            archivos.add(event.getFile());
            JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
        }
    }

    public void grabarArchivo() {
        try {
            if (this.validaciones()) {
                if (file != null) {
                    archivo = documentalService.guardarArchivo(file, crearAchivoDto(file.getContentType()));
                    if (archivo != null) {
                        if (Utils.isNotEmpty(archivo.getImagenes())) {
                            ss.borrarDatos();
                            ss.instanciarParametros();
                            ss.agregarParametro("archivoId", archivo.getId());
                            JsfUti.redirectFaces("procesos/documentos/documento");
                        } else if (Utils.isNotEmptyString(archivo.getId())) {
                            JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
                        } else {
                            JsfUti.messageError(null, Messages.intenteNuevamente, "");
                        }
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                    }
                } else {
                    JsfUti.messageError(null, Messages.subirArchivo, "");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void grabarArchivoMasivo() {
        if (validaciones()) {
            if (Utils.isNotEmpty(archivos)) {
                ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
                archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo());
                archivoIndex.setEstado(Boolean.TRUE);
                archivoIndex.setTipoIndexacion(indice.getDescripcion());
                List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
                for (IndexacionCampo ic : indice.getCampos()) {
                    detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
                }
                archivoIndex.setDetalles(detalles);
                for (UploadedFile f : archivos) {
                    archivo = documentalService.guardarArchivo(f, archivoIndex);
                    if (archivo != null) {
                        if (Utils.isEmpty(archivo.getImagenes())) {
                            JsfUti.messageError(null, Messages.intenteNuevamente, "");
                            return;
                        }
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                        return;
                    }
                }
                JsfUti.messageError(null, "Archivos cargados correctamente", "");
            } else {
                JsfUti.messageError(null, Messages.subirArchivo, "");
            }
        }
    }

    public ArchivoIndexDto crearAchivoDto(String contentType) {
        ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
        archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo());
        archivoIndex.setEstado(Boolean.TRUE);
        archivoIndex.setTipoIndexacion(indice.getDescripcion());
        archivoIndex.setFormatoUpload(contentType);
        List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
        for (IndexacionCampo ic : indice.getCampos()) {
            detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
        }
        archivoIndex.setDetalles(detalles);
        return archivoIndex;
    }

    public Boolean validaciones() {
        if (indice == null) {
            JsfUti.messageError(null, Messages.escojaIndice, "");
            return Boolean.FALSE;
        }
        /*if (Utils.isEmptyString(indice.getDescripcionArchivo())) {
            JsfUti.messageError(null, Messages.descripcionArchivo, "");
            return Boolean.FALSE;
        }*/
        for (IndexacionCampo ic : indice.getCampos()) {
            if (ic.getObligatorio()) {
                if (Utils.isEmptyString(ic.getDetalle())) {
                    JsfUti.messageError(null, Messages.camposObligatorios, "");
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {

    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public void validarCampoTesauro() {
        sugerencias = new ArrayList<>();
        if (indice != null && Utils.isNotEmptyString(indice.getDescripcionArchivo())) {
            String[] palabras = indice.getDescripcionArchivo().split(" ");
            for (String s : palabras) {
                s = s.trim().toLowerCase();
                Tesauro tes = documentalService.validarIndexacionTesauro(new Tesauro(s));
                if (tes != null) {
                    if (!sugerencias.contains(tes)) {
                        sugerencias.add(tes);
                    }
                }
            }
        }
    }

    public void abrirDocuments() {
        try {
            archivoDocuments = new Documentos();
            archivoDocuments.setEstado("0");
            archivoDocuments.setFecha(new Date());
            archivoDocuments.setUsuario(session.getName_user());
            archivoDocuments.setIp(session.getIpClient());
            archivoDocuments = documentalService.guardarDocumento(archivoDocuments);
            JsfUti.messageInfo(null, "Datos generados correctamente", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void actualizarDocumentosScan() {
        documentosScan = documentalService.getDocumentosScan();
        JsfUti.update("");
    }

    public void guardarArchivo(Documentos documentos) {
        try {
            if (validaciones()) {
                String contentType = "application/pdf";
                String nombreArchivo = new Date().getTime() + "_" + StringUtils.stripAccents(indice.getDescripcionArchivo()).replace(" ", "_").replace("-", "_").concat(".pdf");
                archivo = documentalService.guardarArchivo(contentType, nombreArchivo, documentos.getArchivo(), crearAchivoDto(contentType));
                if (archivo != null) {
                    if (Utils.isNotEmpty(archivo.getImagenes())) {
                        ss.borrarDatos();
                        ss.instanciarParametros();
                        ss.agregarParametro("archivoId", archivo.getId());
                        JsfUti.redirectFaces("procesos/documentos/documento");
                    } else if (Utils.isNotEmptyString(archivo.getId())) {
                        JsfUti.messageInfo(null, "Archivo cargado correctamente", "");
                    } else {
                        JsfUti.messageError(null, Messages.intenteNuevamente, "");
                    }
                } else {
                    JsfUti.messageError(null, Messages.intenteNuevamente, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDownloadFile(Long idDocuments) {
        JsfUti.redirectNewTab(SisVars.ws + "documents/pdf/" + idDocuments);
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
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

    public List<UploadedFile> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<UploadedFile> archivos) {
        this.archivos = archivos;
    }

    public List<Documentos> getDocumentosScan() {
        return documentosScan;
    }

    public void setDocumentosScan(List<Documentos> documentosScan) {
        this.documentosScan = documentosScan;
    }

    public Documentos getArchivoDocuments() {
        return archivoDocuments;
    }

    public void setArchivoDocuments(Documentos archivoDocuments) {
        this.archivoDocuments = archivoDocuments;
    }

    public List<Tesauro> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(List<Tesauro> sugerencias) {
        this.sugerencias = sugerencias;
    }

}
