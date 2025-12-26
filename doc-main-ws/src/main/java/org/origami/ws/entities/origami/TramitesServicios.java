package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(schema = "public", name = "tramite_servicio")
public class TramitesServicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numTramite;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramites tramite;
    @JoinColumn(name = "id_tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramite idTipoTramite;
    @JoinColumn(name = "solicitud_servicio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SolicitudServicios solicitudServicios;
    private String procInstId;
    private String procDefId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    private String deleteReason;
    private BigInteger duration;
    private String callProcInstId;
    private String participants;
    private Short periodo;
    private Boolean terminado;
    private String campoReferencia;
    //PARA OBTENER EL VALOR DE LOS TERMINADOS O PENDIENTES
    @Transient
    private Long value;
    @Transient
    private Long pendientes;
    @Transient
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
    public String toString() {
        return "tramitesServicios{" +
                "id=" + id +
                ", numTramite=" + numTramite +
                ", tramite=" + tramite +
                ", idTipoTramite=" + idTipoTramite +
                ", solicitudServicios=" + solicitudServicios +
                ", procInstId='" + procInstId + '\'' +
                ", procDefId='" + procDefId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", deleteReason='" + deleteReason + '\'' +
                ", duration=" + duration +
                ", callProcInstId='" + callProcInstId + '\'' +
                ", participants='" + participants + '\'' +
                ", periodo=" + periodo +
                ", terminado=" + terminado +
                ", value=" + value +
                ", pendientes=" + pendientes +
                ", terminados=" + terminados +
                '}';
    }
}
