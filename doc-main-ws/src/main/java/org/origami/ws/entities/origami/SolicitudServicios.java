package org.origami.ws.entities.origami;

import org.origami.ws.entities.security.Usuario;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(schema = "public", name = "solicitud_servicio")
public class SolicitudServicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String representante;
    private String campoReferencia;
    private String descripcionInconveniente;
    private Boolean solicitudInterna;
    private String informe;
    private Date fechaCreacion;
    private Boolean asignado;
    private String asignados;
    private String status;
    private String notaGuia;
    private Date fechaMaximaRespuesta;
    private Integer prioridad;
    private String asunto;
    private String numOficio;
    private String tipoPrioridad;
    private Long usuarioCreacion;
    @JoinColumn(name = "tipo_servicio_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiciosDepartamento tipoServicio;
    @JoinColumn(name = "tramite_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramites tramite;
    @JoinColumn(name = "tipo_tramite_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramite tipoTramite;
    @JoinColumn(name = "ente_solicitante_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Persona enteSolicitante;
    @Transient
    private List<SolicitudDocumentos> documentos;
    private Boolean finalizado;
    @JoinColumn(name = "departamento_id")
    @ManyToOne
    private Departamento departamento;
    private String correoNotificacion;
    @Transient
    private Usuario usuarioIngreso;

    public SolicitudServicios() {
    }

    public Usuario getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(Usuario usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Long getUsuarioCreacion() {
        if (usuarioIngreso != null) {
            usuarioCreacion = usuarioIngreso.getId();
        }
        return usuarioCreacion;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public SolicitudServicios(Long id) {
        this.id = id;
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


    public String getDescripcionInconveniente() {
        return descripcionInconveniente;
    }

    public void setDescripcionInconveniente(String descripcionInconveniente) {
        this.descripcionInconveniente = descripcionInconveniente;
    }


    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
    }

    public Persona getEnteSolicitante() {
        return enteSolicitante;
    }

    public void setEnteSolicitante(Persona enteSolicitante) {
        this.enteSolicitante = enteSolicitante;
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


    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
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

    public ServiciosDepartamento getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(ServiciosDepartamento tipoServicio) {
        this.tipoServicio = tipoServicio;
    }


    public List<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitudServicios servicios = (SolicitudServicios) o;
        return id.equals(servicios.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SolicitudServicios{" +
                "id=" + id +
                ", representante='" + representante + '\'' +
                ", descripcionInconveniente='" + descripcionInconveniente + '\'' +
                ", solicitudInterna=" + solicitudInterna +
                ", informe='" + informe + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", asignado=" + asignado +
                ", asignados='" + asignados + '\'' +
                ", status='" + status + '\'' +
                ", notaGuia='" + notaGuia + '\'' +
                ", fechaMaximaRespuesta=" + fechaMaximaRespuesta +
                ", prioridad=" + prioridad +
                ", asunto='" + asunto + '\'' +
                ", numOficio='" + numOficio + '\'' +
                ", tipoPrioridad='" + tipoPrioridad + '\'' +
                ", tipoServicio=" + tipoServicio +
                ", usuarioIngreso=" + usuarioIngreso +
                ", tramite=" + tramite +
                ", tipoTramite=" + tipoTramite +
                ", enteSolicitante=" + enteSolicitante +
                ", documentos=" + documentos +
                ",finalizado=" + finalizado +
                '}';
    }
}
