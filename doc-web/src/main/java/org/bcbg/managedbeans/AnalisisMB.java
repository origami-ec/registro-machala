/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.entities.Departamento;
import org.bcbg.entities.Rol;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.User;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class AnalisisMB implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;

    private List<User> usuarios;
    private User usuario;
    private List<Departamento> departamentos;
    private Departamento departamento;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        departamentos = appServices.getListDepartamentos();
    }

    public void cargarUsuarioDepartamento() {
//        usuarios = appServices.getUsuariosXDepts(new RolUsuario(new User(Boolean.TRUE), new Rol(new Departamento(departamento.getId())), Boolean.TRUE));

    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

}
