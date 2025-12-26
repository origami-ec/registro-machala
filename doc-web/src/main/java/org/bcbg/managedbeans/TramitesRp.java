/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.bpm.models.DetalleProceso;
import org.bcbg.bpm.models.TareaWF;
import org.bcbg.config.RolesEspeciales;
import org.bcbg.config.SisVars;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.User;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.lazymodels.ProcessInstanceLazy;
import org.bcbg.services.interfaces.BpmBaseEngine;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class TramitesRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(TramitesRp.class.getName());
    @Inject
    private BpmBaseEngine baseEngine;
    protected List<TareaWF> tareasWF = new ArrayList<>();
    protected Boolean esAdmin = false;
    protected String usuario, usuario2, usuario3;
    protected String taskDefKey, taskDefKey2, taskDefKey3;
    protected String observacion, idProcess, candidatos;
    protected Long tramite;
    protected Integer prioridad = 0, cantidad = 0, cantidad2 = 0, cantidad3 = 0;
    protected User user = new User();
    protected ProcessInstanceLazy details;
    protected DetalleProceso proceso;
    protected HistoricoTramites ht;
//    protected List<User> users = new ArrayList<>();
    protected LazyModelWS<User> users;
    protected HistoricTaskInstance tareaActual;
    protected List<Attachment> listAttach = new ArrayList<>();
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected HashMap<String, Object> pars;
    private List<Observaciones> observaciones;
    private StreamedContent imageProcessInstance;

    @PostConstruct
    protected void iniView() {
        try {
//            imageProcessInstance = new DefaultStreamedContent();
            proceso = new DetalleProceso();
            details = new ProcessInstanceLazy(false, appServices);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarTramitePendiente() {
        try {
            if (usuario != null) {
                tareasWF = this.getListaTareasPersonales(usuario, null);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean validateAdmin(List<Long> list) {
        for (Long id : list) {
            if (id.equals(RolesEspeciales.ADMINISTRADOR)) {
                return true;
            }
        }
        return false;
    }

    public void reasignarTramitesByUsuario(SelectEvent event) {
        try {
            user = (User) event.getObject();
            if (user != null && user.getNombreUsuario() != null) {
                if (!tareasWF.isEmpty()) {
                    for (TareaWF tw : tareasWF) {
                        this.reasignarTarea(tw.getTarea().getId(), user.getNombreUsuario());
                        Map<String, Object> v = this.engine.getvariables(tw.getTarea().getProcessInstanceId());
                        for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                            if (entrySet.getValue() != null && entrySet.getValue().equals(tw.getTarea().getAssignee())) {
                                this.setVariableByProcessInstance(tw.getTarea().getProcessInstanceId(), entrySet.getKey(), user.getNombreUsuario());
                            }
                        }
                    }
                    JsfUti.messageInfo(null, "Al parecer reasignacion con exito.", "");
                    tareasWF = new ArrayList<>();
                } else {
                    JsfUti.messageWarning(null, "No se encontraron tramites.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se encontro usuario de sistema.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDelete(DetalleProceso dp) {
        try {
            proceso = dp;
            JsfUti.update("formEdit");
            JsfUti.executeJS("PF('dlgTramiteEliminar').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void deleteTramite() {
        try {
            if (proceso.getId() != null) {
                if (observacion != null) {
                    if (this.saveObservacion()) {
                        this.deleteProcessInstance(proceso.getInstancia());
                        JsfUti.messageInfo(null, "Trámite eliminado.", "");
                        JsfUti.update("formMain:tbViewTramites:dtDetail");
                        JsfUti.executeJS("PF('dlgTramiteEliminar').hide();");
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR", "");
        }
    }

    public void cancelarEliminarTramite() {
        JsfUti.executeJS("PF('dlgTramiteEliminar').hide()");
        JsfUti.update("formEdit");
    }

    public boolean saveObservacion() {
        try {
            Observaciones ob = new Observaciones();
            ob.setObservacion(observacion);
            ob.setEstado(true);
            ob.setFecCre(new Date());
            ob.setIdTramite(new HistoricoTramites(proceso.getId()));
            ob.setTarea("ELIMINAR TRAMITE");
            ob.setUserCre(session.getName_user());
            appServices.guardarActualizarObservacion(ob);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void reactivarTramite(DetalleProceso dp) {
        try {
            proceso = dp;
            System.out.println("INSTANCIA " + dp.getInstancia());
            tareas = this.getTaskByProcessInstanceIdMain(proceso.getInstancia());
            listAttach = this.getProcessInstanceAllAttachmentsFiles(proceso.getInstancia());
            ht = appServices.buscarHistoricoTramite(new HistoricoTramites(proceso.getId()));
            observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(ht.getId())));
            InputStream diagram = baseEngine.getProcessInstanceDiagram(proceso.getInstancia());
            if (diagram != null) {
                imageProcessInstance = DefaultStreamedContent.builder().stream(() -> diagram).build();
                //imageProcessInstance = new DefaultStreamedContent().builder().build();
            }
            this.initDocEsc(this.ht.getNumTramite().toString());
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
//        if (solicitudServicio.getFinalizado()) {
//            solicitudServicio.setFinalizado(Boolean.FALSE);
//            appServices.actualizarSolicitudServicios(solicitudServicio);
//            String asignado = observaciones.get(observaciones.size() - 1).getUserCre();
//            String usuarioJefe = appServices.getCandidateUserByList(appServices.getUserXDeptsXrevisores(solicitudServicio.getDepartamento().getId()));
//            HashMap<String, Object> pars = new HashMap<>();
//            pars.put("asignarUsuario", asignado);
//            pars.put("validarSolicitud", jefe);
//            pars.put("prioridad", 50);
//        } else {
//            JsfUti.messageError(null, "El trámite no ha sido finalizado", "");
//        }
    }

    public void showDetalis(DetalleProceso dp) {
        try {
            proceso = dp;
            System.out.println("INSTANCIA " + dp.getInstancia());
            tareas = this.getTaskByProcessInstanceIdMain(proceso.getInstancia());
            listAttach = this.getProcessInstanceAllAttachmentsFiles(proceso.getInstancia());
            ht = appServices.buscarHistoricoTramite(new HistoricoTramites(proceso.getId()));
            observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(ht.getId())));
            InputStream diagram = baseEngine.getProcessInstanceDiagram(proceso.getInstancia());
            if (diagram != null) {
                imageProcessInstance = DefaultStreamedContent.builder().stream(() -> diagram).build();
                //imageProcessInstance = new DefaultStreamedContent().builder().build();
            }
            this.initDocEsc(this.ht.getNumTramite().toString());
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgPrioridad(DetalleProceso dp) {
        try {
            proceso = dp;
            prioridad = (int) this.getVariableByPorcessIntance(dp.getInstancia(), "prioridad");
            JsfUti.update("formEditPrioridadTra");
            JsfUti.executeJS("PF('editPrioridadTraDlg').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarPrioridad() {
        try {
            if (proceso.getInstancia() != null && prioridad > 0) {
                this.setVariableByProcessInstance(proceso.getInstancia(), "prioridad", prioridad);
                List<Task> tareasActivas = this.obtenerTareasActivasProcessInstance(proceso.getInstancia());
                this.asignarTareaPriority(tareasActivas, prioridad);
                JsfUti.messageInfo(null, "Prioridad de tramite actualizada.", "");
                JsfUti.update("formMain:tbViewTramites:dtDetail");
                JsfUti.executeJS("PF('editPrioridadTraDlg').hide();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void showDlgReasigancion(DetalleProceso dp) {
        try {
            proceso = dp;
            map = new HashMap();
            map.put("id", proceso.getId());
            ht = appServices.buscarHistoricoTramite(new HistoricoTramites(proceso.getId()));
//            users = appServices.getUsuarios();
            users = new LazyModelWS<>(SisVars.ws + "usuarios", User[].class, session.getToken());
            tareaActual = this.getTaskByProcessInstance(dp.getInstancia());
            if (tareaActual.getEndTime() == null) {
//                if (tareaActual.getAssignee() == null) {
//                    JsfUti.messageError(null, "Esta tarea no se puede re-asignar. Tiene usuarios candidatos.", "");
//                    return;
//                }
                JsfUti.update("formreasignar");
                JsfUti.executeJS("PF('dlgReasignar').show();");
            } else {
                JsfUti.messageError(null, "No se puede re asignar Tarea. Tarea Finalizada.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void reasignarTarea(User user) {
        try {

            String obs = "TAREA: " + tareaActual.getName() + ", USUARIO ANTERIOR: " + tareaActual.getAssignee() + ", USUARIO ACTUAL: "
                    + user.getNombreUsuario();
            appServices.guardarObservaciones(ht, session.getName_user(), obs, "REASIGNACIÓN DE USUARIO");
            this.reasignarTarea(tareaActual.getId(), user.getNombreUsuario());
            Map<String, Object> v = this.engine.getvariables(tareaActual.getProcessInstanceId());
            for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                if (entrySet.getValue() != null && entrySet.getValue().equals(tareaActual.getAssignee())) {
                    this.setVariableByProcessInstance(tareaActual.getProcessInstanceId(), entrySet.getKey(), user.getNombreUsuario());
                }
            }

            JsfUti.executeJS("PF('dlgReasignar').hide();");
            JsfUti.update("formMain:tbViewTramites:dtDetail");
            JsfUti.messageInfo(null, "Tarea Re-Asignada.", "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void asignarCanditeUsers() {
        try {
            if (usuario2 != null && candidatos != null) {
                if (!tareasWF.isEmpty()) {
                    for (TareaWF tw : tareasWF) {
                        this.setCandidateUsers(tw.getTarea().getId(), candidatos);
                    }
                    JsfUti.messageInfo(null, "Tareas con candidatos.", "");
                    tareasWF = new ArrayList<>();
                } else {
                    JsfUti.messageInfo(null, "No se encontraron tareas.", "");
                }
            } else {
                JsfUti.messageInfo(null, "Debe ingresar los usuarios candidatos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void deleteCanditeUsers() {
        try {
            if (usuario3 != null) {
                if (!tareasWF.isEmpty()) {
                    for (TareaWF tw : tareasWF) {
                        this.eliminarCandidateUsers(tw.getTarea().getId(), usuario3);
                    }
                    JsfUti.messageInfo(null, "Tareas con candidatos.", "");
                    tareasWF = new ArrayList<>();
                } else {
                    JsfUti.messageInfo(null, "No se encontraron tareas.", "");
                }
            } else {
                JsfUti.messageInfo(null, "Debe ingresar el usuario para eliminar tareas.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void closeDlogoViewTramite() {
        JsfUti.executeJS("PF('dlgVerInfoRp').hide()");
        JsfUti.update("formInformLiq");
    }

    public void executeJobManagementService() {
        try {
            if (idProcess != null) {
                this.getProcessEngine().getManagementService().executeJob(idProcess);
                System.out.println("Execute job: " + idProcess);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cantidadTareas() {
        if (usuario != null) {
            cantidad = this.getCantidadTareasUser(usuario, taskDefKey);
            tareasWF = this.getListaTareasPersonales(usuario, taskDefKey);
        }
    }

    public void cantidadTareas2() {
        if (usuario2 != null) {
            cantidad2 = this.getCantidadTareasUser(usuario2, taskDefKey2);
            tareasWF = this.getListaTareasPersonales(usuario2, taskDefKey2);
        }
    }

    public void cantidadTareas3() {
        if (usuario3 != null) {
            cantidad3 = this.getCantidadTareasUser(usuario3, taskDefKey3);
            tareasWF = this.getListaTareasPersonales(usuario3, taskDefKey3);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
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

    public ProcessInstanceLazy getDetails() {
        return details;
    }

    public void setDetails(ProcessInstanceLazy details) {
        this.details = details;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<Attachment> getListAttach() {
        return listAttach;
    }

    public void setListAttach(List<Attachment> listAttach) {
        this.listAttach = listAttach;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public DetalleProceso getProceso() {
        return proceso;
    }

    public void setProceso(DetalleProceso proceso) {
        this.proceso = proceso;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public LazyModelWS<User> getUsers() {
        return users;
    }

    public void setUsers(LazyModelWS<User> users) {
        this.users = users;
    }

    public HistoricTaskInstance getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(HistoricTaskInstance tareaActual) {
        this.tareaActual = tareaActual;
    }

    public String getIdProcess() {
        return idProcess;
    }

    public void setIdProcess(String idProcess) {
        this.idProcess = idProcess;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getCandidatos() {
        return candidatos;
    }

    public void setCandidatos(String candidatos) {
        this.candidatos = candidatos;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(Integer cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskDefKey2() {
        return taskDefKey2;
    }

    public void setTaskDefKey2(String taskDefKey2) {
        this.taskDefKey2 = taskDefKey2;
    }

    public String getUsuario3() {
        return usuario3;
    }

    public void setUsuario3(String usuario3) {
        this.usuario3 = usuario3;
    }

    public String getTaskDefKey3() {
        return taskDefKey3;
    }

    public void setTaskDefKey3(String taskDefKey3) {
        this.taskDefKey3 = taskDefKey3;
    }

    public Integer getCantidad3() {
        return cantidad3;
    }

    public void setCantidad3(Integer cantidad3) {
        this.cantidad3 = cantidad3;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }
//</editor-fold>
}
