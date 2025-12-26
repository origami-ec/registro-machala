/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.TipoTramite;

/**
 *
 * @author Ricardo
 */
public class TramiteTareas {

    private Long id;
    private String nombre;
    private TipoTramite tipoTramite;
    private String taskId;
    private Boolean inicio;
    private Boolean estado;

    public TramiteTareas() {
        estado = Boolean.TRUE;
    }

    public TramiteTareas(TipoTramite tipoTramite, Boolean inicio) {
        this.tipoTramite = tipoTramite;
        this.inicio = inicio;
        this.estado = Boolean.TRUE;
    }

    public TramiteTareas(Long id) {
        this.id = id;
    }

    public TramiteTareas(TipoTramite tipoTramite, String taskId) {
        this.tipoTramite = tipoTramite;
        this.taskId = taskId;
        this.estado = Boolean.TRUE;
    }

    public TramiteTareas(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
        this.estado = Boolean.TRUE;
    }

    public Boolean getInicio() {
        return inicio;
    }

    public void setInicio(Boolean inicio) {
        this.inicio = inicio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TramiteTareas)) {
            return false;
        }
        TramiteTareas other = (TramiteTareas) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "TramiteTareas{" + "id=" + id + ", nombre=" + nombre
                + ", tipoTramite=" + tipoTramite + ", taskId=" + taskId + '}';
    }

}
