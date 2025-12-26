/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.managedbeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.bpmn.model.Message;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.Formato;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class FormatoMB extends BpmManageBeanBaseRoot implements Serializable {

    private LazyModelWS<Formato> lazy;
    private Formato formato;

    @PostConstruct
    public void init() {
        try {
            formato = new Formato();
            lazy = new LazyModelWS<>(SisVars.wsDocs + "formatos?sort=fecha,DESC", Formato[].class, session.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarFormato(Formato f) {
        this.formato = f;
    }

    public void guardarFormato() {
        if (Utils.isEmptyString(formato.getNombre())) {
            JsfUti.messageError(null, Messages.faltanCampos, "Ingrese el nombre del formato");
            return;
        }
        if (Utils.isEmptyString(formato.getExtension())) {
            JsfUti.messageError(null, Messages.faltanCampos, "Ingrese la extensión");
            return;
        }
        if (Utils.isEmptyString(formato.getFormato())) {
            JsfUti.messageError(null, Messages.faltanCampos, "Ingrese el tipo de formato");
            return;
        }
        if (Utils.isEmptyString(formato.getConversion())) {
            JsfUti.messageError(null, Messages.faltanCampos, "Ingrese el tipo de conversión");
            return;
        }
        formato.setNombre(formato.getNombre().trim());
        formato.setExtension(formato.getExtension().trim());
        formato.setFormato(formato.getFormato().trim());
        Formato rest = documentalService.guardarFormato(formato);
        if (rest != null) {
            JsfUti.messageInfo(null, Messages.correcto, "");
            formato = new Formato();
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public LazyModelWS<Formato> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<Formato> lazy) {
        this.lazy = lazy;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

}
