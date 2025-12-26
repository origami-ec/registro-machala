/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Angel Navarro
 */
public class EnteSecuencia implements Serializable {
    
    

    private Long id;
    private BigInteger anio;
    private BigInteger secuencia;

    public EnteSecuencia() {
    }

    public EnteSecuencia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
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
        if (!(object instanceof EnteSecuencia)) {
            return false;
        }
        EnteSecuencia other = (EnteSecuencia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.EnteSecuencia[ id=" + id + " ]";
    }
    
}
