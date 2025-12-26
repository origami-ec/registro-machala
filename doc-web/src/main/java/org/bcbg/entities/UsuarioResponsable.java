/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author ORIGAMI
 */
public class UsuarioResponsable {

    private Long id;
    private User usuario;
    private String observacion;
    private Boolean responsable;
    private Boolean director;
    private Boolean jefe;
    private Boolean estado;
    private Departamento departamento;
    private Boolean disponible;
    private String estadoDisponible;
    private List<UsuarioResponsableServicio> servicios;

    public UsuarioResponsable() {
    }

    public UsuarioResponsable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(Boolean responsable) {
        this.responsable = responsable;
    }

    public Boolean getDirector() {
        return director;
    }

    public void setDirector(Boolean director) {
        this.director = director;
    }

    public Boolean getJefe() {
        return jefe;
    }

    public void setJefe(Boolean jefe) {
        this.jefe = jefe;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<UsuarioResponsableServicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<UsuarioResponsableServicio> servicios) {
        this.servicios = servicios;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioResponsable other = (UsuarioResponsable) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "UsuarioResponsable{"
                + "id=" + id
                + ", usuario=" + usuario
                + ", observacion='" + observacion + '\''
                + ", responsable=" + responsable
                + ", director=" + director
                + ", jefe=" + jefe
                + ", estado=" + estado
                + '}';
    }
}
