/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

/**
 *
 * @author Criss Intriago
 */
public class Catalogo {

    private Long id;
    private String nombre;
    private String estado;

    public Catalogo() {
    }

    public Catalogo(Long id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Catalogo{" + "id=" + id + ", nombre=" + nombre + ", estado=" + estado + '}';
    }

}
