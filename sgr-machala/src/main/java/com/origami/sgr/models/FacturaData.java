/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author eduar
 */
public class FacturaData implements Serializable {

    private String mensaje;
    private String clave_acceso;
    private String numero;
    private BigInteger factura_id;
    private BigInteger referencia;
    private BigInteger tramite;
    private String numero_autorizacion;
    private String fecha_autorizacion;
    private String estado;

    public FacturaData() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getClave_acceso() {
        return clave_acceso;
    }

    public void setClave_acceso(String clave_acceso) {
        this.clave_acceso = clave_acceso;
    }

    public BigInteger getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(BigInteger factura_id) {
        this.factura_id = factura_id;
    }

    public BigInteger getReferencia() {
        return referencia;
    }

    public void setReferencia(BigInteger referencia) {
        this.referencia = referencia;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public String getNumero_autorizacion() {
        return numero_autorizacion;
    }

    public void setNumero_autorizacion(String numero_autorizacion) {
        this.numero_autorizacion = numero_autorizacion;
    }

    public String getFecha_autorizacion() {
        return fecha_autorizacion;
    }

    public void setFecha_autorizacion(String fecha_autorizacion) {
        this.fecha_autorizacion = fecha_autorizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaData{");
        sb.append("mensaje=").append(mensaje);
        sb.append(", clave_acceso=").append(clave_acceso);
        sb.append(", factura_id=").append(factura_id);
        sb.append(", referencia=").append(referencia);
        sb.append(", tramite=").append(tramite);
        sb.append(", numero_autorizacion=").append(numero_autorizacion);
        sb.append(", fecha_autorizacion=").append(fecha_autorizacion);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }

}
