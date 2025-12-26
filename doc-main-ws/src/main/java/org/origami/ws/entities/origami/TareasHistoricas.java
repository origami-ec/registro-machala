package org.origami.ws.entities.origami;

import org.origami.ws.util.Utility;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "tarea_historica")
public class TareasHistoricas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "historico_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramites tramite;
    private Long numTramite;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "nombre_solicitante")
    private String nombreSolicitante;
    @Column(name = "ci_ruc_solicitante")
    private String ciRucSolicitante;
    @Column(name = "id_proceso_temp")
    private String idProcesoTemp;
    private Boolean entregado;
    private Boolean blocked;
    @JoinColumn(name = "id_tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramite idTipoTramite;
    private String descripcion;
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "proc_inst_id")
    private String procInstId;
    @Column(name = "task_def_key")
    private String taskDefKey;
    private String name_;
    private String assignee;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "form_key")
    private String formKey;
    private Integer priority;
    private String candidate;
    private String codigo;
    private Boolean terminado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    private Short periodo;
    private String campoReferencia;
    @Transient
    //@Formula(value = Constantes.tareaHistoricaEnd)
    private String endTimeString;
    @Transient
    ///@Formula(value = Constantes.tareaHistoricaStart)
    private String startTimeString;
    @Transient
    private Long valorPendienteFinalizado;
    @Transient
    private String nombreCompleto;

    public TareasHistoricas() {
    }

    public TareasHistoricas(TipoTramite idTipoTramite, String assignee, Boolean terminado, Short periodo, Long valor) {
        this.idTipoTramite = idTipoTramite;
        this.assignee = assignee;
        this.terminado = terminado;
        this.periodo = periodo;
        this.valorPendienteFinalizado = valor;
    }

    public Long getValorPendienteFinalizado() {
        return valorPendienteFinalizado;
    }

    public void setValorPendienteFinalizado(Long valorPendienteFinalizado) {
        this.valorPendienteFinalizado = valorPendienteFinalizado;
    }

    public TareasHistoricas(Long id) {
        this.id = id;
    }

    public String getEndTimeString() {

        if (endTime != null) {
            endTimeString = Utility.dateFormatPattern("yyyy-MM-dd HH:mm", endTime);
        }
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getStartTimeString() {

        if (createTime != null) {
            startTimeString = Utility.dateFormatPattern("yyyy-MM-dd HH:mm", createTime);
        }
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

    public void setIdTipoTramite(TipoTramite idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public TipoTramite getIdTipoTramite() {
        return idTipoTramite;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    @Override
    public String toString() {
        return "TareasHistoricas{" +
                "id=" + id +
                '}';
    }
}
