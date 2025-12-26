/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.Tramites;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.services.interfaces.BpmBaseEngine;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


@Named()
@ViewScoped
public class TramitesMB extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(TramitesMB.class.getName());
    @Inject
    private AppServices appServices;
    @Inject
    private BpmBaseEngine baseEngine;

    private LazyModelWS<Tramites> lazyTramites;
    protected List<Attachment> listAttach = new ArrayList<>();
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected HistoricoTramites ht;
    private List<Observaciones> observaciones;
    private StreamedContent imageProcessInstance;
    private Tramites tram, tramites;
    private String observacion;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        lazyTramites = new LazyModelWS<>(SisVars.ws + "tramites/findAll?sort=id,DESC", Tramites[].class, session.getToken());
    }

    public void showDetalis(Tramites tramite) {
        try {
            this.tram = tramite;
            tareas = this.getTaskByProcessInstanceIdMain(tramite.getProcInstId());
            listAttach = this.getProcessInstanceAllAttachmentsFiles(tramite.getProcInstId());
            ht = appServices.buscarHistoricoTramite(new HistoricoTramites(tramite.getTramite().getId()));
            observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(ht.getId())));
            InputStream diagram = baseEngine.getProcessInstanceDiagram(tramite.getProcInstId());
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

    /**
     *
     * @param terminado
     */
    public void filterByEstado(Boolean terminado) {
        try {
            lazyTramites = new LazyModelWS<>(SisVars.ws + "tramites/findAll?terminado=" + terminado + "&sort=id,DESC", Tramites[].class, session.getToken());
            JsfUti.update("formMain");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "finalizados", e);
        }
    }

    public void closeDlogoViewTramite() {
        JsfUti.executeJS("PF('dlgVerInfoRp').hide()");
        JsfUti.update("formInformLiq");
    }

    public void cerrarDialog() {
        JsfUti.executeJS("PF('dlgTramiteActivar').hide()");
    }

    public void reactivarTramiteActiviti() {
        try {
            if (Utils.isNotEmptyString(observacion)) {
                if (tramites.getTerminado()) {
                    if (this.saveObservacion()) {
                        HashMap<String, Object> v = this.variableTramitesFinalizado(tramites.getProcInstId());
                        ProcessInstance p = this.startProcessByDefinitionKey(tramites.getTramite().getTipoTramite().getActivitykey(), v);
                        if (p != null) {
                            tramites.getTramite().setIdProceso(p.getId());
                            service.methodPOST(tramites.getTramite(), SisVars.ws + "historicoTramite/actualizarProcessInstance", HistoricoTramites.class);
                            if (tramites.getSolicitudServicios() != null && tramites.getSolicitudServicios().getId() != null) {
                                tramites.getSolicitudServicios().setFinalizado(Boolean.FALSE);
                                appServices.actualizarSolicitudServicios(tramites.getSolicitudServicios());
                            }
                            JsfUti.messageInfo(null, "Trámite reactivado con éxito.", "");
                            JsfUti.executeJS("PF('dlgTramiteActivar').hide()");
                            loadModel();
                        } else {
                            JsfUti.messageError(null, Messages.error, "");
                        }
                    }
                } else {
                    JsfUti.messageError(null, "El trámite debe estar finalizado para reactivarlo", "");
                }
            } else {
                JsfUti.messageError(null, "Debe ingresar una observación para continuar", "");
            }

        } catch (Exception e) {
            System.out.println("//EXCEPTION " + e.getMessage());
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean saveObservacion() {
        try {
            Observaciones ob = new Observaciones();
            ob.setObservacion(observacion);
            ob.setEstado(true);
            ob.setFecCre(new Date());
            ob.setIdTramite(new HistoricoTramites(tramites.getTramite().getId()));
            ob.setTarea("REACTIVAR TRAMITE");
            ob.setUserCre(session.getName_user());
            appServices.guardarActualizarObservacion(ob);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void showDlgReactivar(Tramites tramites) {
        try {
            this.tramites = tramites;
            JsfUti.update("formEdit");
            JsfUti.executeJS("PF('dlgTramiteActivar').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModelWS<Tramites> getLazyTramites() {
        return lazyTramites;
    }

    public void setLazyTramites(LazyModelWS<Tramites> lazyTramites) {
        this.lazyTramites = lazyTramites;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public List<Attachment> getListAttach() {
        return listAttach;
    }

    public void setListAttach(List<Attachment> listAttach) {
        this.listAttach = listAttach;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
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
    public Tramites getTram() {
        return tram;
    }

    public void setTram(Tramites tram) {
        this.tram = tram;
    }

    public Tramites getTramites() {
        return tramites;
    }

    public void setTramites(Tramites tramites) {
        this.tramites = tramites;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
