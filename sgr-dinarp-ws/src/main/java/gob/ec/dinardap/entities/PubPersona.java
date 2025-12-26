package gob.ec.dinardap.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Transient;

@Entity
@Table(name = "cat_ente", schema = "app")
public class PubPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;
    private String cedRuc;
    private String nombres; //RAZON SOCIAL
    private String apellidos; //NOMBRE COMERCIAL
    private String tipoDocumento;
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String correo1;
    private String correo2;
    private String nombreConyuge;
    private String apellidoConyuge;
    private String estadoCivil;
    private String condicionCiudadano;
    private String fechaDefuncion;
    @Transient
    private Long fechaNacimientoLong;
    //CAMPO PARA LA EXPEDICION DE LA CEDULA E INICIO DE ACTIVIDADES
    @Transient
    private Long fechaExpedicionLong;


    public PubPersona() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
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

    public Long getFechaNacimientoLong() {
        return fechaNacimientoLong;
    }

    public void setFechaNacimientoLong(Long fechaNacimientoLong) {
        this.fechaNacimientoLong = fechaNacimientoLong;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Long getFechaExpedicionLong() {
        return fechaExpedicionLong;
    }

    public void setFechaExpedicionLong(Long fechaExpedicionLong) {
        this.fechaExpedicionLong = fechaExpedicionLong;
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

    @Override
    public String toString() {
        return "PubPersona{"
                + "id=" + id
                + ", cedRuc='" + cedRuc + '\''
                + ", nombres='" + nombres + '\''
                + ", apellidos='" + apellidos + '\''
                + ", tipoDocumento='" + tipoDocumento + '\''
                + ", fechaNacimiento=" + fechaNacimiento
                + ", direccion='" + direccion + '\''
                + ", telefono1='" + telefono1 + '\''
                + ", telefono2='" + telefono2 + '\''
                + ", correo1='" + correo1 + '\''
                + ", correo2='" + correo2 + '\''
                + ", nombreConyuge='" + nombreConyuge + '\''
                + ", apellidoConyuge='" + apellidoConyuge + '\''
                + ", fechaNacimientoLong=" + fechaNacimientoLong
                + ", estadoCivil='" + estadoCivil + '\''
                + '}';
    }
}
