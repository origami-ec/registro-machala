/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.entities.firmaelectronica.DocumentoElectronico;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class ValidarDocumentoMB implements Serializable {

    @Inject
    private BcbgService service;
    private FirmaElectronica firmaElectronica;
    private DocumentoElectronico documento;
    private Boolean validar;

    @PostConstruct
    private void init() {
        loadModel();
    }

    private void loadModel() {
        validar = Boolean.FALSE;
        firmaElectronica = new FirmaElectronica();
        documento = new DocumentoElectronico();
    }

    public void validarDocumento() {
        if (Utils.isNotEmptyString(firmaElectronica.getArchivoFirmado())) {
            validar = Boolean.TRUE;
            documento = (DocumentoElectronico) service.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/verificarDocumento", DocumentoElectronico.class);
        } else {
            validar = Boolean.FALSE;
            JsfUti.messageError(null, "Debe subir un documento para continuar", "");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            File file = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
            firmaElectronica.setArchivo(event.getFile().getFileName());
            firmaElectronica.setArchivoFirmado(file.getAbsolutePath());
            JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");

        } catch (IOException e) {
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
        }
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public DocumentoElectronico getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoElectronico documento) {
        this.documento = documento;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

}
