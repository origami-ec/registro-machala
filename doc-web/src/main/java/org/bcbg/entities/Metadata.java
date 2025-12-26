/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

import org.bcbg.documental.models.UsuarioDocs;

/**
 *
 * @author ORIGAMI
 */
public class Metadata {

    private String descripcion;
    private String tipo;
    private String fecha;
    private UsuarioDocs usuario;

    public Metadata() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

}
