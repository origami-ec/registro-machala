/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.ebilling.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author eduar
 */
public class FacturaDetalleEgob implements Serializable {

    private Integer cantidad;
    private BigDecimal valorUnitario;
    private Integer estado;
    private Integer idRubro;
    private String fechaCreacion;

    public FacturaDetalleEgob() {
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Integer idRubro) {
        this.idRubro = idRubro;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaDetalleEgob{");
        sb.append("cantidad=").append(cantidad);
        sb.append(", valorUnitario=").append(valorUnitario);
        sb.append(", estado=").append(estado);
        sb.append(", idRubro=").append(idRubro);
        sb.append(", fechaCreacion=").append(fechaCreacion);
        sb.append('}');
        return sb.toString();
    }

}
