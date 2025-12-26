/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.math.BigInteger; 

/**
 *
 * @author XndySxnchez
 */
public class AclLogin implements Serializable {

    private Long id;
    private String app;
    private String ipUserSession;
    private String macClient;
    private String osClient;
    private String userSessionName;
    private String nombres;
    private BigInteger userSessionId;
    private String evento;
    private String fechaDoLogin;
    private String fechaDoLogout;

    public AclLogin() {
    }

    public AclLogin(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpUserSession() {
        return ipUserSession;
    }

    public void setIpUserSession(String ipUserSession) {
        this.ipUserSession = ipUserSession;
    }

    public String getMacClient() {
        return macClient;
    }

    public void setMacClient(String macClient) {
        this.macClient = macClient;
    }

    public String getUserSessionName() {
        return userSessionName;
    }

    public void setUserSessionName(String userSessionName) {
        this.userSessionName = userSessionName;
    }

    public BigInteger getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(BigInteger userSessionId) {
        this.userSessionId = userSessionId;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getFechaDoLogin() {
        return fechaDoLogin;
    }

    public void setFechaDoLogin(String fechaDoLogin) {
        this.fechaDoLogin = fechaDoLogin;
    }

    public String getFechaDoLogout() {
        return fechaDoLogout;
    }

    public void setFechaDoLogout(String fechaDoLogout) {
        this.fechaDoLogout = fechaDoLogout;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getOsClient() {
        return osClient;
    }

    public void setOsClient(String osClient) {
        this.osClient = osClient;
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
        if (!(object instanceof AclLogin)) {
            return false;
        }
        AclLogin other = (AclLogin) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.AclLogin[ id=" + id + " ]";
    }
}
