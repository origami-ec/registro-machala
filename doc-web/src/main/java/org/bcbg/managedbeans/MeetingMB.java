/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class MeetingMB implements Serializable {

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;
    private String url, asunto, observacion;

    @PostConstruct
    public void init() {
        if (ss.tieneParametro("reunionURL")) {
            url = (String) ss.retornarValor("reunionURL");
            asunto = (String) ss.retornarValor("asunto");
            observacion = (String) ss.retornarValor("observacion");
        } else {
            url = SisVars.JITSI;
            asunto = "";
            observacion = "";
        }
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public UserSession getUs() {
        return us;
    }

    public void setUs(UserSession us) {
        this.us = us;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
