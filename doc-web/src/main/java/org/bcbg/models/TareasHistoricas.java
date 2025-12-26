/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.TipoTramite;
import java.util.Date;

/**
 *
 * @author jesus
 */
public class TareasHistoricas {

    private Long id;
    private HistoricoTramites tramite;
    private String codigo;
    private Long numTramite;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private String nombreSolicitante;
    private String idProcesoTemp;
    private Boolean entregado;
    private Boolean blocked;
    private TipoTramite idTipoTramite;
    private String descripcion;
    private String taskId;
    private String procInstId;
    private String taskDefKey;
    private String name_;
    private String assignee;
    private Date createTime;
    private String formKey;
    private Integer priority;
    private String candidate;
    private String ciRucSolicitante;
    private Boolean terminado;
    private Date endTime;
    private Short periodo;
    private String endTimeString;
    private String startTimeString;
    private String campoReferencia;

    public TareasHistoricas() {
    }

    public TareasHistoricas(Long id) {
        this.id = id;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCiRucSolicitante() {
        return ciRucSolicitante;
    }

    public void setCiRucSolicitante(String ciRucSolicitante) {
        this.ciRucSolicitante = ciRucSolicitante;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    @Override
    public String toString() {
        return "TareasHistoricas{" + "id=" + id + '}';
    }

}
