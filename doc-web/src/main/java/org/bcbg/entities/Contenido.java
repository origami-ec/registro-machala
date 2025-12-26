/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;

/**
 *
 * @author Ricardo
 */
public class Contenido implements Serializable {

    private Long id;
    private String nombre;
    private String descripcion;
    private String urlImagen;
    private Boolean estado;
    private Integer orden;
    private String urlLink;
    private String color;
    private String tipoFormato;
    private String archivo;
    private byte[] byteArchivo;

    public Contenido() {
        estado = Boolean.TRUE;
    }

    public Contenido(Long id) {
        this.id = id;
    }

    public byte[] getByteArchivo() {
        return byteArchivo;
    }

    public void setByteArchivo(byte[] byteArchivo) {
        this.byteArchivo = byteArchivo;
    }

    public String getTipoFormato() {
        return tipoFormato;
    }

    public void setTipoFormato(String tipoFormato) {
        this.tipoFormato = tipoFormato;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        if (!(object instanceof Contenido)) {
            return false;
        }
        Contenido other = (Contenido) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Contenido{" + "id=" + id + ", nombre="
                + nombre + ", descripcion=" + descripcion + ", urlImagen="
                + urlImagen + ", estado=" + estado + ", orden=" + orden + ", urlLink="
                + urlLink + ", color=" + color + ", tipoFormato=" + tipoFormato + ", archivo="
                + archivo + '}';
    }

}
