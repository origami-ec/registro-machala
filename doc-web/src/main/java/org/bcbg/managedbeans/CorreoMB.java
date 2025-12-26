/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.documental.models.Notificacion;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.CorreoModel;
import org.bcbg.util.JsfUti;
import org.bcbg.config.SisVars;
import org.bcbg.session.UserSession;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "correoMB")
@ViewScoped
public class CorreoMB implements Serializable {

    @Inject
    protected UserSession us;

    @Inject
    private AppServices appServices;
    private LazyModelWS<CorreoModel> lazyCorreo;
    private CorreoModel correoModel;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        lazyCorreo = new LazyModelWS<>(SisVars.wsMail + "correo/findAll?sort=id,DESC", CorreoModel[].class, us.getToken());
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

    public void closeDialog() {
        JsfUti.executeJS("PF('dlgConfirmation').hide()");
    }

    public LazyModelWS<CorreoModel> getLazyCorreo() {
        return lazyCorreo;
    }

    public void setLazyCorreo(LazyModelWS<CorreoModel> lazyCorreo) {
        this.lazyCorreo = lazyCorreo;
    }

}
