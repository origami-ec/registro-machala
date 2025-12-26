/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TipoTramite;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Ricardo
 */
public class TramitesServicios {

    private Long id;
    private Long numTramite;
    private HistoricoTramites tramite;
    private TipoTramite idTipoTramite;
    private SolicitudServicios solicitudServicios;
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
    private String campoReferencia;
    //PARA OBTENER EL VALOR DE LOS TERMINADOS O PENDIENTES
    private Long value;
    private Long pendientes;
    private Long terminados;

    public TramitesServicios() {
    }

    public TramitesServicios(Long id) {
        this.id = id;
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

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
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

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
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
        if (!(object instanceof TramitesServicios)) {
            return false;
        }
        TramitesServicios other = (TramitesServicios) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "tramitesServicios{"
                + "id=" + id
                + ", numTramite=" + numTramite
                + ", tramite=" + tramite
                + ", idTipoTramite=" + idTipoTramite
                + ", solicitudServicios=" + solicitudServicios
                + ", procInstId='" + procInstId + '\''
                + ", procDefId='" + procDefId + '\''
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + ", deleteReason='" + deleteReason + '\''
                + ", duration=" + duration
                + ", callProcInstId='" + callProcInstId + '\''
                + ", participants='" + participants + '\''
                + ", periodo=" + periodo
                + ", terminado=" + terminado
                + ", value=" + value
                + ", pendientes=" + pendientes
                + ", terminados=" + terminados
                + '}';
    }
}
