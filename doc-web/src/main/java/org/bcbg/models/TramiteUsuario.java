/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.ServiciosDepartamento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ricardo
 */
public class TramiteUsuario {

    private Long id;
    private Boolean estado;
    private TramiteTareas tramiteTareas;
    private String zona;
    private ServiciosDepartamento serviciosDepartamento;
    private List<TramiteUsuarioDetalle> tramiteUsuarioDetalles;
    private List<UsuarioTareas> usuarioTareas;

    public TramiteUsuario() {
        this.estado = Boolean.TRUE;
        this.usuarioTareas = new ArrayList<>();
    }

    public TramiteUsuario(TramiteTareas tramiteTareas, String zona, Boolean estado) {
        this.estado = estado;
        this.tramiteTareas = tramiteTareas;
        this.zona = zona;
    }

    public TramiteUsuario(TramiteTareas tramiteTareas) {
        this.estado = Boolean.TRUE;
        this.tramiteTareas = tramiteTareas;
    }

    public TramiteUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ServiciosDepartamento getServiciosDepartamento() {
        return serviciosDepartamento;
    }

    public void setServiciosDepartamento(ServiciosDepartamento serviciosDepartamento) {
        this.serviciosDepartamento = serviciosDepartamento;
    }

    public List<TramiteUsuarioDetalle> getTramiteUsuarioDetalles() {
        return tramiteUsuarioDetalles;
    }

    public void setTramiteUsuarioDetalles(List<TramiteUsuarioDetalle> tramiteUsuarioDetalles) {
        this.tramiteUsuarioDetalles = tramiteUsuarioDetalles;
    }

    public List<UsuarioTareas> getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(List<UsuarioTareas> usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
    }

    public TramiteTareas getTramiteTareas() {
        return tramiteTareas;
    }

    public void setTramiteTareas(TramiteTareas tramiteTareas) {
        this.tramiteTareas = tramiteTareas;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
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
        if (!(object instanceof TramiteUsuario)) {
            return false;
        }
        TramiteUsuario other = (TramiteUsuario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

}
