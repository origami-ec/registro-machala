/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.models;

import java.io.Serializable;
import java.util.Date;
//import java.util.List;

/**
 *
 * @author Origami
 */
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String descripcion;
    private String usuario;
    private Date fecha;
    private String estado = "A";
    private String codigo;
    //private List<TipoDocumentoDetalle> tipoDocumentoDetalleList;

    public TipoDocumento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

   /* public List<TipoDocumentoDetalle> getTipoDocumentoDetalleList() {
        return tipoDocumentoDetalleList;
    }

    public void setTipoDocumentoDetalleList(List<TipoDocumentoDetalle> tipoDocumentoDetalleList) {
        this.tipoDocumentoDetalleList = tipoDocumentoDetalleList;
    }*/

    @Override
    public String toString() {
        return "TipoDocumento{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", usuario=" + usuario + ", fecha=" + fecha + ", estado=" + estado + ", codigo=" + codigo + '}';
    }

}
