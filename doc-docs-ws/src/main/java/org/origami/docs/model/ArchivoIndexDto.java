package org.origami.docs.model;

import java.util.List;

public class ArchivoIndexDto {
    private Long referenciaId;
    private String referencia;
    private String tramite;
    private String numTramite;
    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private String formatoUpload;
    private List<ArchivoIndexCampoDto> detalles;

    public ArchivoIndexDto() {
    }

    public String getTipoIndexacion() {
        return tipoIndexacion;
    }

    public void setTipoIndexacion(String tipoIndexacion) {
        this.tipoIndexacion = tipoIndexacion;
    }

    public String getDetalleDocumento() {
        return detalleDocumento;
    }

    public void setDetalleDocumento(String detalleDocumento) {
        this.detalleDocumento = detalleDocumento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<ArchivoIndexCampoDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ArchivoIndexCampoDto> detalles) {
        this.detalles = detalles;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getFormatoUpload() {
        return formatoUpload;
    }

    public void setFormatoUpload(String formatoUpload) {
        this.formatoUpload = formatoUpload;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
