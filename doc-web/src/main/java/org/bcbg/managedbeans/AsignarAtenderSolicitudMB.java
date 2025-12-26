/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.Persona;
import org.bcbg.entities.Rol;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.User;
import org.bcbg.models.Correo;
import org.bcbg.models.CorreoArchivo;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author jesus
 */
@Named
@ViewScoped
public class AsignarAtenderSolicitudMB extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private AppServices appServices;
    @Inject
    private BcbgService service;
    private String observacion;
    private List<RolUsuario> rolesUsuarios;
    private User userAsignado;
    private HistoricoTramites historicoTramites;
    private SolicitudDepartamento solicitudDepartamento;
    private List<Observaciones> observaciones;
    private List<Departamento> departamentosSolicitud;
    private SolicitudServicios solicitudServicios;
    private int atender;
    private List<SolicitudDocumentos> documentos, documentosNuevos;
    private Persona usuarioIniciTrmaite;
    private List<Observaciones> listaObervacionTemp;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        observacion = "";
        rolesUsuarios = new ArrayList<>();
        if (session.getTaskID() != null) {
            this.setTaskId(session.getTaskID());
            if (session.getNumTramite() != null) {
//                historicoTramites = appServices.buscarHistoricoTramite(new HistoricoTramites(null, session.getNumTramite()));
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
                rolUsuarioByDepartamento();
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

    public void rolUsuarioByDepartamento() {
        rolesUsuarios = appServices.getRolesUsuarios(new RolUsuario(
                new Rol(Boolean.TRUE, new Departamento(historicoTramites.getTipoTramite().getDepartamento().getId()))));
    }

    public void asignarUsuario(User u) {
        userAsignado = u;
        atender = 2;
        JsfUti.executeJS("PF('observacionDlg').show()");
    }

    public void rechazarTarea() {
        atender = 3;
        JsfUti.executeJS("PF('observacionDlg').show()");
    }

    public void resolver() {
        atender = 1;
        completarTarea();
    }

    public void completarTarea() {
        try {
            //atender si es 2 asigna ... si es 1 atiende. si es 3 lo rechaza
            HashMap<String, Object> par = new HashMap<>();
            par.put("atender", atender);
            if (atender == 2) {
                if (userAsignado != null && userAsignado.getId() != null) {
                    par.put("usuario", userAsignado.getNombreUsuario());
                    this.reasignarTarea(this.getTaskId(), userAsignado.getNombreUsuario());
                } else {
                    JsfUti.messageError(null, "Asigne un usuario a la solicitud", "");
                    return;
                }
            } else if (atender == 1) {
                enviarCorrreo(usuarioInicioTramite().getCorreo(), usuarioInicioTramite().getNombresApellidos());
            }
            solicitudServicios.setDescripcionInconveniente(observacion);
            appServices.guardarObservacionHT(historicoTramites, this.getTaskDataByTaskID().getName(), getObservacion());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void enviarCorrreo(String email, String usuario) {
        if (Utils.isNotEmptyString(email)
                && Utils.validacionCorreos(email)) {
            Correo correo = new Correo();
            correo.setAsunto("Trámite: " + historicoTramites.getTipoTramite().getDescripcion());
            correo.setDestinatario(email);
            correo.setMensaje(getMensajeNotf(usuario));
            correo.setRemitente(session.getUsuarioDocs());
            appServices.enviarCorreo(correo);
        }
    }

    public void enviarCorreo() {
        System.out.println("entro al envio de correo");
        List<CorreoArchivo> listCorreoArchivo = new ArrayList<>();
        CorreoArchivo correoArchivo;
        Boolean validar = false;
        if (!documentos.isEmpty()) {
            for (SolicitudDocumentos solic : documentos) {
                if (solic.getCheck()) {
                    validar = true;
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
                            "Este correo fue enviado de forma automática y no requiere respuesta."), listCorreoArchivo, historicoTramites.getCodigo(), solicitudServicios.getEnteSolicitante().getNombreCompleto(),session.getUsuarioDocs(), session.getUsuarioDocs());
            appServices.enviarCorreo(correo);
            JsfUti.messageInfo(null, "Se realizo el envio de Correo", "Información");

        } else {
            JsfUti.messageError(null, "Adjunte un requisito", "Información");

        }
    }

    private String getMensajeNotf(String user) {
        Locale espanol = new Locale("es", "ES");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy", espanol);
        String fecha = dateFormat.format(solicitudServicios.getFechaMaximaRespuesta());

        return Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo() + "-" + solicitudServicios.getTipoTramite().getDescripcion().toUpperCase(),
                "<strong>Estimado(a):</strong><br><strong>" + user + "</strong>"
                + "por medio de presente le informamos que el tramite <strong>" + solicitudServicios.getTipoTramite().getDescripcion().toUpperCase() + "</strong> "
                + ",despues de haber revisado los documentos, se envia le fecha de la inspección acordada para el " + fecha,
                "<strong>Gracias por la Atención Brindada</strong><br>",
                "Este correo fue enviado de forma automática y no requiere respuesta.");
    }

    public Persona usuarioInicioTramite() {
        listaObervacionTemp.addAll(observaciones);
        Collections.sort(listaObervacionTemp, (obj1, obj2) -> obj1.getId().compareTo(obj2.getId()));
        usuarioIniciTrmaite = appServices.getUserIncioTramite(listaObervacionTemp.get(0).getUserCre());
        return usuarioIniciTrmaite;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
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

    public HistoricoTramites getHistoricoTramites() {
        return historicoTramites;
    }

    public void setHistoricoTramites(HistoricoTramites historicoTramites) {
        this.historicoTramites = historicoTramites;
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

    public User getUserAsignado() {
        return userAsignado;
    }

    public void setUserAsignado(User userAsignado) {
        this.userAsignado = userAsignado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<RolUsuario> getRolesUsuarios() {
        return rolesUsuarios;
    }

    public void setRolesUsuarios(List<RolUsuario> rolesUsuarios) {
        this.rolesUsuarios = rolesUsuarios;
    }

//</editor-fold>
}
