/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import static org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import org.bcbg.entities.Persona;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.models.Correo;
import org.bcbg.models.CorreoArchivo;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author jesus
 */
@Named
@ViewScoped
public class AtenderSolicitudMB extends BpmManageBeanBaseRoot implements Serializable {

    private String observacion;
    private HistoricoTramites historicoTramites;
    private SolicitudDepartamento solicitudDepartamento;
    private List<Observaciones> observaciones;
    private List<Departamento> departamentosSolicitud;
    private SolicitudServicios solicitudServicios;
    private List<SolicitudDocumentos> documentos, documentosNuevos;
    private Persona usuarioIniciTrmaite;
    private List<Observaciones> listaObervacionTemp;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        if (session.getTaskID() != null) {
            this.setTaskId(session.getTaskID());
            if (session.getNumTramite() != null) {
                solicitudServicios = appServices.buscarSolicitudServicios(new SolicitudServicios(new HistoricoTramites(null, session.getNumTramite())));
                solicitudDepartamento = solicitudServicios.getSolicitudDepartamentos().get(0);
                loadDepartamentos();
                documentos = appServices.buscarSolicitudDocumentos(new SolicitudDocumentos(new SolicitudServicios(solicitudServicios.getId())));
                documentosNuevos = new ArrayList<>();
                solicitudServicios.setDocumentos(documentos);
                historicoTramites = solicitudServicios.getTramite();
                observacion = "";
                observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(historicoTramites.getId())));
                listaObervacionTemp = new ArrayList<>();
                usuarioIniciTrmaite = new Persona();
            } else {
                this.continuar();
            }
        }
    }

    private void loadDepartamentos() {
        departamentosSolicitud = new ArrayList<>();
        for (SolicitudDepartamento sd : solicitudServicios.getSolicitudDepartamentos()) {
            if (!departamentosSolicitud.contains(sd.getDepartamento())) {
                departamentosSolicitud.add(sd.getDepartamento());
            }
        }
    }

    public void enviarCorreo() {
        List<CorreoArchivo> listCorreoArchivo = new ArrayList<>();
        CorreoArchivo correoArchivo;
        Boolean validar = false;
        if (!documentos.isEmpty()) {
//            System.out.println("Correo " + solicitudServicios.getEnteSolicitante().getCorreo());
            for (SolicitudDocumentos solic : documentos) {
                if (solic.getCheck()) {
                    validar = true;
//                    System.out.println("requisito" + solic.getRequisito().getNombre());
                    correoArchivo = new CorreoArchivo(solic.getRutaArchivo(), solic.getTipoArchivo());
                    listCorreoArchivo.add(correoArchivo);
                }
            }
            if (!validar) {
                JsfUti.messageError(null, "Adjunte un requisito", "Información");
                return;
            }
            Correo correo = new Correo(solicitudServicios.getCorreoNotificacion(), "Trámite: " + historicoTramites.getTipoTramite().getDescripcion(),
                    Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo() + "-" + historicoTramites.getTipoTramite().getDescripcion(),
                            "<strong>¡Hola! </strong><br><strong>" + solicitudServicios.getEnteSolicitante().getNombreCompleto() + "</strong>"
                            + " Se envia los archivos correspondientes al trámite generado.",
                            "<strong>Gracias por la Atención Brindada</strong><br>",
                            "Este correo fue enviado de forma automática y no requiere respuesta."), listCorreoArchivo, historicoTramites.getCodigo(), solicitudServicios.getEnteSolicitante().getNombreCompleto(), session.getUsuarioDocs(), session.getUsuarioDocs());
            appServices.enviarCorreo(correo);
            JsfUti.messageInfo(null, "Se realizo el envio de Correo", "Información");

        } else {
            JsfUti.messageError(null, "Adjunte un requisito", "Información");
        }
    }

    public void completarTarea() {
        try {
            HashMap<String, Object> par = new HashMap<>();
            appServices.guardarObservacionHT(historicoTramites, this.getTaskDataByTaskID().getName(), getObservacion());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public HistoricoTramites getHistoricoTramites() {
        return historicoTramites;
    }

    public void setHistoricoTramites(HistoricoTramites historicoTramites) {
        this.historicoTramites = historicoTramites;
    }

    public SolicitudDepartamento getSolicitudDepartamento() {
        return solicitudDepartamento;
    }

    public void setSolicitudDepartamento(SolicitudDepartamento solicitudDepartamento) {
        this.solicitudDepartamento = solicitudDepartamento;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public List<Departamento> getDepartamentosSolicitud() {
        return departamentosSolicitud;
    }

    public void setDepartamentosSolicitud(List<Departamento> departamentosSolicitud) {
        this.departamentosSolicitud = departamentosSolicitud;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public List<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public List<SolicitudDocumentos> getDocumentosNuevos() {
        return documentosNuevos;
    }

    public void setDocumentosNuevos(List<SolicitudDocumentos> documentosNuevos) {
        this.documentosNuevos = documentosNuevos;
    }

    public Persona getUsuarioIniciTrmaite() {
        return usuarioIniciTrmaite;
    }

    public void setUsuarioIniciTrmaite(Persona usuarioIniciTrmaite) {
        this.usuarioIniciTrmaite = usuarioIniciTrmaite;
    }

    public List<Observaciones> getListaObervacionTemp() {
        return listaObervacionTemp;
    }

    public void setListaObervacionTemp(List<Observaciones> listaObervacionTemp) {
        this.listaObervacionTemp = listaObervacionTemp;
    }
//</editor-fold>
}
