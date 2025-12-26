/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.model;

/**
 *
 * @author EDWIN
 */
public class FirmaDocumento {

    private Long id;
    private Long referencia;
    private Long documento;
    private String usuario;
    private String motivo;
    private String archivo;
    private String clave;
    private String archivoFirmado;
    private String estado;
    private Integer posicionX1;
    private Integer posicionY1;
    private Integer numeroPagina;
    private String subirArchivo;

    public FirmaDocumento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getArchivoFirmado() {
        return archivoFirmado;
    }

    public void setArchivoFirmado(String archivoFirmado) {
        this.archivoFirmado = archivoFirmado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getSubirArchivo() {
        return subirArchivo;
    }

    public void setSubirArchivo(String subirArchivo) {
        this.subirArchivo = subirArchivo;
    }

    @Override
    public String toString() {
        return "DocumentoLocal{" + "id=" + id + ", referencia=" + referencia + ", documento=" + documento + ", usuario=" + usuario + ", motivo=" + motivo + ", archivo=" + archivo + ", clave=" + clave + '}';
    }

}
