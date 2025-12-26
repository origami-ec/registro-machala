/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import org.bcbg.models.Document;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Anyelo
 */
public class PubGuiMenuRol implements Serializable {

    private Long id;
    private Boolean esDirector;
    private PubGuiMenu menu;
    private Long rol;
    private String acciones;
    private List<Document> documents;
    private String permiso;

    public PubGuiMenuRol() {
    }

    public PubGuiMenuRol(Long id) {
        this.id = id;
    }

    public PubGuiMenuRol(Long id, Long rol) {
        this.id = id;
        this.rol = rol;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEsDirector() {
        return esDirector;
    }

    public void setEsDirector(Boolean esDirector) {
        this.esDirector = esDirector;
    }

    public PubGuiMenu getMenu() {
        return menu;
    }

    public void setMenu(PubGuiMenu menu) {
        this.menu = menu;
    }

    public Long getRol() {
        return rol;
    }

    public void setRol(Long rol) {
        this.rol = rol;
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
        if (!(object instanceof PubGuiMenuRol)) {
            return false;
        }
        PubGuiMenuRol other = (PubGuiMenuRol) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.PubGuiMenuRol[ id=" + id + " ]";
    }

}
