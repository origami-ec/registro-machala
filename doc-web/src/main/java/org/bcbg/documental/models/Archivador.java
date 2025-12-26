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
public class Archivador implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private String descripcion;
    private Long padre;
    private String estado;
    private Date fechaIngreso;
    private Integer orden = 0;
    private String comentario;
    private String folderName;

    public Archivador() {
    }

    public Archivador(String descripcion, String estado, Date fechaIngreso, Integer orden) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.orden = orden;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getPadre() {
        return padre;
    }

    public void setPadre(Long padre) {
        this.padre = padre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        return "Archivador{" + "id=" + id
                + ", descripcion=" + descripcion
                + ", padre=" + padre
                + ", estado=" + estado
                + ", fechaIngreso=" + fechaIngreso
                + ", orden=" + orden
                + ", comentario=" + comentario
                + ", folderName=" + folderName + '}';
    }
    
}
