/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author OrigamiEC
 */
public class TareasActivas implements Serializable {

    private Long id;
    private Integer numTramite;
    private Integer secuencial;
    private String codigo;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private String nombreSolicitante;
    private String idProcesoTemp;
    private Boolean entregado;
    private Boolean blocked;
    private String carpetaRep;
    private Long idTipoTramite;
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
    private Integer rev_;
    private String ciRucSolicitante;
    private String correo;
    private boolean notificacion;
    private String descripcionInconveniente;
    private String asunto;
    private String campoReferencia;

    public TareasActivas() {
    }

    public TareasActivas(Long id) {
        this.id = id;
    }

    public TareasActivas(Integer numTramite) {
        this.numTramite = numTramite;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcionInconveniente() {
        return descripcionInconveniente;
    }

    public void setDescripcionInconveniente(String descripcionInconveniente) {
        this.descripcionInconveniente = descripcionInconveniente;
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

    public Integer getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Integer numTramite) {
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

    public String getCarpetaRep() {
        return carpetaRep;
    }

    public void setCarpetaRep(String carpetaRep) {
        this.carpetaRep = carpetaRep;
    }

    public Long getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(Long idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
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

    public Integer getRev_() {
        return rev_;
    }

    public void setRev_(Integer rev_) {
        this.rev_ = rev_;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(boolean notificacion) {
        this.notificacion = notificacion;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    @Override
    public String toString() {
        return "TareasActivas{" + "id=" + id + ", numTramite=" + numTramite + ", secuencial=" + secuencial + ", codigo=" + codigo + ", fechaIngreso=" + fechaIngreso + ", fechaEntrega=" + fechaEntrega + ", nombreSolicitante=" + nombreSolicitante + ", idProcesoTemp=" + idProcesoTemp + ", entregado=" + entregado + ", blocked=" + blocked + ", carpetaRep=" + carpetaRep + ", idTipoTramite=" + idTipoTramite + ", descripcion=" + descripcion + ", taskId=" + taskId + ", procInstId=" + procInstId + ", taskDefKey=" + taskDefKey + ", name_=" + name_ + ", assignee=" + assignee + ", createTime=" + createTime + ", formKey=" + formKey + ", priority=" + priority + ", candidate=" + candidate + ", rev_=" + rev_ + ", ciRucSolicitante=" + ciRucSolicitante + ", correo=" + correo + ", notificacion=" + notificacion + ", descripcionInconveniente=" + descripcionInconveniente + ", asunto=" + asunto + ", campoReferencia=" + campoReferencia + '}';
    }

}
