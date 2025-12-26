package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public", name = "historico_tramite")
public class HistoricoTramites implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numTramite;
    private String idProceso;
    private String idProcesoTemp;
    private String nombreSolicitante;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "carpeta_rep")
    private String carpetaRep;
    private Boolean entregado;
    @JoinColumn(name = "solicitante_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Persona solicitante;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    private Date fechaEntrega;
    private Date fechaNotificacion;
    @JoinColumn(name = "tipo_tramite_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramite tipoTramite;
    private Date fechaRetiro;
    private Boolean blocked;
    private Long userDesblock;
    private Date fechaDesblock;
    private Boolean documento;
    @Transient
    private String observacion;
    @Transient
    private String tarea;
    @Transient
    private String nameUser;
    private String codigo;
    private Boolean notificacion;
    private Long secuencial;
    private String correoNotificacion;


    public HistoricoTramites() {
    }

    public HistoricoTramites(Long id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public HistoricoTramites(String idProceso) {
        this.idProceso = idProceso;
    }

    public HistoricoTramites(Long id, Long numTramite, String codigo) {
        this.id = id;
        this.numTramite = numTramite;
        this.codigo = codigo;
    }

    public HistoricoTramites(Long id, Long numTramite, String idProceso, String idProcesoTemp,
                             String nombreSolicitante,
                             Date fecha, String carpetaRep, Boolean entregado,
                             Date fechaIngreso, Date fechaEntrega, Date fechaNotificacion,
                             Date fechaRetiro, Boolean blocked, Long userDesblock, Date fechaDesblock,
                             Boolean documento, String codigo,
                             Boolean notificacion, Long secuencial,
                             Long idTipoTramite, String descripcionTipoTramite, String activitykeyTipoTramite,
                             Boolean estadoTipoTramite,
                             String archivoBpmnTipoTramite, String abreviaturaTipoTramite,
                             Integer diasTipoTramite, Integer horasTipoTramite, Integer minutosTipoTramite,
                             Integer segundosTipoTramite,
                             Boolean internoTipoTramite, String urlImagenTipoTramite, Long idDepTipoTramite,
                             String nombreDepTipoTramite,
                             Boolean estadoDepTipoTramite, String urlImagenDepTipoTramite, String codigoDep,
                             Long idSol, String ciRucSol, String nombresSol, String apellidosSol, Boolean esPersonaSol, String direccionSol, Date fechaNacimientoSol,
                             String estadoSol, String userCreSol, Date fechaCreSol, String userModSol, Date fechaModSol, String nombreComercialSol, String razonSocialSol,
                             String telefonoSol, String celularSol, String correoSol, String tipoIdentificacionSol,
                             String tituloProfSol, Long representanteLegalSol, Boolean sexoSol, String definicionTramite, String colorTramite) {
        this.id = id;
        this.numTramite = numTramite;
        this.idProceso = idProceso;
        this.idProcesoTemp = idProcesoTemp;
        this.nombreSolicitante = nombreSolicitante;
        this.fecha = fecha;
        this.carpetaRep = carpetaRep;
        this.entregado = entregado;
        this.solicitante = new Persona(idSol, ciRucSol, nombresSol, apellidosSol, esPersonaSol, direccionSol, fechaNacimientoSol,
                estadoSol, userCreSol, fechaCreSol, userModSol, fechaModSol, nombreComercialSol, razonSocialSol,
                telefonoSol, celularSol, correoSol, tipoIdentificacionSol, tituloProfSol, representanteLegalSol, sexoSol);
        this.fechaIngreso = fechaIngreso;
        this.fechaEntrega = fechaEntrega;
        this.fechaNotificacion = fechaNotificacion;
        this.tipoTramite = new TipoTramite(idTipoTramite, descripcionTipoTramite,
                activitykeyTipoTramite, estadoTipoTramite, archivoBpmnTipoTramite,
                abreviaturaTipoTramite, diasTipoTramite, horasTipoTramite, minutosTipoTramite, segundosTipoTramite, internoTipoTramite,
                urlImagenTipoTramite, idDepTipoTramite, nombreDepTipoTramite,  estadoDepTipoTramite, codigoDep, definicionTramite, colorTramite);
        this.fechaRetiro = fechaRetiro;
        this.blocked = blocked;
        this.userDesblock = userDesblock;
        this.fechaDesblock = fechaDesblock;
        this.documento = documento;
        this.codigo = codigo;
        this.notificacion = notificacion;
        this.secuencial = secuencial;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }

    public HistoricoTramites(Long id) {
        this.id = id;
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

    public Persona getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Persona solicitante) {
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

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
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

    public Long getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Long secuencial) {
        this.secuencial = secuencial;
    }

    @Override
    public String toString() {
        return "HistoricoTramites{" +
                "id=" + id +
                ", numTramite=" + numTramite +
                ", idProceso='" + idProceso + '\'' +
                ", idProcesoTemp='" + idProcesoTemp + '\'' +
                ", nombreSolicitante='" + nombreSolicitante + '\'' +
                ", fecha=" + fecha +
                ", carpetaRep='" + carpetaRep + '\'' +
                ", entregado=" + entregado +
                ", solicitante=" + solicitante +
                ", fechaIngreso=" + fechaIngreso +
                ", fechaEntrega=" + fechaEntrega +
                ", fechaNotificacion=" + fechaNotificacion +
                ", tipoTramite=" + tipoTramite +
                ", fechaRetiro=" + fechaRetiro +
                ", blocked=" + blocked +
                ", userDesblock=" + userDesblock +
                ", fechaDesblock=" + fechaDesblock +
                ", documento=" + documento +
                '}';
    }
}
