/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.ebilling.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author eduar
 */
public class ClienteEgob implements Serializable {

    private BigInteger id;
    private String cedula;
    private String apellido;
    private String nombre;
    private String direccion;
    private String telefono;
    private String celular;
    private String fechaNacimiento;
    private Integer discapacitado = 0;
    private Integer entidadReligiosaExonerada = 0;
    private BigDecimal porcentajeExoneracion = new BigDecimal("0.0");
    private String estadoCivil;
    private String email;
    private Integer tipoPersoneriaJuridica = 1;
    private Integer nacionalidad = 1;
    private String fechaActualizacion;
    private Integer usuarioActualizacion;
    private Integer divisionBienes = 1;
    private Integer estado = 1;
    private Integer cliente;
    private BigDecimal porcentajeDiscapacidad = new BigDecimal("0.0");
    private Integer status;
    private String mensaje;

    public ClienteEgob() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getDiscapacitado() {
        return discapacitado;
    }

    public void setDiscapacitado(Integer discapacitado) {
        this.discapacitado = discapacitado;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(Integer usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getEntidadReligiosaExonerada() {
        return entidadReligiosaExonerada;
    }

    public void setEntidadReligiosaExonerada(Integer entidadReligiosaExonerada) {
        this.entidadReligiosaExonerada = entidadReligiosaExonerada;
    }

    public BigDecimal getPorcentajeExoneracion() {
        return porcentajeExoneracion;
    }

    public void setPorcentajeExoneracion(BigDecimal porcentajeExoneracion) {
        this.porcentajeExoneracion = porcentajeExoneracion;
    }

    public Integer getTipoPersoneriaJuridica() {
        return tipoPersoneriaJuridica;
    }

    public void setTipoPersoneriaJuridica(Integer tipoPersoneriaJuridica) {
        this.tipoPersoneriaJuridica = tipoPersoneriaJuridica;
    }

    public Integer getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Integer nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Integer getDivisionBienes() {
        return divisionBienes;
    }

    public void setDivisionBienes(Integer divisionBienes) {
        this.divisionBienes = divisionBienes;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getPorcentajeDiscapacidad() {
        return porcentajeDiscapacidad;
    }

    public void setPorcentajeDiscapacidad(BigDecimal porcentajeDiscapacidad) {
        this.porcentajeDiscapacidad = porcentajeDiscapacidad;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClienteEgob{");
        sb.append("id=").append(id);
        sb.append(", cedula=").append(cedula);
        sb.append(", apellido=").append(apellido);
        sb.append(", nombre=").append(nombre);
        sb.append(", direccion=").append(direccion);
        sb.append(", telefono=").append(telefono);
        sb.append(", celular=").append(celular);
        sb.append(", fechaNacimiento=").append(fechaNacimiento);
        sb.append(", discapacitado=").append(discapacitado);
        sb.append(", estadoCivil=").append(estadoCivil);
        sb.append(", email=").append(email);
        sb.append(", fechaActualizacion=").append(fechaActualizacion);
        sb.append(", usuarioActualizacion=").append(usuarioActualizacion);
        sb.append(", status=").append(status);
        sb.append(", mensaje=").append(mensaje);
        sb.append('}');
        return sb.toString();
    }

}
