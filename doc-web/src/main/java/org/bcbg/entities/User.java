/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import jdk.jfr.Description;
import org.bcbg.util.Variables;

/**
 *
 * @author Anyelo
 */
public class User implements Serializable {

    @Description("Clave primaria del usuario")
    private Long id;
    @Description("Nombre de usuario en el sistema")
    private String usuarioNombre;
    @Description(Variables.omitirCampo)
    private String password;
    @Description(Variables.omitirCampo)
    private String clave;
    @Description("Datos del usuario")
    private String nombreUsuario;
    @Description(Variables.omitirCampo)
    private Long usuarioRRHH;
    @Description(Variables.omitirCampo)
    private Boolean eliminado;
    @Description(Variables.omitirCampo)
    private RecursoHumano recursoHumano;
    @Description(Variables.omitirCampo)
    private String cargo;
    @Description(Variables.omitirCampo)
    private Boolean disponible;
    @Description(Variables.omitirCampo)
    private String estadoDisponible;
    @Description(Variables.omitirCampo)
    private Long departamentoId;
    @Description(Variables.omitirCampo)
    private String departamento;

    public User() {
        this.eliminado = Boolean.FALSE;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
        this.eliminado = Boolean.FALSE;
    }

    public User(String usuarioNombre, String clave) {
        this.usuarioNombre = usuarioNombre;
        this.clave = clave;
        this.eliminado = Boolean.FALSE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Long getUsuarioRRHH() {
        return usuarioRRHH;
    }

    public void setUsuarioRRHH(Long usuarioRRHH) {
        this.usuarioRRHH = usuarioRRHH;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public RecursoHumano getRecursoHumano() {
        return recursoHumano;
    }

    public void setRecursoHumano(RecursoHumano recursoHumano) {
        this.recursoHumano = recursoHumano;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getEstadoDisponible() {
        return estadoDisponible;
    }

    public void setEstadoDisponible(String estadoDisponible) {
        this.estadoDisponible = estadoDisponible;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", usuarioNombre=" + usuarioNombre + ", password=" + password + ", clave=" + clave + ", nombreUsuario=" + nombreUsuario + ", usuarioRRHH=" + usuarioRRHH + ", eliminado=" + eliminado + ", recursoHumano=" + recursoHumano + '}';
    }

}
