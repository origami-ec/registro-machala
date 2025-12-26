package org.origami.ws.dto;

import java.util.Date;

public class HistoricoTramitesDTO {

    private Long id;
    private Long numTramite;
    private String idProceso;
    private String idProcesoTemp;
    private String nombreSolicitante;
    private Date fecha;
    private String carpetaRep;
    private Boolean entregado;
    private CatEnteDTO solicitante;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private Date fechaNotificacion;

    private CatEnteDTO usuarioRetiro;
    private Date fechaRetiro;
    private Boolean blocked;
    private Long userDesblock;
    private Date fechaDesblock;
    private Boolean documento;
    private String observacion;
    private String tarea;
    private String nameUser;
    private String codigo;
    private Boolean notificacion;
    private String correoNotificacion;
    private Long secuencial;

    public HistoricoTramitesDTO() {
    }

    public Long getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Long secuencial) {
        this.secuencial = secuencial;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
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

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCarpetaRep() {
        return carpetaRep;
    }

    public void setCarpetaRep(String carpetaRep) {
        this.carpetaRep = carpetaRep;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public CatEnteDTO getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnteDTO solicitante) {
        this.solicitante = solicitante;
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

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public CatEnteDTO getUsuarioRetiro() {
        return usuarioRetiro;
    }

    public void setUsuarioRetiro(CatEnteDTO usuarioRetiro) {
        this.usuarioRetiro = usuarioRetiro;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Long getUserDesblock() {
        return userDesblock;
    }

    public void setUserDesblock(Long userDesblock) {
        this.userDesblock = userDesblock;
    }

    public Date getFechaDesblock() {
        return fechaDesblock;
    }

    public void setFechaDesblock(Date fechaDesblock) {
        this.fechaDesblock = fechaDesblock;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
        this.notificacion = notificacion;
    }
}
