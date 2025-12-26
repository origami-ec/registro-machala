/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anyelo
 */

public class CtlgCatalogo implements Serializable {

    private Long id;
    private String nombre;
    private String estado;
    private Collection<CtlgItem> ctlgItemCollection;

    public CtlgCatalogo() {
    }

    public CtlgCatalogo(Long id) {
        this.id = id;
    }

    public CtlgCatalogo(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<CtlgItem> getCtlgItemCollection() {
        return ctlgItemCollection;
    }

    public void setCtlgItemCollection(Collection<CtlgItem> ctlgItemCollection) {
        this.ctlgItemCollection = ctlgItemCollection;
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
        if (!(object instanceof CtlgCatalogo)) {
            return false;
        }
        CtlgCatalogo other = (CtlgCatalogo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CtlgCatalogo[ id=" + id + " ]";
    }
    
}
