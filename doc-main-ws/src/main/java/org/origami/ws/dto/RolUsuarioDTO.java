package org.origami.ws.dto;

public class RolUsuarioDTO {

    private Long id;
    private Long usuario;
    private RolDTO rol;
    private Boolean responsable;
    private Boolean revisor;

    public RolUsuarioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public RolDTO getRol() {
        return rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = rol;
    }

    public Boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(Boolean responsable) {
        this.responsable = responsable;
    }

    public Boolean getRevisor() {
        return revisor;
    }

    public void setRevisor(Boolean revisor) {
        this.revisor = revisor;
    }
}
