/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Dominio implements Serializable {

    private Long id;
    private String descripcion;
    private Short tipo;

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

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

}
