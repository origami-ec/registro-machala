/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.managedbeans;

import org.bcbg.documental.models.Notificacion;
import org.bcbg.documental.models.TipoNotificacion;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.config.SisVars;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class NotificacionDocumental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession us;
    @Inject
    private BcbgService irs;
    @Inject
    protected ServletSession ss;

    protected Notificacion notificacion;
    protected TipoNotificacion tipoNotificacion;
    protected LazyModelWS<Notificacion> lazy;

    @PostConstruct
    public void initView() {
        try {
            lazy = new LazyModelWS<>(SisVars.ws + "notificacion/lazy?sort=id,DESC", Notificacion[].class, us.getToken());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void visualizarDocumento(Notificacion notificacion) {
        try {
            ss.borrarDatos();
            ss.setUrlWebService(SisVars.ws + "reportes/notificacion/" + notificacion.getId());
            JsfUti.redirectFacesNewTab("/ReporteWS");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "Error al generar el documento.", "");
        }
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public LazyModelWS<Notificacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<Notificacion> lazy) {
        this.lazy = lazy;
    }

}
