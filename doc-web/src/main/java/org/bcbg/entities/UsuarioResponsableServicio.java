/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

/**
 *
 * @author ORIGAMI
 */
public class UsuarioResponsableServicio {

    private Long id;
    private Boolean estado;
    private UsuarioResponsable usuarioResponsable;
    private ServiciosDepartamento servicio;

    public UsuarioResponsableServicio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public UsuarioResponsable getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(UsuarioResponsable usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public ServiciosDepartamento getServicio() {
        return servicio;
    }

    public void setServicio(ServiciosDepartamento servicio) {
        this.servicio = servicio;
    }

}
