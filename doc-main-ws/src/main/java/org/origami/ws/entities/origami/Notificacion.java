package org.origami.ws.entities.origami;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private Integer secuencia;
    private Integer anio;
    private String codigo;
    private Date fecha;
    private String contenido;
    private String color;
    private Date fechaRevision;
    private String observacion;
    private Boolean revisada;
    private String titulo;
    private Boolean aprobado;
    @JoinColumn(name = "tipo_notificacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoNotificacion tipoNotificacion;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramites tramite;
//    @JoinColumn(name = "usuario", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Usuario usuario;

    @JoinColumn(name = "solicitud_servicio", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private SolicitudServicios solicitudServicio;
    private String respuesta;

    public Notificacion() {
    }

    public Notificacion(Long id) {
        this.id = id;
    }

    public Notificacion(Long id, Integer anio, String codigo, Date fecha,
                        String contenido, String color, Date fechaRevision, String observacion,
                        Boolean revisada, String titulo, String respuesta,
                        Long idTramite, String codigoTramite, Long tipoNotificacionId) {
        this.id = id;
        this.anio = anio;
        this.codigo = codigo;
        this.fecha = fecha;
        this.contenido = contenido.replace("<br>", "");
        this.color = color;
        this.fechaRevision = fechaRevision;
        this.observacion = observacion;
        this.revisada = revisada;
        this.respuesta = respuesta;
        this.titulo = titulo;
        this.tramite = new HistoricoTramites(idTramite, codigoTramite);
        this.tipoNotificacion = new TipoNotificacion(tipoNotificacionId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getRevisada() {
        return revisada;
    }

    public void setRevisada(Boolean revisada) {
        this.revisada = revisada;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    /*public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }*/

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public SolicitudServicios getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(SolicitudServicios solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", secuencia=" + secuencia +
                ", anio=" + anio +
                ", codigo='" + codigo + '\'' +
                ", fecha=" + fecha +
                ", contenido='" + contenido + '\'' +
                ", color='" + color + '\'' +
                ", fechaRevision=" + fechaRevision +
                ", observacion='" + observacion + '\'' +
                ", revisada=" + revisada +
                ", titulo='" + titulo + '\'' +
                ", aprobado=" + aprobado +
                ", tipoNotificacion=" + tipoNotificacion +
                ", tramite=" + tramite +
                ", solicitudServicio=" + solicitudServicio +
                ", respuesta='" + respuesta + '\'' +
                '}';
    }
}
