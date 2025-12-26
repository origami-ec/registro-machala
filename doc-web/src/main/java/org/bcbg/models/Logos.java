/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.CtlgItem;

/**
 *
 * @author Ricardo
 */
public class Logos {

    private Long id;
    private String nombre;
    private String descripcion;
    private String ruta;
    private Boolean estado;
    private String imagen64;
    private String urlImagen;
    private CtlgItem tipoCategoria;

    public Logos() {
        this.estado = Boolean.TRUE;
    }

    public Logos(Long id) {
        this.id = id;
    }

    public Logos(Boolean estado, CtlgItem tipoCategoria) {
        this.estado = estado;
        this.tipoCategoria = tipoCategoria;
    }

    public CtlgItem getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(CtlgItem tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getImagen64() {
        return imagen64;
    }

    public void setImagen64(String imagen64) {
        this.imagen64 = imagen64;
    }

    @Override
    public String toString() {
        return "Logos{" + "id=" + id + ", nombre=" + nombre + ", descripcion="
                + descripcion + ", ruta=" + ruta + ", estado=" + estado + ", imagen64=" + imagen64 + '}';
    }

}
