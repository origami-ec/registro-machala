package org.origami.ws.dto;

import org.origami.ws.entities.origami.TipoTramite;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.models.UserTask;

import java.util.Date;
import java.util.List;

public class SolicitudServiciosDTO {

    private Long id;
    private String representante;
    private Date fechaInconveniente;
    private String descripcionInconveniente;
    private String notificacionDep;
    private Boolean solicitudInterna;
    private String informe;
    private Date fechaCreacion;
    private Boolean asignado;
    private String asignados;
    private String status;
    private Boolean archivar;
    private Boolean notificar;
    private String notificacion;
    private String notaGuia;
    private Date fechaMaximaRespuesta;
    private Integer prioridad;
    /**
     * Campos para permisos de construccion
     */
    private String tipoPermisoConstruccion; //APROBACION PLANOS - AMBOS
    private Double areaContruccion;
    private String registroMunicipal;
    private String tipoProyecto;
    private Long usuarioCreacion;
    private ServiciosDepartamentoItemsDTO tipoServicio;
    private Usuario usuarioIngreso;
    private ServiciosDepartamentoItemsDTO lugarAudiencia;
    private HistoricoTramitesDTO tramite;
    private TipoTramite tipoTramite;
    private CatEnteDTO enteSolicitante;
    private List<SolicitudDocumentosDTO> documentos;
    private String cadastralCode;
    private String tipoAsesoria;
    private String asunto;
    private String numOficio;
    private String tipoPrioridad;

    private Date fechaInspeccionEstudiosg;
    private Boolean finalizado;
    private String permisoConstruccion;
    private DepartamentoDTO departamento;
    private List<UserTask> usersTasks;
    private String correoNotificacion;

    private String fecha; //ESTO ES FECHA DESDE

    private String fecha1;//ESTO ES FECHA HASTA

    private String campoReferencia;

    public SolicitudServiciosDTO() {
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

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    public String getPermisoConstruccion() {
        return permisoConstruccion;
    }

    public void setPermisoConstruccion(String permisoConstruccion) {
        this.permisoConstruccion = permisoConstruccion;
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

    public String getTipoAsesoria() {
        return tipoAsesoria;
    }

    public void setTipoAsesoria(String tipoAsesoria) {
        this.tipoAsesoria = tipoAsesoria;
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

    public Date getFechaInconveniente() {
        return fechaInconveniente;
    }

    public void setFechaInconveniente(Date fechaInconveniente) {
        this.fechaInconveniente = fechaInconveniente;
    }

    public String getDescripcionInconveniente() {
        return descripcionInconveniente;
    }

    public void setDescripcionInconveniente(String descripcionInconveniente) {
        this.descripcionInconveniente = descripcionInconveniente;
    }

    public String getNotificacionDep() {
        return notificacionDep;
    }

    public void setNotificacionDep(String notificacionDep) {
        this.notificacionDep = notificacionDep;
    }

    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
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

    public Boolean getArchivar() {
        return archivar;
    }

    public void setArchivar(Boolean archivar) {
        this.archivar = archivar;
    }

    public Boolean getNotificar() {
        return notificar;
    }

    public void setNotificar(Boolean notificar) {
        this.notificar = notificar;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
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

    public ServiciosDepartamentoItemsDTO getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(ServiciosDepartamentoItemsDTO tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Usuario getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(Usuario usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public ServiciosDepartamentoItemsDTO getLugarAudiencia() {
        return lugarAudiencia;
    }

    public void setLugarAudiencia(ServiciosDepartamentoItemsDTO lugarAudiencia) {
        this.lugarAudiencia = lugarAudiencia;
    }

    public HistoricoTramitesDTO getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramitesDTO tramite) {
        this.tramite = tramite;
    }

    public CatEnteDTO getEnteSolicitante() {
        return enteSolicitante;
    }

    public void setEnteSolicitante(CatEnteDTO enteSolicitante) {
        this.enteSolicitante = enteSolicitante;
    }

    public List<SolicitudDocumentosDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<SolicitudDocumentosDTO> documentos) {
        this.documentos = documentos;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getCadastralCode() {
        return cadastralCode;
    }

    public void setCadastralCode(String cadastralCode) {
        this.cadastralCode = cadastralCode;
    }

    public String getTipoPermisoConstruccion() {
        return tipoPermisoConstruccion;
    }

    public void setTipoPermisoConstruccion(String tipoPermisoConstruccion) {
        this.tipoPermisoConstruccion = tipoPermisoConstruccion;
    }

    public Double getAreaContruccion() {
        return areaContruccion;
    }

    public void setAreaContruccion(Double areaContruccion) {
        this.areaContruccion = areaContruccion;
    }

    public String getRegistroMunicipal() {
        return registroMunicipal;
    }

    public void setRegistroMunicipal(String registroMunicipal) {
        this.registroMunicipal = registroMunicipal;
    }

    public String getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(String tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public Date getFechaInspeccionEstudiosg() {
        return fechaInspeccionEstudiosg;
    }

    public void setFechaInspeccionEstudiosg(Date fechaInspeccionEstudiosg) {
        this.fechaInspeccionEstudiosg = fechaInspeccionEstudiosg;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    @Override
    public String toString() {
        return "SolicitudServiciosDTO{" +
                "id=" + id +
                ", representante='" + representante + '\'' +
                ", fechaInconveniente=" + fechaInconveniente +
                ", descripcionInconveniente='" + descripcionInconveniente + '\'' +
                ", notificacionDep='" + notificacionDep + '\'' +
                ", solicitudInterna=" + solicitudInterna +
                ", informe='" + informe + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", asignado=" + asignado +
                ", asignados='" + asignados + '\'' +
                ", status='" + status + '\'' +
                ", archivar=" + archivar +
                ", notificar=" + notificar +
                ", notificacion='" + notificacion + '\'' +
                ", notaGuia='" + notaGuia + '\'' +
                ", fechaMaximaRespuesta=" + fechaMaximaRespuesta +
                ", prioridad=" + prioridad +
                ", tipoPermisoConstruccion='" + tipoPermisoConstruccion + '\'' +
                ", areaContruccion=" + areaContruccion +
                ", registroMunicipal='" + registroMunicipal + '\'' +
                ", tipoProyecto='" + tipoProyecto + '\'' +
                ", usuarioCreacion=" + usuarioCreacion +
                ", tipoServicio=" + tipoServicio +
                ", usuarioIngreso=" + usuarioIngreso +
                ", lugarAudiencia=" + lugarAudiencia +
                ", tramite=" + tramite +
                ", tipoTramite=" + tipoTramite +
                ", enteSolicitante=" + enteSolicitante +
                ", documentos=" + documentos +
                ", cadastralCode='" + cadastralCode + '\'' +
                ", tipoAsesoria='" + tipoAsesoria + '\'' +
                ", asunto='" + asunto + '\'' +
                ", numOficio='" + numOficio + '\'' +
                ", tipoPrioridad='" + tipoPrioridad + '\'' +
                ", fechaInspeccionEstudiosg=" + fechaInspeccionEstudiosg +
                ", finalizado=" + finalizado +
                ", permisoConstruccion='" + permisoConstruccion + '\'' +
                ", departamento=" + departamento +
                ", usersTasks=" + usersTasks +
                ", correoNotificacion='" + correoNotificacion + '\'' +
                '}';
    }
}
