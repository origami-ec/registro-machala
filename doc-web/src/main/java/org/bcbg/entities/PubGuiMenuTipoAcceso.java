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
public class PubGuiMenuTipoAcceso implements Serializable {

    
    
    private Integer id;
    private String identificador;
    private Collection<PubGuiMenu> pubGuiMenuCollection;

    public PubGuiMenuTipoAcceso() {
    }

    public PubGuiMenuTipoAcceso(Integer id) {
        this.id = id;
    }

    public PubGuiMenuTipoAcceso(Integer id, String identificador) {
        this.id = id;
        this.identificador = identificador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Collection<PubGuiMenu> getPubGuiMenuCollection() {
        return pubGuiMenuCollection;
    }

    public void setPubGuiMenuCollection(Collection<PubGuiMenu> pubGuiMenuCollection) {
        this.pubGuiMenuCollection = pubGuiMenuCollection;
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
        if (!(object instanceof PubGuiMenuTipoAcceso)) {
            return false;
        }
        PubGuiMenuTipoAcceso other = (PubGuiMenuTipoAcceso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.PubGuiMenuTipoAcceso[ id=" + id + " ]";
    }

}
