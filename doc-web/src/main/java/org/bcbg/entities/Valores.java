/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Anyelo
 */
public class Valores implements Serializable {

    
    
    private Long id;
    private String code;
    private String valorString;
    private BigDecimal valorNumeric;

    public Valores() {
    }

    public Valores(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public BigDecimal getValorNumeric() {
        return valorNumeric;
    }

    public void setValorNumeric(BigDecimal valorNumeric) {
        this.valorNumeric = valorNumeric;
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
        if (!(object instanceof Valores)) {
            return false;
        }
        Valores other = (Valores) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Valores{" + "id=" + id + ", code=" + code + ", valorString=" + valorString + ", valorNumeric=" + valorNumeric + '}';
    }

}
