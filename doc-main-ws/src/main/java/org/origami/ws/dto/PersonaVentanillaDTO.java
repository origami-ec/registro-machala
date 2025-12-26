package org.origami.ws.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PersonaVentanillaDTO {

    private Long id;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String direccion;
    private String telefono;
    private String celular;
    private String correoSecundario;
    private String estadoCivil;
    private String correoSecundarioCodificado;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date fechaNacimiento;
    private String primerNombreApellido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCorreoSecundario() {
        return correoSecundario;
    }

    public void setCorreoSecundario(String correoSecundario) {
        this.correoSecundario = correoSecundario;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCorreoSecundarioCodificado() {
        return correoSecundarioCodificado;
    }

    public void setCorreoSecundarioCodificado(String correoSecundarioCodificado) {
        this.correoSecundarioCodificado = correoSecundarioCodificado;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPrimerNombreApellido() {
        return primerNombreApellido;
    }

    public void setPrimerNombreApellido(String primerNombreApellido) {
        this.primerNombreApellido = primerNombreApellido;
    }
}
