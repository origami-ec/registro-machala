/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_ficha_linderos", schema = "app")
public class RegFichaLinderos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "ficha", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegFicha ficha;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegTipoLindero tipo;
    @Column(name = "linderante")
    private String linderante;
    @Column(name = "longitud")
    private BigDecimal longitud = BigDecimal.ZERO;
    @Column(name = "estado")
    private Boolean estado = true;

    public RegFichaLinderos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public RegTipoLindero getTipo() {
        return tipo;
    }

    public void setTipo(RegTipoLindero tipo) {
        this.tipo = tipo;
    }

    public String getLinderante() {
        return linderante;
    }

    public void setLinderante(String linderante) {
        this.linderante = linderante;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
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
        if (!(object instanceof RegFichaLinderos)) {
            return false;
        }
        RegFichaLinderos other = (RegFichaLinderos) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegFichaLinderos[ id=" + id + " ]";
    }
}
