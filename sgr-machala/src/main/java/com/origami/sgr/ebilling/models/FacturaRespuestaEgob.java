/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.ebilling.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author eduar
 */
public class FacturaRespuestaEgob implements Serializable {

    private BigInteger id;
    private BigInteger idModulo;
    private BigInteger idPropietarioEmision;
    private String nroFactura;
    private BigDecimal valorBase;
    private BigDecimal valorTarifa;
    private Date fechaCreacion;
    private BigInteger usuarioCreacion;
    private Integer estado;
    private Integer pagado;

    public FacturaRespuestaEgob() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(BigInteger idModulo) {
        this.idModulo = idModulo;
    }

    public BigInteger getIdPropietarioEmision() {
        return idPropietarioEmision;
    }

    public void setIdPropietarioEmision(BigInteger idPropietarioEmision) {
        this.idPropietarioEmision = idPropietarioEmision;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public BigDecimal getValorTarifa() {
        return valorTarifa;
    }

    public void setValorTarifa(BigDecimal valorTarifa) {
        this.valorTarifa = valorTarifa;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(BigInteger usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaRespuestaEgob{");
        sb.append("id=").append(id);
        sb.append(", idModulo=").append(idModulo);
        sb.append(", idPropietarioEmision=").append(idPropietarioEmision);
        sb.append(", nroFactura=").append(nroFactura);
        sb.append(", valorBase=").append(valorBase);
        sb.append(", valorTarifa=").append(valorTarifa);
        sb.append(", fechaCreacion=").append(fechaCreacion);
        sb.append(", usuarioCreacion=").append(usuarioCreacion);
        sb.append(", estado=").append(estado);
        sb.append(", pagado=").append(pagado);
        sb.append('}');
        return sb.toString();
    }

}
