/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import com.origami.sgr.util.EjbsCaller;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "reg_movimiento_referencia", schema = "app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegMovimientoReferencia.findAll", query = "SELECT r FROM RegMovimientoReferencia r"),
    @NamedQuery(name = "RegMovimientoReferencia.findById", query = "SELECT r FROM RegMovimientoReferencia r WHERE r.id = :id"),
    @NamedQuery(name = "RegMovimientoReferencia.findByMovimiento", query = "SELECT r FROM RegMovimientoReferencia r WHERE r.movimiento = :movimiento")})
public class RegMovimientoReferencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "movimiento")
    private Long movimiento;
    @JoinColumn(name = "movimiento_referencia", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegMovimiento movimientoReferencia;

    @Transient
    private RegMovimiento movimiento1;

    public RegMovimientoReferencia() {
    }

    public RegMovimientoReferencia(Long id) {
        this.id = id;
    }

    public RegMovimientoReferencia(Long id, Long movimiento) {
        this.id = id;
        this.movimiento = movimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Long movimiento) {
        this.movimiento = movimiento;
    }

    public RegMovimiento getMovimientoReferencia() {
        return movimientoReferencia;
    }

    public void setMovimientoReferencia(RegMovimiento movimientoReferencia) {
        this.movimientoReferencia = movimientoReferencia;
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
        if (!(object instanceof RegMovimientoReferencia)) {
            return false;
        }
        RegMovimientoReferencia other = (RegMovimientoReferencia) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.RegMovimientoReferencia[ id=" + id + " ]";
    }

    public RegMovimiento getMovimientoRef() {
        if (movimiento1 == null) {
            movimiento1 = EjbsCaller.getTransactionManager().find(RegMovimiento.class, this.movimiento);
        }
        return movimiento1;
    }

    public RegMovimiento getMovimiento1() {
        return movimiento1;
    }

    public void setMovimiento1(RegMovimiento movimiento1) {
        this.movimiento1 = movimiento1;
    }

}
