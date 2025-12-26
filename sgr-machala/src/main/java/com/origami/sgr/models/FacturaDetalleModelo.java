/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class FacturaDetalleModelo implements Serializable {
    
    public int productoId;
    public String descripcion;
    public int cantidad;
    public double precioUnitario;
    public double descuento;
    public double iva;
    public double subtotal;
    public double total;
    public double porcentajeIva;

    public FacturaDetalleModelo() {
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaDetalleModelo{");
        sb.append("productoId=").append(productoId);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", cantidad=").append(cantidad);
        sb.append(", precioUnitario=").append(precioUnitario);
        sb.append(", descuento=").append(descuento);
        sb.append(", iva=").append(iva);
        sb.append(", subtotal=").append(subtotal);
        sb.append(", total=").append(total);
        sb.append(", porcentajeIva=").append(porcentajeIva);
        sb.append('}');
        return sb.toString();
    }
    
}
