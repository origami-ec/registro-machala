package org.origami.docs.model;

import java.util.Date;

public class DocumentoFirmadoDTO {
    public String _id;
    private Long referencia;
    private Long numTramite;
    private Long documento;
    private String usuario;
    private String motivo;
    private Integer posicionX1;
    private Integer posicionY1;
    private Integer numeroPagina;
    private String fecha;
    private String tipo;
    private String archivoFirmado;
    private String fechaFirma;
    /*
           Campos FirmaEc
        */
    private String estadoFirma; //Certificado revocado - Certificado caducado  - Certificado emitido por entidad certificadora
    private String uid;
    private String cn;
    private String emision;
    private Date fechaEmision;
    private Date fechaExpiracion;
    private String isuser;
    public DocumentoFirmadoDTO() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getPosicionX1() {
        return posicionX1;
    }

    public void setPosicionX1(Integer posicionX1) {
        this.posicionX1 = posicionX1;
    }

    public Integer getPosicionY1() {
        return posicionY1;
    }

    public void setPosicionY1(Integer posicionY1) {
        this.posicionY1 = posicionY1;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getArchivoFirmado() {
        return archivoFirmado;
    }

    public void setArchivoFirmado(String archivoFirmado) {
        this.archivoFirmado = archivoFirmado;
    }

    public String getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(String fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getIsuser() {
        return isuser;
    }

    public void setIsuser(String isuser) {
        this.isuser = isuser;
    }
}
