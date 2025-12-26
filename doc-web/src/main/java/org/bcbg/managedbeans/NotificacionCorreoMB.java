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
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.CorreoModel;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.ws.AppServices;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class NotificacionCorreoMB implements Serializable {

    @Inject
    protected UserSession us;

    @Inject
    private AppServices appServices;
    private LazyModelWS<CorreoModel> lazyCorreoRecibidos, lazyCorreoEnviados;
    private CorreoModel correoModel;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        lazyCorreoRecibidos = new LazyModelWS<>(SisVars.wsMail + "correo/findAll?destino.referenceId=" + us.getUserId() + "&sort=id,DESC", CorreoModel[].class, us.getToken());
        lazyCorreoEnviados = new LazyModelWS<>(SisVars.wsMail + "correo/findAll?remitente.referenceId=" + us.getUserId() + "&sort=id,DESC", CorreoModel[].class, us.getToken());
    }

    public void showDialog(CorreoModel c) {
        if (c.getEnviado()) {
            correoModel = c;
            JsfUti.executeJS("PF('dlgConfirmation').show()");
        } else {
            JsfUti.messageError(null, "El correo se envio.", "");
        }
    }

    public void reenviarCorreo() {
        CorreoModel correoREST = appServices.reenviarCorreo(correoModel);
        System.out.println(correoREST);
        if (correoREST != null) {
            JsfUti.messageInfo(null, "Correo Reenviado Correctamente", "");
        } else {
            JsfUti.messageError(null, "Error al reenviar el Correo", "");
        }

        JsfUti.executeJS("PF('dlgConfirmation').hide()");

    }

    public void abrirZimbra() {
        JsfUti.redirectNewTab(SisVars.urlCorreoZimbra);
    }

    public void closeDialog() {
        JsfUti.executeJS("PF('dlgConfirmation').hide()");
    }

    public LazyModelWS<CorreoModel> getLazyCorreoRecibidos() {
        return lazyCorreoRecibidos;
    }

    public void setLazyCorreoRecibidos(LazyModelWS<CorreoModel> lazyCorreoRecibidos) {
        this.lazyCorreoRecibidos = lazyCorreoRecibidos;
    }

    public LazyModelWS<CorreoModel> getLazyCorreoEnviados() {
        return lazyCorreoEnviados;
    }

    public void setLazyCorreoEnviados(LazyModelWS<CorreoModel> lazyCorreoEnviados) {
        this.lazyCorreoEnviados = lazyCorreoEnviados;
    }

}
