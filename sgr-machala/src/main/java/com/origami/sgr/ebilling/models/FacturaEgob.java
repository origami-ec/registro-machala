/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.ebilling.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author eduar
 */
public class FacturaEgob implements Serializable {

    private Integer idModulo;
    private BigInteger idPropietarioEmision;
    private BigDecimal valorBase;
    private BigDecimal totalTarifa;
    private String fechaCreacion;
    private Integer usuarioCreacion;
    private Integer estado;
    private String concepto;
    private Integer idTramiteTipo;
    private List<FacturaDetalleEgob> facturaDetalle;

    public FacturaEgob() {
    }

    public Integer getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Integer idModulo) {
        this.idModulo = idModulo;
    }

    public BigInteger getIdPropietarioEmision() {
        return idPropietarioEmision;
    }

    public void setIdPropietarioEmision(BigInteger idPropietarioEmision) {
        this.idPropietarioEmision = idPropietarioEmision;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public BigDecimal getTotalTarifa() {
        return totalTarifa;
    }

    public void setTotalTarifa(BigDecimal totalTarifa) {
        this.totalTarifa = totalTarifa;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Integer usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Integer getIdTramiteTipo() {
        return idTramiteTipo;
    }

    public void setIdTramiteTipo(Integer idTramiteTipo) {
        this.idTramiteTipo = idTramiteTipo;
    }

    public List<FacturaDetalleEgob> getFacturaDetalle() {
        return facturaDetalle;
    }

    public void setFacturaDetalle(List<FacturaDetalleEgob> facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaEgob{");
        sb.append("idModulo=").append(idModulo);
        sb.append(", idPropietarioEmision=").append(idPropietarioEmision);
        sb.append(", valorBase=").append(valorBase);
        sb.append(", totalTarifa=").append(totalTarifa);
        sb.append(", fechaCreacion=").append(fechaCreacion);
        sb.append(", estado=").append(estado);
        sb.append(", concepto=").append(concepto);
        sb.append(", idTramiteTipo=").append(idTramiteTipo);
        sb.append(", facturaDetalle=").append(facturaDetalle);
        sb.append('}');
        return sb.toString();
    }

}
