/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.Rol;
import org.bcbg.entities.User;

/**
 *
 * @author Ricardo
 */
public class UsuarioTareas {

    private Long id;
    private User usuario;
    private Rol rol;
    private Boolean estado;
    private Boolean tramite;

    public UsuarioTareas() {
    }

    public UsuarioTareas(User usuario, Rol rol, Boolean estado, Boolean tramite) {
        this.usuario = usuario;
        this.rol = rol;
        this.estado = estado;
        this.tramite = tramite;
    }

    public UsuarioTareas(Rol rol, Boolean estado, Boolean tramite) {
        this.rol = rol;
        this.estado = estado;
        this.tramite = tramite;
    }

    public UsuarioTareas(Rol rol) {
        this.rol = rol;
        this.estado = Boolean.TRUE;
        this.tramite = Boolean.TRUE;
    }

    public UsuarioTareas(Long id) {
        this.id = id;
    }

    public Boolean getTramite() {
        return tramite;
    }

    public void setTramite(Boolean tramite) {
        this.tramite = tramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof UsuarioTareas)) {
            return false;
        }
        UsuarioTareas other = (UsuarioTareas) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "UsuarioTareas{" + "id=" + id + ", usuario=" + usuario + ", rol=" + rol + ", estado=" + estado + '}';
    }
}
