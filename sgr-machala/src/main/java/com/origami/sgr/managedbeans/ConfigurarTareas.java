/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class ConfigurarTareas implements Serializable {

    @Inject
    protected Entitymanager em;

    protected Map map;
    protected Long tramite;

    protected HistoricoTramites ht;
    protected List<RegpTareasTramite> tareas;

    @PostConstruct
    protected void iniView() {
        try {
            tareas = new ArrayList<>();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarTareas() {
        try {
            if (tramite != null) {
                tareas = em.findAll(Querys.getRegpTareasTramite, new String[]{"tramite"}, new Object[]{tramite});
            } else {
                JsfUti.messageWarning(null, "Debe ingresar numero de tramite para continuar.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void desactivarTarea(RegpTareasTramite rtt) {
        try {
            rtt.setEstado(!rtt.getEstado());
            em.update(rtt);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

}
