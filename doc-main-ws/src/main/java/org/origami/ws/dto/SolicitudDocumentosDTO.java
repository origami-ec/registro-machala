package org.origami.ws.dto;

import java.util.Date;

public class SolicitudDocumentosDTO {

    private Long id;
    private String codigoVerificacion;
    private String nombreArchivo;
    private String tipoArchivo;
    private String rutaArchivo;
    private Date fechaCreacion;
    private String usuario;
    private String estado;
    private Boolean tieneFirmaElectronica;
    private SolicitudServiciosDTO solicitud;
    private ServiciosDepartamentoItemsRequisitosDTO requisito;
    private String descripcion;
    private Boolean rechazado;
    private DepartamentoDTO departamento;
    private String archivoMemoVb;//campo para permiso de Construcción
    private Boolean tieneQr;


    public SolicitudDocumentosDTO() {
    }


    public Boolean getTieneQr() {
        return tieneQr;
    }

    public void setTieneQr(Boolean tieneQr) {
        this.tieneQr = tieneQr;
    }

    public Boolean getRechazado() {
        return rechazado;
    }

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
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

    public Boolean getTieneFirmaElectronica() {
        return tieneFirmaElectronica;
    }

    public void setTieneFirmaElectronica(Boolean tieneFirmaElectronica) {
        this.tieneFirmaElectronica = tieneFirmaElectronica;
    }

    public SolicitudServiciosDTO getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServiciosDTO solicitud) {
        this.solicitud = solicitud;
    }

    public ServiciosDepartamentoItemsRequisitosDTO getRequisito() {
        return requisito;
    }

    public void setRequisito(ServiciosDepartamentoItemsRequisitosDTO requisito) {
        this.requisito = requisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArchivoMemoVb() {
        return archivoMemoVb;
    }

    public void setArchivoMemoVb(String archivoMemoVb) {
        this.archivoMemoVb = archivoMemoVb;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }
}
