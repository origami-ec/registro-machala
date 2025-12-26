/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.Cajero;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Cajeros implements Serializable {

    private static final Logger LOG = Logger.getLogger(Cajeros.class.getName());

    @EJB
    protected Entitymanager em;

    protected List<RenCajero> cajeros;
    protected List<Cajero> puntosEmision;
    protected RenCajero cajero;
    protected Cajero caja;
    protected Map map;

    @PostConstruct
    protected void iniView() {
        try {
            cajero = new RenCajero();
            puntosEmision = em.findAll(Querys.getPuntosEmision);
            cajeros = em.findAll(Querys.getRenCajeros);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarUsuario(String url) {
        JsfUti.openDialogFrame(url);
    }

    public void showDlgCaja() {
        try {
            cajero = new RenCajero();
            JsfUti.update("formCajero");
            JsfUti.executeJS("PF('usuarioCaja').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void procesar(SelectEvent event) {
        if (event.getObject() != null) {
            cajero.setUsuario((AclUser) event.getObject());
        }
    }

    public void guardarCaja() {
        try {
            if (cajero.getId() == null) {
                cajero.setCodigoCaja(cajero.getCajero().getPuntoEmision());
                em.persist(cajero);
                cajeros = em.findAll(Querys.getRenCajeros);
                JsfUti.update("mainForm:accMantenimiento:dtcajeros");
                JsfUti.executeJS("PF('usuarioCaja').hide()");
            } else {
                cajero.setCodigoCaja(cajero.getCajero().getPuntoEmision());
                cajero = (RenCajero) em.persist(cajero);
                JsfUti.update("mainForm:accMantenimiento:dtcajeros");
                JsfUti.executeJS("PF('usuarioCaja').hide()");
            }
            cajero = new RenCajero();
            JsfUti.messageInfo(null, "", "Datos almacenados correctamente.");
        } catch (Exception e) {
            JsfUti.messageError(null, "", "Error al guadar los datos.");
            System.out.println(e);
        }
    }

    public void showDlgEditCaja(RenCajero temp) {
        try {
            cajero = temp;
            JsfUti.update("formCajero");
            JsfUti.executeJS("PF('usuarioCaja').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<RenCajero> getCajeros() {
        return cajeros;
    }

    public void setCajeros(List<RenCajero> cajeros) {
        this.cajeros = cajeros;
    }

    public RenCajero getCajero() {
        return cajero;
    }

    public void setCajero(RenCajero cajero) {
        this.cajero = cajero;
    }

    public Cajero getCaja() {
        return caja;
    }

    public void setCaja(Cajero caja) {
        this.caja = caja;
    }

    public List<Cajero> getPuntosEmision() {
        return puntosEmision;
    }

    public void setPuntosEmision(List<Cajero> puntosEmision) {
        this.puntosEmision = puntosEmision;
    }

}
