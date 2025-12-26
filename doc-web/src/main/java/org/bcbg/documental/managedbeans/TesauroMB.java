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
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.TipoDato;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.Tesauro;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class TesauroMB extends BpmManageBeanBaseRoot implements Serializable {

    private Tesauro tesauro;

    private Boolean activaGenerar;
    private String campo, palabra, sugerencia, id;
    private LazyModelWS<Tesauro> lazy;

    private List<String> sugerencias;
    private Boolean obligatorio;

    @PostConstruct
    public void init() {
        cargarVariables();
    }

    public void cargarVariables() {
        id = null;
        activaGenerar = Boolean.TRUE;
        obligatorio = Boolean.FALSE;
        campo = "";
        palabra = "";
        sugerencia = "";
        sugerencias = new ArrayList<>();
        lazy = new LazyModelWS<>(SisVars.wsDocs + "tesauro?estado=true&sort=fecha,DESC", Tesauro[].class, session.getToken());
    }

    public void agregarSugerencia() {
        if (Utils.isEmptyString(sugerencia)) {
            JsfUti.messageError(null, "Debe ingresar la categoria del campo para continuar", "");
            return;
        }
        if (Utils.isNotEmpty(sugerencias)) {
            for (String cat : sugerencias) {
                if (cat.equals(sugerencias)) {
                    JsfUti.messageError(null, "Ya existe una sugerencia con la misma descripción", "");
                    return;
                }
            }
        }
        sugerencias.add(sugerencia);
    }

    public void guardarTesauro() {
        if (Utils.isEmptyString(palabra)) {
            JsfUti.messageError(null, "Debe ingresar la palabra del formulario para continuar", "");
            return;
        }
        if (Utils.isEmpty(sugerencias)) {
            JsfUti.messageError(null, "Debe ingresar los sugerencias para " + palabra, "");
            return;
        }
        tesauro = new Tesauro();
        tesauro.set_id(id);
        tesauro.setEstado(Boolean.TRUE);
        tesauro.setFecha(new Date());
        tesauro.setPalabra(palabra);
        tesauro.setSugerencias(sugerencias);
        Boolean guardar = Boolean.FALSE;
        if (Utils.isEmptyString(id)) {
            tesauro.setUsuario(session.getUsuarioDocs());
            Tesauro rest = documentalService.validarIndexacionTesauro(new Tesauro(palabra));
            if (rest == null) {
                guardar = Boolean.TRUE;
            }
        } else {
            tesauro.setUsuarioEdita(session.getUsuarioDocs());
            guardar = Boolean.TRUE;
        }

        if (guardar) {
            Tesauro rest = documentalService.guardarTesauro(tesauro);
            if (rest != null) {
                cargarVariables();
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
            } else {
                JsfUti.messageError(null, Messages.intenteNuevamente, "");
            }
        } else {
            JsfUti.messageError(null, Messages.existeIndexacionFormulario, "");
        }
    }

    public void eliminarSugerencia(int index) {
        if (Utils.isNotEmpty(sugerencias)) {
            sugerencias.remove(index);
        }
    }

    public void editarSugerencia(Tesauro index) {
        palabra = index.getPalabra();
        sugerencias = index.getSugerencias();
        id = index.getId();
    }

    public void eliminarSugerencias(Tesauro index) {
        index.setEstado(Boolean.FALSE);
        index.setUsuarioEdita(session.getUsuarioDocs());
        Tesauro rest = documentalService.guardarTesauro(index);
        if (rest != null) {
            cargarVariables();
            JsfUti.messageInfo(null, Messages.transaccionOK, "");
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public Tesauro getTesauro() {
        return tesauro;
    }

    public void setTesauro(Tesauro tesauro) {
        this.tesauro = tesauro;
    }

    public Boolean getActivaGenerar() {
        return activaGenerar;
    }

    public void setActivaGenerar(Boolean activaGenerar) {
        this.activaGenerar = activaGenerar;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LazyModelWS<Tesauro> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<Tesauro> lazy) {
        this.lazy = lazy;
    }

    public List<String> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(List<String> sugerencias) {
        this.sugerencias = sugerencias;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

}
