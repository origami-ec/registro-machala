/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bcbg.util.Utils;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author OrigamiEC
 */
public class ListaRequisitosModel implements Serializable {

    private ServiciosDepartamentoRequisitos serviciosRequisitos;
    private List<UploadedFile> uploadedFile;
    private List< File> file;

    public ListaRequisitosModel() {
    }

    public ServiciosDepartamentoRequisitos getServiciosRequisitos() {
        return serviciosRequisitos;
    }

    public void setServiciosRequisitos(ServiciosDepartamentoRequisitos serviciosRequisitos) {
        this.serviciosRequisitos = serviciosRequisitos;
    }

    public List<UploadedFile> getUploadedFile() {
        if (Utils.isEmpty(uploadedFile)) {
            uploadedFile = new ArrayList<>();
        }
        return uploadedFile;
    }

    public void setUploadedFile(List<UploadedFile> uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        getUploadedFile().add(uploadedFile);
        this.uploadedFile = getUploadedFile();
    }

    public List<File> getFile() {
        if (Utils.isEmpty(file)) {
            file = new ArrayList<>();
        }
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    public void setFile(File file) {
        getFile().add(file);
        this.file = getFile();
    }

}
