/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

import java.io.Serializable;

/**
 *
 * @author gutya
 */
public class PubPersona implements Serializable{

    private Long id;
    private String cedRuc;
    private String nombres;
    private String apellidos;
    private String razonSocial;
    private String tituloProf;
    private String tipoDocumento;
    private String fechaNacimiento;
    private String direccion;
    private String telefono1;
    private String correo1;
    private String nombreConyuge;
    private String apellidoConyuge;
    private String estadoCivil;
    private String condicionCiudadano;
    private String fechaDefuncion;
    private Long fechaNacimientoLong;
    private Long fechaExpedicionLong;

    public PubPersona() {
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTituloProf() {
        return tituloProf;
    }

    public void setTituloProf(String tituloProf) {
        this.tituloProf = tituloProf;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getFechaNacimientoLong() {
        return fechaNacimientoLong;
    }

    public void setFechaNacimientoLong(Long fechaNacimientoLong) {
        this.fechaNacimientoLong = fechaNacimientoLong;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getNombreConyuge() {
        return nombreConyuge;
    }

    public void setNombreConyuge(String nombreConyuge) {
        this.nombreConyuge = nombreConyuge;
    }

    public String getApellidoConyuge() {
        return apellidoConyuge;
    }

    public void setApellidoConyuge(String apellidoConyuge) {
        this.apellidoConyuge = apellidoConyuge;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public String getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public Long getFechaExpedicionLong() {
        return fechaExpedicionLong;
    }

    public void setFechaExpedicionLong(Long fechaExpedicionLong) {
        this.fechaExpedicionLong = fechaExpedicionLong;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PubPersona{");
        sb.append("id=").append(id);
        sb.append(", cedRuc=").append(cedRuc);
        sb.append(", nombres=").append(nombres);
        sb.append(", apellidos=").append(apellidos);
        sb.append(", tipoDocumento=").append(tipoDocumento);
        sb.append(", fechaNacimiento=").append(fechaNacimiento);
        sb.append(", direccion=").append(direccion);
        sb.append(", telefono1=").append(telefono1);
        sb.append(", correo1=").append(correo1);
        sb.append(", nombreConyuge=").append(nombreConyuge);
        sb.append(", apellidoConyuge=").append(apellidoConyuge);
        sb.append(", estadoCivil=").append(estadoCivil);
        sb.append(", condicionCiudadano=").append(condicionCiudadano);
        sb.append(", fechaDefuncion=").append(fechaDefuncion);
        sb.append(", fechaNacimientoLong=").append(fechaNacimientoLong);
        sb.append(", fechaExpedicionLong=").append(fechaExpedicionLong);
        sb.append('}');
        return sb.toString();
    }

}
