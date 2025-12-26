/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;

/**
 *
 * @author ORIGAMI1
 */
public class EquipoAutorizado implements Serializable {

    private Integer id;
    private String nombreEquipo;
    private String ip;
    private String mac;
    private User usuario;
    private Boolean rango;
    private Boolean excluir;

    public EquipoAutorizado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Boolean getRango() {
        return rango;
    }

    public void setRango(Boolean rango) {
        this.rango = rango;
    }

    public Boolean getExcluir() {
        return excluir;
    }

    public void setExcluir(Boolean excluir) {
        this.excluir = excluir;
    }

}
