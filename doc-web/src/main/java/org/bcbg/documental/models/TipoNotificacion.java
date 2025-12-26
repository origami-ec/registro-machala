/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.models;

import java.io.Serializable;

/**
 *
 * @author Origami
 */
public class TipoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean estado = true;
    private String abreviatura;
    private String codigoSecuencia;

    public TipoNotificacion() {
    }

    public TipoNotificacion(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getCodigoSecuencia() {
        return codigoSecuencia;
    }

    public void setCodigoSecuencia(String codigoSecuencia) {
        this.codigoSecuencia = codigoSecuencia;
    }

    @Override
    public String toString() {
        return "TipoNotificacion{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", estado=" + estado + ", abreviatura=" + abreviatura + ", codigoSecuencia=" + codigoSecuencia + '}';
    }

}
