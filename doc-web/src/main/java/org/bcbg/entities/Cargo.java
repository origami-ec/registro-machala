/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

/**
 *
 * @author ricar
 */
public class Cargo {

    private Long id;
    private Categoria categoria;
    private String nombre;
    private String codigo;
    private Boolean eliminado;

    public Cargo() {
    }

    public Cargo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Cargo{"
                + "id=" + id
                + ", categoria=" + categoria
                + ", nombre='" + nombre + '\''
                + ", codigo='" + codigo + '\''
                + ", eliminado=" + eliminado
                + '}';
    }
}
