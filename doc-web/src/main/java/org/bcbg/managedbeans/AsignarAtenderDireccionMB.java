/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.RolUsuario;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author jesus
 */
@ViewScoped
@Named
public class AsignarAtenderDireccionMB extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private AppServices appServices;
    @Inject
    private BcbgService service;
    private Departamento departamento;
    private List<Departamento> departamentos;
    private String observacion;
    private List<RolUsuario> roles;
    private HistoricoTramites historicoTramites;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        observacion = "";
        if (session.getTaskID() != null) {
            this.setTaskId(session.getTaskID());
            if (session.getNumTramite() != null) {
                historicoTramites = appServices.buscarHistoricoTramite(new HistoricoTramites(null, session.getNumTramite()));
                departamento = new Departamento();
                roles = new ArrayList<>();
                departamentos = appServices.getListDepartamentos();
            } else {
                this.continuar();
            }
        }

    }

    public void asignarDireccion(Departamento d) {
        departamento = d;
//        roles = appServices.getRolesUsuarios(new RolUsuario(new User(new Persona(Boolean.TRUE)),
//                new Rol(Boolean.TRUE, new Departamento(departamento.getId()))));
//        roles = lojaService.methodListGET(SisVars.ws + "rol/usuario/departamento/" + departamento.getId() + "/director/" + Boolean.TRUE
//                + "/esPersona/" + Boolean.TRUE, RolUsuario[].class);
        JsfUti.executeJS("PF('resolverDlg').show()");
    }

    public void completarTarea(Boolean atender) {
        try {
            //atender si es 2 asigna ... si es 1 atiende.
            HashMap<String, Object> par = new HashMap<>();
            if (atender) {
                par.put("atender", 1);
            } else {
                par.put("atender", 2);
                if (roles != null && !roles.isEmpty()) {
//                    par.put("asignarAtenderSolicitud", roles.get(0).getUsuario().getNombreUsuario());
//                    this.reasignarTarea(this.getTaskId(), roles.get(0).getUsuario().getNombreUsuario());
                } else {
                    JsfUti.messageError(null, "No hay un director o jefe asignado al departamento", "");
                    return;
                }
            }
            this.guardarObservacion();
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarObservacion() {
        try {
            if (historicoTramites != null && historicoTramites.getId() != null) {
                if (observacion != null || !observacion.isEmpty()) {
                    appServices.guardarObservacionHT(historicoTramites, this.getTaskDataByTaskID().getName(), observacion);
                } else {
                    JsfUti.messageInfo(null, "Ingrese la observacion", "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }
//</editor-fold>
}
