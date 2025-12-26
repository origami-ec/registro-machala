/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

import java.util.Date;

/**
 *
 * @author ricar
 */
public class PersonaRH {

    private Long id;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String genero;
    private String direccion;
    private String telefono;
    private String celular;
    private String cedulaMilitar;
    private String correo;
    private String titulo;
    private Date fechaEliminado;
    private String detalleNombre;
    private Boolean eliminado;

    public PersonaRH() {
    }

    public PersonaRH(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCedulaMilitar() {
        return cedulaMilitar;
    }

    public void setCedulaMilitar(String cedulaMilitar) {
        this.cedulaMilitar = cedulaMilitar;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaEliminado() {
        return fechaEliminado;
    }

    public void setFechaEliminado(Date fechaEliminado) {
        this.fechaEliminado = fechaEliminado;
    }

    public String getDetalleNombre() {
        return detalleNombre;
    }

    public void setDetalleNombre(String detalleNombre) {
        this.detalleNombre = detalleNombre;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Persona{"
                + "id=" + id
                + ", cedula='" + cedula + '\''
                + ", nombres='" + nombres + '\''
                + ", apellidos='" + apellidos + '\''
                + ", genero='" + genero + '\''
                + ", direccion='" + direccion + '\''
                + ", telefono='" + telefono + '\''
                + ", celular='" + celular + '\''
                + ", cedulaMilitar='" + cedulaMilitar + '\''
                + ", correo='" + correo + '\''
                + ", titulo='" + titulo + '\''
                + ", fechaEliminado=" + fechaEliminado
                + ", detalleNombre='" + detalleNombre + '\''
                + ", eliminado=" + eliminado
                + '}';
    }
}
