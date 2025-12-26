/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
 * @author eduar
 */
@Entity
@Table(name = "ficha_proceso_linderos", schema = "catastro")
public class FichaProcesoLinderos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "ficha_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FichaProceso fichaProceso;
    @Column(name = "reg_ficha")
    private Long regFicha;
    @Column(name = "reg_ficha_lindero")
    private Long regFichaLindero;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegTipoLindero tipo;
    @Column(name = "linderante")
    private String linderante;
    @Column(name = "longitud")
    private BigDecimal longitud = BigDecimal.ZERO;
    @Column(name = "estado")
    private Boolean estado = true;

    public FichaProcesoLinderos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FichaProceso getFichaProceso() {
        return fichaProceso;
    }

    public void setFichaProceso(FichaProceso fichaProceso) {
        this.fichaProceso = fichaProceso;
    }

    public Long getRegFicha() {
        return regFicha;
    }

    public void setRegFicha(Long regFicha) {
        this.regFicha = regFicha;
    }

    public Long getRegFichaLindero() {
        return regFichaLindero;
    }

    public void setRegFichaLindero(Long regFichaLindero) {
        this.regFichaLindero = regFichaLindero;
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

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FichaProcesoLinderos{");
        sb.append("id=").append(id);
        sb.append(", fichaProceso=").append(fichaProceso);
        sb.append(", regFicha=").append(regFicha);
        sb.append(", regFichaLindero=").append(regFichaLindero);
        sb.append(", tipo=").append(tipo);
        sb.append(", linderante=").append(linderante);
        sb.append(", longitud=").append(longitud);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }

}
