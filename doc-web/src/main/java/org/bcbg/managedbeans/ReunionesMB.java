/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.CorreoSettings;
import org.bcbg.entities.Reuniones;
import org.bcbg.entities.Valores;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.DocumentalService;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class ReunionesMB implements Serializable {

    @Inject
    private AppServices services;
    @Inject
    protected UserSession us;
    @Inject
    protected ServletSession ss;
    private Reuniones reunion;
    private List<Reuniones> reuniones;

    @PostConstruct
    public void init() {
        try {
            reunion = new Reuniones();
            reuniones = services.getReuniones();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirReunion(Reuniones v) {
        this.reunion = v;
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("reunionURL", v.getLink());
        ss.agregarParametro("asunto", v.getAsunto());
        ss.agregarParametro("observacion", v.getObservacion());
        JsfUti.redirectFacesNewTab("/admin/manage/meeting.xhtml");
    }

    public void guardarReuniones() {
        if (Utils.isEmptyString(reunion.getAsunto())) {
            JsfUti.messageError(null, "Debe ingresar un asunto a la reunion", "");
            return;
        }
        if (Utils.isEmptyString(reunion.getObservacion())) {
            JsfUti.messageError(null, "Debe ingresar una descripción a la reunion", "");
            return;
        }
        reunion.setLink(SisVars.JITSI + StringUtils.stripAccents(reunion.getAsunto()).replace(" ", "-"));
        reunion.setUsuario(us.getName_user());
        reunion.setFecha(new Date());
        Reuniones rest = services.guardarReunion(reunion);
        if (rest != null) {
            reunion = new Reuniones();
            JsfUti.messageInfo(null, Messages.correcto, "");
            reuniones = services.getReuniones();
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public void eliminarReunion(Reuniones reunion) {
        services.eliminarReunion(reunion);
        reuniones = services.getReuniones();
    }

    public void abrirJenkins() {
        JsfUti.redirectNewTab(SisVars.JENKINS);
    }

    public Reuniones getReunion() {
        return reunion;
    }

    public void setReunion(Reuniones reunion) {
        this.reunion = reunion;
    }

    public List<Reuniones> getReuniones() {
        return reuniones;
    }

    public void setReuniones(List<Reuniones> reuniones) {
        this.reuniones = reuniones;
    }

}
