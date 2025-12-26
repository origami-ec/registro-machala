package org.origami.ws.dto;

import org.origami.ws.entities.origami.UsuarioResponsableServicio;
import org.origami.ws.entities.security.Usuario;

import java.util.List;


public class UsuarioResponsableDto {

    private Long id;

    private String observacion;
    private Boolean responsable;
    private Boolean director;
    private Boolean jefe;
    private Boolean estado;
    private Usuario usuario;
    private DepartamentoDTO departamento;
    private List<UsuarioResponsableServicio> servicios;

    public UsuarioResponsableDto() {
    }

    public UsuarioResponsableDto(Long id, Usuario usuario, String observacion, Boolean responsable, Boolean director, Boolean jefe, Boolean estado, DepartamentoDTO departamento, List<UsuarioResponsableServicio> servicios) {
        this.id = id;
        this.usuario = usuario;
        this.observacion = observacion;
        this.responsable = responsable;
        this.director = director;
        this.jefe = jefe;
        this.estado = estado;
        this.departamento = departamento;
        this.servicios = servicios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
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

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    public List<UsuarioResponsableServicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<UsuarioResponsableServicio> servicios) {
        this.servicios = servicios;
    }

    @Override
    public String toString() {
        return "UsuarioResponsable{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", observacion='" + observacion + '\'' +
                ", responsable=" + responsable +
                ", director=" + director +
                ", jefe=" + jefe +
                ", estado=" + estado +
                '}';
    }

}
