/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.models;

import java.util.List;

/**
 *
 * @author ORIGAMI
 */
public class Indexacion {

    public String _id;
    private String descripcion;
    private String fecha;
    private Boolean estado;
    private UsuarioDocs usuario;
    private UsuarioDocs usuarioEdita;
    private String descripcionArchivo;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private List<IndexacionCampo> campos;

    public Indexacion() {
    }

    public Indexacion(String _id, String descripcion) {
        this._id = _id;
        this.descripcion = descripcion;
    }

    public Indexacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public UsuarioDocs getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDocs usuario) {
        this.usuario = usuario;
    }

    public List<IndexacionCampo> getCampos() {
        return campos;
    }

    public void setCampos(List<IndexacionCampo> campos) {
        this.campos = campos;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public UsuarioDocs getUsuarioEdita() {
        return usuarioEdita;
    }

    public void setUsuarioEdita(UsuarioDocs usuarioEdita) {
        this.usuarioEdita = usuarioEdita;
    }

}
