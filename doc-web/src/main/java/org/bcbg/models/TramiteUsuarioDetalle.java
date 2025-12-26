/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

/**
 *
 * @author Ricardo
 */
public class TramiteUsuarioDetalle {

    private Long id;
    private TramiteUsuario tramiteUsuario;
    private UsuarioTareas usuarioTareas;
    private Boolean estado;

    public TramiteUsuarioDetalle() {
    }

    public TramiteUsuarioDetalle(TramiteUsuario tramiteUsuario, UsuarioTareas usuarioTareas) {
        this.tramiteUsuario = tramiteUsuario;
        this.usuarioTareas = usuarioTareas;
        this.estado = Boolean.TRUE;
    }

    public TramiteUsuarioDetalle(Long id) {
        this.id = id;
    }

    public TramiteUsuarioDetalle(Long id, TramiteUsuario tramiteUsuario, UsuarioTareas usuarioTareas) {
        this.id = id;
        this.tramiteUsuario = tramiteUsuario;
        this.usuarioTareas = usuarioTareas;
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

    public TramiteUsuario getTramiteUsuario() {
        return tramiteUsuario;
    }

    public void setTramiteUsuario(TramiteUsuario tramiteUsuario) {
        this.tramiteUsuario = tramiteUsuario;
    }

    public UsuarioTareas getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(UsuarioTareas usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
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
        if (!(object instanceof TramiteUsuarioDetalle)) {
            return false;
        }
        TramiteUsuarioDetalle other = (TramiteUsuarioDetalle) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "TramiteUsuarioDetalle{" + "id=" + id + ", tramiteUsuario=" + tramiteUsuario + ", usuarioTareas=" + usuarioTareas + '}';
    }

}
