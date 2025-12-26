/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import java.util.Date;

/**
 *
 * @author Ricardo
 */
public class TareasAsignadas {

    private Long id;
    private UsuarioTareas usuarioTareas;
    private Date fecha;
    private Long cantidad;
    private Long peso;
    private String fechaString;

    public TareasAsignadas() {
    }

    public TareasAsignadas(UsuarioTareas usuarioTareas, String fechaString) {
        this.usuarioTareas = usuarioTareas;
        this.fechaString = fechaString;
    }

    public TareasAsignadas(UsuarioTareas usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
    }

    public TareasAsignadas(Long id) {
        this.id = id;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioTareas getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(UsuarioTareas usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getPeso() {
        return peso;
    }

    public void setPeso(Long peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "TareasAsignadas{" + "id=" + id + ", usuarioTareas=" + usuarioTareas + ", fecha=" + fecha + ", cantidad=" + cantidad + ", peso=" + peso + '}';
    }
}
