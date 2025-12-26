/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.bpm.models.TareaWF;
import org.bcbg.config.RolesEspeciales;
import org.bcbg.config.SisVars;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.TareasActivas;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.User;
import org.bcbg.lazymodels.LazyModelTareas;
import org.bcbg.models.Data;
import org.bcbg.util.JsfUti;
import org.bcbg.ws.BcbgService;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DashBoard extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private BcbgService service;

    protected List<TareaWF> tareasWF = new ArrayList<>();
    protected Boolean esAdmin = false;
    protected String usuario, taskDefKey;
    protected User user = new User();
    protected LazyModelTareas<TareasActivas> lazy;
    protected Long cantidad = 0L;
    protected Date hoy = new Date();
    protected int data = 0;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private StringBuilder queryCount = null;
    private List<ServiciosDepartamento> servicios;

    @PostConstruct
    protected void iniView() {
        try {
            usuario = session.getName_user();
            if (usuario != null) {
                queryCount = new StringBuilder("SELECT CAST(COUNT(*) AS INTEGER) FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
                this.validateRoles(session.getRoles());
                //cantidad = this.getCantidadTareasUser(usuario);
                Data dataCantidad = (Data) service.methodPOST(new Data(usuario), SisVars.ws + "tareasActivas/cantidad", Data.class);
                cantidad = dataCantidad.getId();
                //int temp = reg.getCantidadTramitesByUser(session.getUserId());
                data = 2;
                lazy = new LazyModelTareas<>(SisVars.ws
                        + "tareasActivas/searchUrl?search=(assignee:" + usuario + " OR candidate:*" + usuario + "*)&sort=numTramite,DESC",
                        TareasActivas[].class, session.getToken());
                /*if (temp > 0) {
                    data = 1;
                    tareasWF = this.getListaTareasPersonales(usuario, temp, taskDefKey);
                } else {
                    data = 2;
                    lazy = new TareasWFLazy(usuario, taskDefKey);
                }*/
                loadServiciosAlcaldia();
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Boolean renderedColumn() {
        return session.getDepts().contains(92L) || session.getDepts().contains(355L);
    }

    private void loadServiciosAlcaldia() {
        servicios = appServices.getListItems(new ServiciosDepartamento(new TipoTramite(Boolean.TRUE, "PAPC")));
    }

    public void actualizarTramitePendiente() {
        try {
            if (usuario != null && !usuario.isEmpty()) {
                lazy = new LazyModelTareas<>(SisVars.ws
                        + "tareasActivas/searchUrl?search=assignee:" + usuario + " OR candidate:*" + usuario + "*",
                        TareasActivas[].class, session.getToken());
                //cantidad = this.getCantidadTareasUser(usuario, taskDefKey);
                //cantidad = (Integer) manager.getNativeQuery(queryCount.toString(), new Object[]{usuario, usuario});
                Data dataCantidad = (Data) service.methodPOST(new Data(usuario), SisVars.ws + "tareasActivas/cantidad", Data.class);
                cantidad = dataCantidad.getId();
                JsfUti.update("formMain");
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void validateRoles(List<Long> list) {
        for (Long id : list) {
            if (id.equals(RolesEspeciales.ADMINISTRADOR)) {
                esAdmin = true;
            }
        }
    }

    public void reasignarTramitesByUsuario(SelectEvent event) {
        try {
            user = (User) event.getObject();
            if (user != null && user.getNombreUsuario() != null) {
                for (TareaWF tw : tareasWF) {
                    this.reasignarTarea(tw.getTarea().getId(), user.getNombreUsuario());
                    Map<String, Object> v = this.engine.getvariables(tw.getTarea().getProcessInstanceId());
                    for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                        if (entrySet.getValue() != null && entrySet.getValue().equals(tw.getTarea().getAssignee())) {
                            this.setVariableByProcessInstance(tw.getTarea().getProcessInstanceId(), entrySet.getKey(), user.getNombreUsuario());
                        }
                    }
                }
                JsfUti.messageInfo(null, "Al parecer reasignacion con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "No se pudo reasignar los tramites.", "");
            }
        } catch (Exception e) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public List<TareaWF> getTareasWF() {
        return tareasWF;
    }

    public void setTareasWF(List<TareaWF> tareasWF) {
        this.tareasWF = tareasWF;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Boolean getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LazyModelTareas<TareasActivas> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelTareas<TareasActivas> lazy) {
        this.lazy = lazy;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public List<ServiciosDepartamento> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServiciosDepartamento> servicios) {
        this.servicios = servicios;
    }

}
