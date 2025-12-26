package org.origami.ws.entities.origami;

import org.origami.ws.models.Data;
import org.origami.ws.util.Utility;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public", name = "persona")
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private Boolean esPersona;
    private String direccion;
    private Date fechaNacimiento;
    private Date fechaExpedicion;
    private String estado;
    private String userCre;
    private Date fechaCre;
    private String userMod;
    private Date fechaMod;
    private String nombreComercial;
    private String razonSocial;
    private String telefono;
    private String celular;
    private String correo;
    private String correoSecundario;
    private String tipoIdentificacion;
    private String tituloProf;
    private Long representanteLegal;
    private Boolean sexo;
    @Transient
    private String nombreCompleto;
    @Transient
    private String nombrerazonsocial;
    @Transient
    private String apellidonombrecomercial;
    @Transient
    private String correoCodificado;
    @Transient
    private String correoSecundarioCodificado;
    @Transient
    private Data usuario;

    public Persona() {
    }

    public Persona(String identificacion) {
        this.identificacion = identificacion;
        this.estado = "A";
    }

    public Persona(Long id, String identificacion, String nombres, String apellidos, Boolean esPersona, String direccion, Date fechaNacimiento,
                   String estado, String userCre, Date fechaCre, String userMod, Date fechaMod, String nombreComercial, String razonSocial,
                   String telefono, String celular, String correo, String tipoIdentificacion, String tituloProf, Long representanteLegal, Boolean sexo) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.esPersona = esPersona;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado;
        this.userCre = userCre;
        this.fechaCre = fechaCre;
        this.userMod = userMod;
        this.fechaMod = fechaMod;
        this.nombreComercial = nombreComercial;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.tipoIdentificacion = tipoIdentificacion;
        this.tituloProf = tituloProf;
        this.representanteLegal = representanteLegal;
        this.sexo = sexo;
    }

    public String getCorreoSecundario() {
        return correoSecundario;
    }

    public void setCorreoSecundario(String correoSecundario) {
        this.correoSecundario = correoSecundario;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public String getNombreCompleto() {
        if (esPersona) {
            nombreCompleto = Utility.isEmpty(apellidos) + " " + Utility.isEmpty(nombres);
            return nombreCompleto;
        } else {
            return Utility.isEmpty(razonSocial);
        }
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }


    public Persona(Long id) {
        this.id = id;
    }

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

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public Date getFechaCre() {
        return fechaCre;
    }

    public void setFechaCre(Date fechaCre) {
        this.fechaCre = fechaCre;
    }

    public String getUserMod() {
        return userMod;
    }

    public void setUserMod(String userMod) {
        this.userMod = userMod;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombrerazonsocial() {
        return nombrerazonsocial;
    }

    public void setNombrerazonsocial(String nombrerazonsocial) {
        this.nombrerazonsocial = nombrerazonsocial;
    }

    public String getApellidonombrecomercial() {
        return apellidonombrecomercial;
    }

    public void setApellidonombrecomercial(String apellidonombrecomercial) {
        this.apellidonombrecomercial = apellidonombrecomercial;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTituloProf() {
        return tituloProf;
    }

    public void setTituloProf(String tituloProf) {
        this.tituloProf = tituloProf;
    }

    public Long getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Long representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public void setCorreoCodificado(String correoCodificado) {
        this.correoCodificado = correoCodificado;
    }

    public String getCorreoCodificado() {
        if (correo != null && !correo.isEmpty()) {
            return correo.replaceAll("(?<=.{3}).(?=.*@)", "*");
        }
        return correoCodificado;
    }

    public String getCorreoSecundarioCodificado() {
        if (correoSecundario != null && !correoSecundario.isEmpty()) {
            return correoSecundario.replaceAll("(?<=.{3}).(?=.*@)", "*");
        }
        return correoSecundarioCodificado;
    }

    public void setCorreoSecundarioCodificado(String correoSecundarioCodificado) {
        this.correoSecundarioCodificado = correoSecundarioCodificado;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Data getUsuario() {
        return usuario;
    }

    public void setUsuario(Data usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", identificacion='" + identificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", esPersona=" + esPersona +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaExpedicion=" + fechaExpedicion +
                ", estado='" + estado + '\'' +
                ", userCre='" + userCre + '\'' +
                ", fechaCre=" + fechaCre +
                ", userMod='" + userMod + '\'' +
                ", fechaMod=" + fechaMod +
                ", nombreComercial='" + nombreComercial + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", telefono='" + telefono + '\'' +
                ", celular='" + celular + '\'' +
                ", correo='" + correo + '\'' +
                ", correoSecundario='" + correoSecundario + '\'' +
                ", tipoIdentificacion='" + tipoIdentificacion + '\'' +
                ", tituloProf='" + tituloProf + '\'' +
                ", representanteLegal=" + representanteLegal +
                ", sexo=" + sexo +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", nombrerazonsocial='" + nombrerazonsocial + '\'' +
                ", apellidonombrecomercial='" + apellidonombrecomercial + '\'' +
                ", correoCodificado='" + correoCodificado + '\'' +
                ", correoSecundarioCodificado='" + correoSecundarioCodificado + '\'' +
                '}';
    }
}
