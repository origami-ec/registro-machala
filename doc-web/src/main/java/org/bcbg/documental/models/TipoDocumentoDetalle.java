/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Origami
 */
public class TipoDocumentoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoDocumento tipoDocumento;
    private Integer tipoDetalle = 1;
    private TipoDocumento tipoDocumentoHijo;
    private String nombre;
    private TipoDato tipoDato;
    private String formato;
    private String usuario;
    private Date fecha;
    private String estado;
    private Integer orden;
    private Boolean opcional;

    public TipoDocumentoDetalle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(Integer tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public TipoDocumento getTipoDocumentoHijo() {
        return tipoDocumentoHijo;
    }

    public void setTipoDocumentoHijo(TipoDocumento tipoDocumentoHijo) {
        this.tipoDocumentoHijo = tipoDocumentoHijo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    @Override
    public String toString() {
        return "TipoDocumentoDetalle{" + "id=" + id + ", tipoDocumento=" + tipoDocumento + ", tipoDetalle=" + tipoDetalle + ", tipoDocumentoHijo=" + tipoDocumentoHijo + ", nombre=" + nombre + ", tipoDato=" + tipoDato + ", formato=" + formato + ", usuario=" + usuario + ", fecha=" + fecha + ", estado=" + estado + ", orden=" + orden + ", opcional=" + opcional + '}';
    }

}
