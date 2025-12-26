package org.origami.ws.dto;

import javax.persistence.Transient;
import java.util.Date;

public class PersonaDinardapDTO {

    private String identificacion;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String direccion;
    private String correo;
    private String telefono;
    private Boolean estado;
    private String nombreConyuge;
    private String apellidoConyuge;
    private String estadoCivil;
    private String condicionCiudadano;
    private String fechaExpiracion;
    private Date fechaExpedicion;
    private Date fechaNacimiento;
    //CAMPO PARA LA EXPEDICION DE LA CEDULA E INICIO DE ACTIVIDADES
    private Long fechaExpedicionLong;
    private Integer edad;
    private Long fechaNacimientoLong;

    public PersonaDinardapDTO() {
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Long getFechaExpedicionLong() {
        return fechaExpedicionLong;
    }

    public void setFechaExpedicionLong(Long fechaExpedicionLong) {
        this.fechaExpedicionLong = fechaExpedicionLong;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Long getFechaNacimientoLong() {
        return fechaNacimientoLong;
    }

    public void setFechaNacimientoLong(Long fechaNacimientoLong) {
        this.fechaNacimientoLong = fechaNacimientoLong;
    }

    @Override
    public String toString() {
        return "PersonaDinardapDTO{" +
                "identificacion='" + identificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", direccion='" + direccion + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado=" + estado +
                ", nombreConyuge='" + nombreConyuge + '\'' +
                ", apellidoConyuge='" + apellidoConyuge + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", condicionCiudadano='" + condicionCiudadano + '\'' +
                ", fechaExpiracion='" + fechaExpiracion + '\'' +
                ", fechaExpedicionLong=" + fechaExpedicionLong +
                ", edad=" + edad +
                ", fechaNacimientoLong=" + fechaNacimientoLong +
                '}';
    }
}
