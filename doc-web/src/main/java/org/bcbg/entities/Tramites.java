/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class Tramites {

    private Long id;
    private Long numTramite;
    private HistoricoTramites tramite;
    private TipoTramite idTipoTramite;
    private String procInstId;
    private String procDefId;
    private Date startTime;
    private Date endTime;
    private String deleteReason;
    private BigInteger duration;
    private String callProcInstId;
    private String participants;
    private Short periodo;
    private Boolean terminado;
    private Long pendientes;
    private Long terminados;
    private String servicio;
    private String departamento;
    private String campoReferencia;
    private SolicitudServicios solicitudServicios;

    public Tramites() {
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }

    public Long getTerminados() {
        return terminados;
    }

    public void setTerminados(Long terminados) {
        this.terminados = terminados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public TipoTramite getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(TipoTramite idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public BigInteger getDuration() {
        return duration;
    }

    public void setDuration(BigInteger duration) {
        this.duration = duration;
    }

    public String getCallProcInstId() {
        return callProcInstId;
    }

    public void setCallProcInstId(String callProcInstId) {
        this.callProcInstId = callProcInstId;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    @Override
    public String toString() {
        return "Tramites{" + "id=" + id + '}';
    }

}
