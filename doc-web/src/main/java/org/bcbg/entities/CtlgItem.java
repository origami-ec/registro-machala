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
 * @author Anyelo
 */
public class CtlgItem implements Serializable {

    private Long id;
    private String valor;
    private String codename;
    private BigInteger referencia;
    private String estado = "A";
    private CtlgCatalogo catalogo;

    public CtlgItem() {
    }

    public CtlgItem(String codename) {
        this.codename = codename;
    }

    public CtlgItem(Long id) {
        this.id = id;
    }

    public CtlgItem(Long id, String valor, String codename) {
        this.id = id;
        this.valor = valor;
        this.codename = codename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public BigInteger getReferencia() {
        return referencia;
    }

    public void setReferencia(BigInteger referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
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
        if (!(object instanceof CtlgItem)) {
            return false;
        }
        CtlgItem other = (CtlgItem) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CtlgItem[ id=" + id + " ]";
    }

}
