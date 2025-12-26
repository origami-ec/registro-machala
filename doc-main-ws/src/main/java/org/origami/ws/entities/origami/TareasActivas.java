package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public", name = "tarea_activa")
public class TareasActivas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "num_tramite")
    private Integer numTramite;
    private Integer secuencial;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_entrega")
    private Date fechaEntrega;
    @Column(name = "nombre_solicitante")
    private String nombreSolicitante;
    @Column(name = "id_proceso_temp")
    private String idProcesoTemp;
    private Boolean entregado;
    private Boolean blocked;
    @Column(name = "carpeta_rep")
    private String carpetaRep;
    /* private Long idTipoTramite;*/
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
    private Integer rev_;
    @Column(name = "ci_ruc_solicitante")
    private String ciRucSolicitante;
    private String codigo;
    private String correo;
    private Boolean notificacion;
    @Column(name = "id_tipo_tramite")
    private Long idTipoTramite;
    private String asunto;
    private String campoReferencia;

    public TareasActivas() {
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public TareasActivas(Long id) {
        this.id = id;
    }

    public TareasActivas(Integer numTramite) {
        this.numTramite = numTramite;
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

    public void setDescripcion(String decripcion) {
        this.descripcion = decripcion;
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

    public Boolean getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
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
}
