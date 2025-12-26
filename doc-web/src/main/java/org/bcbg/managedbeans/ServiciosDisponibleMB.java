/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.entities.ServicioDisponible;
import org.bcbg.util.JsfUti;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class ServiciosDisponibleMB implements Serializable {

    @Inject
    private BcbgService service;

    private ServicioDisponible servicioDisponible;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        servicioDisponible = (ServicioDisponible) service.methodGET(SisVars.ws + "servicioDisponible/find", ServicioDisponible.class);
    }

    public void guardar() {
        ServicioDisponible rest = (ServicioDisponible) service.methodPOST(servicioDisponible, SisVars.ws + "servicioDisponible/save", ServicioDisponible.class);
        if (rest != null) {
            JsfUti.messageInfo(null, "Datos actualizados", "");
        } else {
            JsfUti.messageError(null, "Error al actualizarlos datos", "");
        }
    }

    public ServicioDisponible getServicioDisponible() {
        return servicioDisponible;
    }

    public void setServicioDisponible(ServicioDisponible servicioDisponible) {
        this.servicioDisponible = servicioDisponible;
    }

}
