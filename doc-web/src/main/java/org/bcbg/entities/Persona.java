/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Date;
import jdk.jfr.Description;
import org.bcbg.models.Data;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;

/**
 *
 * @author Anyelo
 */
public class Persona implements Serializable {

    @Description("Clave primaria Persona")
    private Long id;
    @Description("Número de cédula de la persona")
    private String identificacion;
    @Description("Nombres de la persona")
    private String nombres;
    @Description("Apellidos de la persona")
    private String apellidos;
    @Description(Variables.omitirCampo)
    private Boolean esPersona;
    @Description("Dirección de la persona o empresa")
    private String direccion;
    @Description("Fecha de nacimiento de la persona")
    private Date fechaNacimiento;
    @Description(Variables.omitirCampo)
    private String estado;
    @Description(Variables.omitirCampo)
    private String userCre;
    @Description(Variables.omitirCampo)
    private Date fechaCre;
    @Description(Variables.omitirCampo)
    private String userMod;
    @Description(Variables.omitirCampo)
    private Date fechaMod;
    @Description(Variables.omitirCampo)
    private String nombreComercial;
    @Description(Variables.omitirCampo)
    private String razonSocial;
    @Description(Variables.omitirCampo)
    private String tituloProf;
    @Description("# Teléfono convencional")
    private String telefono;
    @Description("# Teléfono celular")
    private String celular;
    @Description("Correo electrónico")
    private String correo;
    @Description(Variables.omitirCampo)
    private Boolean excepcional;
    @Description(Variables.omitirCampo)
    private String tipoIdentificacion;
    @Description(Variables.omitirCampo)
    private String nombrerazonsocial;
    @Description(Variables.omitirCampo)
    private String apellidonombrecomercial;
    @Description(Variables.omitirCampo)
    private String nombreCompleto;
    @Description(Variables.omitirCampo)
    private Boolean sexo;
    @Description(Variables.omitirCampo)
    private String correoCodificado;
    @Description("Correo electrónico secundario")
    private String correoSecundario;
    @Description(Variables.omitirCampo)
    private Long representanteLegal;
    @Description(Variables.omitirCampo) //Campo para el envio de correo
    private Data usuario;

    public Persona() {
        this.esPersona = Boolean.TRUE;
        this.estado = "A";
    }

    public Persona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public Persona(String identificacion) {
        this.identificacion = identificacion;
        this.estado = "A";
    }

    public Persona(Long id) {
        this.id = id;
    }

    public Persona(Long id, Boolean esPersona, String estado) {
        this.id = id;
        this.esPersona = esPersona;
        this.estado = estado;
    }

    public String getCorreoSecundario() {
        return correoSecundario;
    }

    public void setCorreoSecundario(String correoSecundario) {
        this.correoSecundario = correoSecundario;
    }

    public String getCorreoCodificado() {
        return correoCodificado;
    }

    public void setCorreoCodificado(String correoCodificado) {
        this.correoCodificado = correoCodificado;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public String getNombreCompleto() {
        if (esPersona) {
            return Utils.isEmpty(apellidos) + " " + Utils.isEmpty(nombres);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombresApellidos() {
        if (esPersona) {
            return Utils.isEmpty(nombres) + " " + Utils.isEmpty(apellidos);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public String getTituloNombreCompleto() {
        if (esPersona) {
            return Utils.isEmpty(tituloProf) + " " + Utils.isEmpty(apellidos) + " " + Utils.isEmpty(nombres);
        } else {
            return Utils.isEmpty(razonSocial);
        }
    }

    public String getNombresTitulo() {
        if (esPersona) {
            return Utils.isEmpty(nombres) + " " + Utils.isEmpty(apellidos) + "\n" + Utils.isEmpty(tituloProf);
        } else {
            return Utils.isEmpty(razonSocial);
        }
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

    public Long getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Long representanteLegal) {
        this.representanteLegal = representanteLegal;
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

    public String getTituloProf() {
        return tituloProf;
    }

    public void setTituloProf(String tituloProf) {
        this.tituloProf = tituloProf;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getExcepcional() {
        return excepcional;
    }

    public void setExcepcional(Boolean excepcional) {
        this.excepcional = excepcional;
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

    public Data getUsuario() {
        return usuario;
    }

    public void setUsuario(Data usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", identificacion=" + identificacion + ", nombres=" + nombres + ", apellidos=" + apellidos + ", esPersona=" + esPersona + ", direccion=" + direccion + ", representanteLegal=" + representanteLegal + ", fechaNacimiento=" + fechaNacimiento + ", estado=" + estado + ", userCre=" + userCre + ", fechaCre=" + fechaCre + ", userMod=" + userMod + ", fechaMod=" + fechaMod + ", nombreComercial=" + nombreComercial + ", razonSocial=" + razonSocial + ", tituloProf=" + tituloProf + ", telefono=" + telefono + ", celular=" + celular + ", correo=" + correo + ", excepcional=" + excepcional + ", tipoIdentificacion=" + tipoIdentificacion + ", nombrerazonsocial=" + nombrerazonsocial + ", apellidonombrecomercial=" + apellidonombrecomercial + ", nombreCompleto=" + nombreCompleto + ", sexo=" + sexo + ", correoCodificado=" + correoCodificado + ", correoSecundario=" + correoSecundario + '}';
    }

}
