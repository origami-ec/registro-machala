/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Anyelo
 */
public class Rol implements Serializable {

    private Long id;
    private String nombre;
    private Boolean estado;
    private Boolean isDirector;
    private Boolean esSubDirector;
    private Collection<User> aclUserCollection;
    private Departamento departamento;
    private Collection<TipoTramite> geTipoTramiteCollection;
    private String accionesMenu;

    public Rol() {
    }

    public Rol(Departamento departamento) {
        this.departamento = departamento;
    }

    public Rol(Boolean isDirector, Departamento departamento) {
        this.isDirector = isDirector;
        this.departamento = departamento;
    }

    public Rol(Long id) {
        this.id = id;
    }

    public Rol(String nombre, Boolean estado, Departamento departamento) {
        this.nombre = nombre;
        this.estado = estado;
        this.departamento = departamento;
    }

    public Rol(Long id, String nombre, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Rol(String nombre, Boolean estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getAccionesMenu() {
        return accionesMenu;
    }

    public void setAccionesMenu(String accionesMenu) {
        this.accionesMenu = accionesMenu;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getIsDirector() {
        return isDirector;
    }

    public void setIsDirector(Boolean isDirector) {
        this.isDirector = isDirector;
    }

    public Boolean getEsSubDirector() {
        return esSubDirector;
    }

    public void setEsSubDirector(Boolean esSubDirector) {
        this.esSubDirector = esSubDirector;
    }

    public Collection<User> getAclUserCollection() {
        return aclUserCollection;
    }

    public void setAclUserCollection(Collection<User> aclUserCollection) {
        this.aclUserCollection = aclUserCollection;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Collection<TipoTramite> getGeTipoTramiteCollection() {
        return geTipoTramiteCollection;
    }

    public void setGeTipoTramiteCollection(Collection<TipoTramite> geTipoTramiteCollection) {
        this.geTipoTramiteCollection = geTipoTramiteCollection;
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
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.AclRol[ id=" + id + " ]";
    }

}
