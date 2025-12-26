/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;

/**
 *
 * @author jesus
 */
public class RolUsuario implements Serializable {

    private Long id;
    private Long usuario;
    private Rol rol;

    public RolUsuario(Long usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    public RolUsuario(Long id, Long usuario, Rol rol) {
        this.id = id;
        this.usuario = usuario;
        this.rol = rol;
    }

    public RolUsuario(Rol rol) {
        this.rol = rol;
    }

    public RolUsuario() {
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}