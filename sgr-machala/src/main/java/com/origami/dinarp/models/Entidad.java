/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.dinarp.models;

import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class Entidad implements Serializable {

    protected String nombre;
    protected Filas filas;

    public Entidad() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Filas getFilas() {
        return filas;
    }

    public void setFilas(Filas filas) {
        this.filas = filas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Entidad{");
        sb.append("nombre=").append(nombre);
        sb.append(", filas=").append(filas);
        sb.append('}');
        return sb.toString();
    }

}
