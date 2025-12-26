/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class HistoricoTramiteVentanillaDTO {

    private String idProcessInstance;
    private String usuariosAsignados;
    private Integer status;
    private String activitiKey;
    private String abreviatura;
    private String claveCatastral;

    public HistoricoTramiteVentanillaDTO() {
    }

    public HistoricoTramiteVentanillaDTO(String idProcessInstance, String usuariosAsignados, Integer status, String activitiKey, String abreviatura) {
        this.idProcessInstance = idProcessInstance;
        this.usuariosAsignados = usuariosAsignados;
        this.status = status;
        this.activitiKey = activitiKey;
        this.abreviatura = abreviatura;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getIdProcessInstance() {
        return idProcessInstance;
    }

    public void setIdProcessInstance(String idProcessInstance) {
        this.idProcessInstance = idProcessInstance;
    }

    public String getUsuariosAsignados() {
        return usuariosAsignados;
    }

    public void setUsuariosAsignados(String usuariosAsignados) {
        this.usuariosAsignados = usuariosAsignados;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActivitiKey() {
        return activitiKey;
    }

    public void setActivitiKey(String activitiKey) {
        this.activitiKey = activitiKey;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @Override
    public String toString() {
        return "HistoricoTramiteVentanillaDTO{" + "idProcessInstance=" + idProcessInstance + ", usuariosAsignados=" + usuariosAsignados + ", status=" + status + ", activitiKey=" + activitiKey + ", abreviatura=" + abreviatura + '}';
    }

}
