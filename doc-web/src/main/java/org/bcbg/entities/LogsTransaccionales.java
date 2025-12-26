/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ORIGAMI1
 */
public class LogsTransaccionales implements Serializable {

    private Long id;
    private String ip;
    private String mac;
    private String descripcion;
    private String accion;
    private String app;
    private String response;
    private String request;
    private String usuario;
    private String fecha;
    private String osClient;
    private String usuarioApp;
    private Date fechaCreacion;

    public LogsTransaccionales() {

    }

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return String return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return String return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return String return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * @return String return the app
     */
    public String getApp() {
        return app;
    }

    /**
     * @param app the app to set
     */
    public void setApp(String app) {
        this.app = app;
    }

    /**
     * @return String return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return String return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return Date return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getOsClient() {
        return osClient;
    }

    public void setOsClient(String osClient) {
        this.osClient = osClient;
    }

    public String getUsuarioApp() {
        return usuarioApp;
    }

    public void setUsuarioApp(String usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
