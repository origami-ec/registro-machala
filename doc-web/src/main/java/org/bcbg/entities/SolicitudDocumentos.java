/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ORIGAMI1
 */
public class SolicitudDocumentos implements Serializable {

    private Long id;
    private String codigoVerificacion;
    private String nombreArchivo;
    private String tipoArchivo;
    private String rutaArchivo;
    private Date fechaCreacion;
    private String usuario;
    private String estado;
    private Boolean tieneFirmaElectronica;
    private SolicitudServicios solicitud;
    private ServiciosDepartamentoRequisitos requisito;
    private String descripcion;
    private Boolean check = Boolean.FALSE;
    private Boolean rechazado;
    private Departamento departamento;
    private Boolean tieneQr;

    public SolicitudDocumentos() {
        this.rechazado = Boolean.FALSE;
        this.tieneQr = Boolean.FALSE;
    }

    public SolicitudDocumentos(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public SolicitudDocumentos(SolicitudServicios solicitud, ServiciosDepartamentoRequisitos requisito) {
        this.solicitud = solicitud;
        this.requisito = requisito;
    }

    public Boolean getTieneQr() {
        return tieneQr;
    }

    public void setTieneQr(Boolean tieneQr) {
        this.tieneQr = tieneQr;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Boolean getRechazado() {
        return rechazado;
    }

    public void setRechazado(Boolean rechazado) {
        this.rechazado = rechazado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public SolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public Boolean getTieneFirmaElectronica() {
        return tieneFirmaElectronica;
    }

    public void setTieneFirmaElectronica(Boolean tieneFirmaElectronica) {
        this.tieneFirmaElectronica = tieneFirmaElectronica;
    }

    public ServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(ServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudDocumentos)) {
            return false;
        }
        SolicitudDocumentos other = (SolicitudDocumentos) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "SolicitudDocumentos{" + "id=" + id + "}";
    }

}
