/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.bpm.models;

import org.bcbg.entities.HistoricoTramites;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import org.activiti.engine.task.Task;

/**
 *
 * @author Anyelo
 */
public class TareaWF implements Serializable {

    protected BigInteger id;
    protected BigInteger numTramite;
    protected BigInteger numActos;
    protected Boolean entregado;
    protected Boolean blocked;
    protected Boolean estaReingresado;
    protected Boolean seleccionada = false;
    protected Boolean descripcionTareaMayor50char = false;
    protected Date fechaIngreso;
    protected Date fechaEntrega;
    protected Date fecIngSH;
    protected Date fecEntSH;
    protected HistoricoTramites tramite;
    protected Integer priority;
    protected Integer revisiones;
    protected Long tramiteRp;
    protected Long idTramite;
    protected String idProcesoTemp;
    protected String nombreSolicitante;
    protected String taskId;
    protected String formKey;
    protected String name;
    protected String candidate;
    protected String assignee;
    protected String nombres;
    protected String actos;
    protected Task tarea;

    public TareaWF() {
    }

    public Long getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Long idTramite) {
        this.idTramite = idTramite;
    }

    public Task getTarea() {
        return tarea;
    }

    public void setTarea(Task tarea) {
        this.tarea = tarea;
    }

    public Boolean getDescripcionTareaMayor50char() {
        return descripcionTareaMayor50char;
    }

    public void setDescripcionTareaMayor50char(Boolean descripcionTareaMayor50char) {
        this.descripcionTareaMayor50char = descripcionTareaMayor50char;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Long getTramiteRp() {
        return tramiteRp;
    }

    public void setTramiteRp(Long tramiteRp) {
        this.tramiteRp = tramiteRp;
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public Date getFecIngSH() {
        return fecIngSH;
    }

    public void setFecIngSH(Date fecIngSH) {
        this.fecIngSH = fecIngSH;
    }

    public Date getFecEntSH() {
        return fecEntSH;
    }

    public void setFecEntSH(Date fecEntSH) {
        this.fecEntSH = fecEntSH;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Boolean getEstaReingresado() {
        return estaReingresado;
    }

    public void setEstaReingresado(Boolean estaReingresado) {
        this.estaReingresado = estaReingresado;
    }

    public String getActos() {
        return actos;
    }

    public void setActos(String actos) {
        this.actos = actos;
    }

    public BigInteger getNumActos() {
        return numActos;
    }

    public void setNumActos(BigInteger numActos) {
        this.numActos = numActos;
    }

    public Integer getRevisiones() {
        return revisiones;
    }

    public void setRevisiones(Integer revisiones) {
        this.revisiones = revisiones;
    }
   
}
