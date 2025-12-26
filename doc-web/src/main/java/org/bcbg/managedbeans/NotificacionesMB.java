/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.entities.Notificaciones;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.config.SisVars;
import org.bcbg.session.UserSession;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class NotificacionesMB implements Serializable {

    @Inject
    private UserSession us;
    @Inject
    private AppServices appServices;

    private LazyModelWS<Notificaciones> notificaciones;
    private Notificaciones notificacion;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        notificaciones = new LazyModelWS<>(SisVars.ws + "notificaciones/find?usuario.user=" + us.getName_user() + "sort=id,DESC", Notificaciones[].class, us.getToken());
    }

    public LazyModelWS<Notificaciones> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(LazyModelWS<Notificaciones> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public Notificaciones getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificaciones notificacion) {
        this.notificacion = notificacion;
    }

}
