/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Anyelo
 */
public class Observaciones implements Serializable {

    

    public Long id;
    private Long idProceso;
    private String observacion;
    private Date fecCre;
    private String userCre;
    private Boolean estado;
    private String tarea;
    private HistoricoTramites idTramite;

    public Observaciones() {
    }

    public Observaciones(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
    }

    public Observaciones(Long id) {
        this.id = id;
    }

    public Observaciones(Long id, String observacion, String userCre) {
        this.id = id;
        this.observacion = observacion;
        this.userCre = userCre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public HistoricoTramites getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
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
        if (!(object instanceof Observaciones)) {
            return false;
        }
        Observaciones other = (Observaciones) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.Observaciones[ id=" + id + " ]";
    }

}
