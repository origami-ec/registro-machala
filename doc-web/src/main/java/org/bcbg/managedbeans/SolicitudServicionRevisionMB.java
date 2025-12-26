/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.User;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.Data;
import org.bcbg.util.JsfUti;
import org.bcbg.config.SisVars;
import org.bcbg.session.UserSession;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class SolicitudServicionRevisionMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SolicitudServicionRevisionMB.class.getName());

    @Inject
    private AppServices appServices;
    @Inject
    private UserSession session;

    private SolicitudServicios solicitud;
    private LazyModelWS<SolicitudServicios> lazy;
    
    private List<Data> listData;
    private String ciUser = "";

    @PostConstruct
    public void init() {
        lazy = new LazyModelWS<>(SisVars.ws + "solicitudServicios/find?sort=id,DESC", SolicitudServicios[].class, session.getToken());
    }

    public void showDlgTitulos(SolicitudServicios ss) {
        try {
            solicitud = ss;
            
            JsfUti.update("formTitulos");
            JsfUti.executeJS("PF('dlgTitulos').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "SolicitudServicionRevisionMB", e);
        }
    }

    public void getTitulosCreditosDeuda() {
        if (ciUser.equals("")) {
            JsfUti.messageError(null, "Agregue la identificación de la persona a consultar los Títulos créditos.", "");
            return;
        }
        listData = new ArrayList<>();
        
//        listData = appServices.getEmisionesGim(new Data("1102190343", solicitudServicio.getId()));
        JsfUti.executeJS("PF('dlgTituloCredito').show()");
        JsfUti.update("formTituloCredito");
    }

    public void addTituloCredito(Data data) {

    }

    public void verificarEmisionTitulos() {
        try {
        
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "SolicitudServicionRevisionMB", e);
        }
    }

    public LazyModelWS<SolicitudServicios> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<SolicitudServicios> lazy) {
        this.lazy = lazy;
    }

    
    public List<Data> getListData() {
        return listData;
    }

    public void setListData(List<Data> listData) {
        this.listData = listData;
    }

    public String getCiUser() {
        return ciUser;
    }

    public void setCiUser(String ciUser) {
        this.ciUser = ciUser;
    }
}
