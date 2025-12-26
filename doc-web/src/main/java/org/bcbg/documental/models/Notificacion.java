/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.models;

import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.User;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Origami
 */
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer secuencia;
    private Integer anio;
    private TipoNotificacion tipoNotificacion;
    private SolicitudServicios solicitudServicio;
    private String codigo;
    private Date fecha;
    private String contenido;
    private HistoricoTramites tramite;
    private String color;
    private Date fechaRevision;
    private String observacion;
    private Boolean revisada;
    private String titulo;
    private User usuario;
    private Boolean aprobado;
    private Integer clase;
    private String respuesta;

    public Notificacion() {
        this.contenido = "";
    }

    public Long getId() {
        return id;
    }

    public Notificacion(TipoNotificacion tipoNotificacion, HistoricoTramites historicoTramites) {
        this.tipoNotificacion = tipoNotificacion;
        this.tramite = historicoTramites;
    }

    public Notificacion(SolicitudServicios solicitudServicios, TipoNotificacion tipoNotificacion, HistoricoTramites historicoTramites) {
        this.solicitudServicio = solicitudServicios;
        this.tipoNotificacion = tipoNotificacion;
        this.tramite = historicoTramites;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getRevisada() {
        return revisada;
    }

    public void setRevisada(Boolean revisada) {
        this.revisada = revisada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public Integer getClase() {
        return clase;
    }

    public void setClase(Integer clase) {
        this.clase = clase;
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
        return "Notificacion{" + "id=" + id + ", secuencia=" + secuencia + ", anio=" + anio + ", tipoNotificacion=" + tipoNotificacion + ", codigo=" + codigo + ", fecha=" + fecha + ", contenido=" + contenido + ", tramite=" + tramite + ", color=" + color + ", fechaRevision=" + fechaRevision + ", observacion=" + observacion + ", revisada=" + revisada + ", titulo=" + titulo + ", usuario=" + usuario + '}';
    }

}
