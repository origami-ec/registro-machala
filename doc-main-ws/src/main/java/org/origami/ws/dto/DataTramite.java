package org.origami.ws.dto;

import java.util.Date;

public class DataTramite {
    private String usuario;
    private String departamento;
    private String numTramite;
    private String solicitante;
    private Date fecha;
    private String servicio;
    private String tipoServicio;
    private String observacion;
    private String asunto;

    private String campoReferencia;

    public DataTramite() {
    }

    public DataTramite(String departamento, String numTramite, String solicitante, Date fecha, String servicio,
                       String tipoServicio, String usuario,String campoReferencia) {
        this.departamento = departamento;
        this.numTramite = numTramite;
        this.solicitante = solicitante;
        this.fecha = fecha;
        this.servicio = servicio;
        this.tipoServicio = tipoServicio;
        this.usuario = usuario;
        this.campoReferencia = campoReferencia;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    @Override
    public String toString() {
        return "DataTramite{" +
                "departamento='" + departamento + '\'' +
                ", numTramite=" + numTramite +
                ", solicitante='" + solicitante + '\'' +
                ", fecha=" + fecha +
                ", servicio='" + servicio + '\'' +
                '}';
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
