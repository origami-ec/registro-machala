/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.origami.ws.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Origami
 */
public class TipoDocumentoDetalleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoDocumentoDTO tipoDocumento;
    private Integer tipoDetalle;
    private TipoDocumentoDTO tipoDocumentoHijo;
    private String nombre;
    private TipoDatoDTO tipoDato;
    private String formato;
    private String usuario;
    private Date fecha;
    private String estado;
    private Integer orden;
    private Boolean opcional;

    public TipoDocumentoDetalleDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumentoDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(Integer tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public TipoDocumentoDTO getTipoDocumentoHijo() {
        return tipoDocumentoHijo;
    }

    public void setTipoDocumentoHijo(TipoDocumentoDTO tipoDocumentoHijo) {
        this.tipoDocumentoHijo = tipoDocumentoHijo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDatoDTO getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDatoDTO tipoDato) {
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDocumentoDetalleDTO)) {
            return false;
        }
        TipoDocumentoDetalleDTO other = (TipoDocumentoDetalleDTO) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "TipoDocumentoDetalle{" + "id=" + id + ", tipoDocumento=" + tipoDocumento + ", tipoDetalle=" + tipoDetalle + ", tipoDocumentoHijo=" + tipoDocumentoHijo + ", nombre=" + nombre + ", tipoDato=" + tipoDato + ", formato=" + formato + ", usuario=" + usuario + ", fecha=" + fecha + ", estado=" + estado + ", orden=" + orden + ", opcional=" + opcional + '}';
    }

}
