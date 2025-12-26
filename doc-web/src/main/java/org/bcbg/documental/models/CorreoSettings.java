/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.models;

/**
 *
 * @author ORIGAMI
 */
public class CorreoSettings {

    private String id;
    private String correo;
    private String correoClave;
    private String correoHost;
    private String correoPort;

    public CorreoSettings() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoClave() {
        return correoClave;
    }

    public void setCorreoClave(String correoClave) {
        this.correoClave = correoClave;
    }

    public String getCorreoHost() {
        return correoHost;
    }

    public void setCorreoHost(String correoHost) {
        this.correoHost = correoHost;
    }

    public String getCorreoPort() {
        return correoPort;
    }

    public void setCorreoPort(String correoPort) {
        this.correoPort = correoPort;
    }
}
