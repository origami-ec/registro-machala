/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import org.bcbg.models.UserTask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import jdk.jfr.Description;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;

/**
 *
 * @author ORIGAMI1
 */
public class SolicitudServicios implements Serializable {

    @Description("Clave primaria SolicitudServicios")
    private Long id;
    @Description("Nombres del solicitante del trámite")
    private String representante;
    @Description("Campo de referencia al trámite")
    private String campoReferencia;
    @Description("Observaciones al trámite")
    private String descripcionInconveniente;
    @Description(Variables.omitirCampo)
    private Boolean solicitudInterna;
    @Description("Respuesta del tramite")
    private String informe;
    @Description("Fecha de creación del tramite")
    private Date fechaCreacion;
    @Description(Variables.omitirCampo)
    private Boolean asignado;
    @Description("Usuarios asignados al trámite")
    private String asignados;
    @Description("Estado del trámite")
    private String status;
    @Description("URL asociadas al trámite")
    private String notaGuia;
    @Description("Fecha máxima de respuesta al trámite")
    private Date fechaMaximaRespuesta;
    @Description("Prioridad del trámite")
    private Integer prioridad;
    @Description("Tipo de trámite (Servicio)")
    private ServiciosDepartamento tipoServicio;
    @Description("Datos del trámite BPMN")
    private HistoricoTramites tramite;
    //@Description("Tipo de trámite BPMN")
    @Description(Variables.omitirCampo)
    private TipoTramite tipoTramite;
    @Description("Datos de la persona")
    private Persona enteSolicitante;
    @Description("Datos del usuario")
    private User usuarioIngreso;
    private List<SolicitudDepartamento> solicitudDepartamentos;
    private List<SolicitudDocumentos> documentos;
    @Description("Asunto del trámite")
    private String asunto;
    @Description("Número de oficio asociado al trámite")
    private String numOficio;
    @Description("Prioridad del trámite")
    private String tipoPrioridad;
    @Description(Variables.omitirCampo)
    private Boolean finalizado;
    @Description("Datos del departamento")
    private Departamento departamento;
    @Description("Correo de notificación del trámite")
    private String correoNotificacion;

    ///PAARA EL REPORTE
    private List<UserTask> usersTasks;
    @Description(Variables.omitirCampo)
    private Long usuarioCreacion;
    private List<String> urls;

    public SolicitudServicios() {
    }

    public SolicitudServicios(ServiciosDepartamento tipoServicio, User usuarioIngresa) {
        this.tipoServicio = tipoServicio;
        this.usuarioIngreso = usuarioIngresa;
    }

    public SolicitudServicios(User usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public SolicitudServicios(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public SolicitudServicios(ServiciosDepartamento tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public SolicitudServicios(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    public SolicitudServicios(Long id) {
        this.id = id;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }

    public List<UserTask> getUsersTasks() {
        return usersTasks;
    }

    public void setUsersTasks(List<UserTask> usersTasks) {
        this.usersTasks = usersTasks;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getTipoPrioridad() {
        return tipoPrioridad;
    }

    public void setTipoPrioridad(String tipoPrioridad) {
        this.tipoPrioridad = tipoPrioridad;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getNumOficio() {
        return numOficio;
    }

    public void setNumOficio(String numOficio) {
        this.numOficio = numOficio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getDescripcionInconveniente() {
        return descripcionInconveniente;
    }

    public void setDescripcionInconveniente(String descripcionInconveniente) {
        this.descripcionInconveniente = descripcionInconveniente;
    }

    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
    }

    public Persona getEnteSolicitante() {
        return enteSolicitante;
    }

    public void setEnteSolicitante(Persona enteSolicitante) {
        this.enteSolicitante = enteSolicitante;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public String getAsignados() {
        return asignados;
    }

    public void setAsignados(String asignados) {
        this.asignados = asignados;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public String getNotaGuia() {
        return notaGuia;
    }

    public void setNotaGuia(String notaGuia) {
        this.notaGuia = notaGuia;
    }

    public Date getFechaMaximaRespuesta() {
        return fechaMaximaRespuesta;
    }

    public void setFechaMaximaRespuesta(Date fechaMaximaRespuesta) {
        this.fechaMaximaRespuesta = fechaMaximaRespuesta;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public ServiciosDepartamento getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(ServiciosDepartamento tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public List<SolicitudDepartamento> getSolicitudDepartamentos() {
        return solicitudDepartamentos;
    }

    public void setSolicitudDepartamentos(List<SolicitudDepartamento> solicitudDepartamentos) {
        this.solicitudDepartamentos = solicitudDepartamentos;
    }

    public List<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public User getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(User usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public List<String> getUrls() {
        urls = new ArrayList<>();
        if (Utils.isNotEmptyString(notaGuia)) {
            urls = new ArrayList<String>(Arrays.asList(notaGuia.split(";")));
        }
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudServicios)) {
            return false;
        }
        SolicitudServicios other = (SolicitudServicios) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "SolicitudServicios{" + "id=" + id + ", representante=" + representante + ", descripcionInconveniente=" + descripcionInconveniente + ", solicitudInterna=" + solicitudInterna + ", informe=" + informe + ", fechaCreacion=" + fechaCreacion + ", asignado=" + asignado + ", asignados=" + asignados + ", status=" + status + ", notaGuia=" + notaGuia + ", fechaMaximaRespuesta=" + fechaMaximaRespuesta + ", prioridad=" + prioridad + ", tipoServicio=" + tipoServicio + ", tramite=" + tramite + ", tipoTramite=" + tipoTramite + ", enteSolicitante=" + enteSolicitante + ", usuarioIngreso=" + usuarioIngreso + ", solicitudDepartamentos=" + solicitudDepartamentos + ", documentos=" + documentos + ", asunto=" + asunto + ", numOficio=" + numOficio + ", tipoPrioridad=" + tipoPrioridad + ", finalizado=" + finalizado + ", departamento=" + departamento + ", correoNotificacion=" + correoNotificacion + ", usersTasks=" + usersTasks + ", usuarioCreacion=" + usuarioCreacion + ", urls=" + urls + '}';
    }

}
