/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author eduar
 */
public class FacturaModelo implements Serializable {
    
    public Long referenciaId;
    public String fechaEmision;
    public Long tramite;
    public int vendedorId;
    public int empresaId;
    public int sucursalId;
    public int formaPagoId;
    public String descripcion;
    public double subtotal;
    public double totalDescuento;
    public double totalSinInpuesto;
    public double totalCeroIva;
    public double subtotalIva;
    public double total;
    public double porcentajeIva;
    public FacturaClienteModelo cliente;
    public List<FacturaDetalleModelo> detalle;

    public FacturaModelo() {
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public int getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(int sucursalId) {
        this.sucursalId = sucursalId;
    }

    public int getFormaPagoId() {
        return formaPagoId;
    }

    public void setFormaPagoId(int formaPagoId) {
        this.formaPagoId = formaPagoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(double totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public double getTotalSinInpuesto() {
        return totalSinInpuesto;
    }

    public void setTotalSinInpuesto(double totalSinInpuesto) {
        this.totalSinInpuesto = totalSinInpuesto;
    }

    public double getTotalCeroIva() {
        return totalCeroIva;
    }

    public void setTotalCeroIva(double totalCeroIva) {
        this.totalCeroIva = totalCeroIva;
    }

    public double getSubtotalIva() {
        return subtotalIva;
    }

    public void setSubtotalIva(double subtotalIva) {
        this.subtotalIva = subtotalIva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(double porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public FacturaClienteModelo getCliente() {
        return cliente;
    }

    public void setCliente(FacturaClienteModelo cliente) {
        this.cliente = cliente;
    }

    public List<FacturaDetalleModelo> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FacturaDetalleModelo> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaModelo{");
        sb.append("referenciaId=").append(referenciaId);
        sb.append(", fechaEmision=").append(fechaEmision);
        sb.append(", tramite=").append(tramite);
        sb.append(", vendedorId=").append(vendedorId);
        sb.append(", empresaId=").append(empresaId);
        sb.append(", sucursalId=").append(sucursalId);
        sb.append(", formaPagoId=").append(formaPagoId);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", subtotal=").append(subtotal);
        sb.append(", totalDescuento=").append(totalDescuento);
        sb.append(", totalSinInpuesto=").append(totalSinInpuesto);
        sb.append(", totalCeroIva=").append(totalCeroIva);
        sb.append(", subtotalIva=").append(subtotalIva);
        sb.append(", total=").append(total);
        sb.append(", porcentajeIva=").append(porcentajeIva);
        sb.append(", cliente=").append(cliente);
        sb.append(", detalle=").append(detalle);
        sb.append('}');
        return sb.toString();
    }
    
}
