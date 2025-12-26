/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;

public class PubSolicitud implements Serializable {

    

    private Long id;
    private HistoricoTramites tramite;
    private String solApellidos;
    private String solNombres;
    private String solCedula;
    private String solDireccion;
    private String sector;
    private String solParroquia;
    private String manzana;
    private String solCelular;
    private String solConvencional;
    private String solCorreo;
    private String otroMotivo;
    private Integer numeroFicha;
    private String observacion;
    private CtlgItem tipoBien;
    private CtlgItem tipoCertificado;
    private String estado;
    private Double total;
    private Boolean tipoPago;
    private String solEstadoCivil;
    private String codigoVerificacion;
    private Integer banco;
    private String codigoComprobante;
    private Long idSolicitudVentanilla;
    private String bancoDesde;
    private String propietarioCuenta;
    private String numeroCuenta;
    private String fechaTransferencia;

    public PubSolicitud() {
    }

    public PubSolicitud(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public String getSolApellidos() {
        return solApellidos;
    }

    public void setSolApellidos(String solApellidos) {
        this.solApellidos = solApellidos;
    }

    public String getSolNombres() {
        return solNombres;
    }

    public void setSolNombres(String solNombres) {
        this.solNombres = solNombres;
    }

    public String getSolCedula() {
        return solCedula;
    }

    public void setSolCedula(String solCedula) {
        this.solCedula = solCedula;
    }

    public String getSolDireccion() {
        return solDireccion;
    }

    public void setSolDireccion(String solDireccion) {
        this.solDireccion = solDireccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSolParroquia() {
        return solParroquia;
    }

    public void setSolParroquia(String solParroquia) {
        this.solParroquia = solParroquia;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getSolCelular() {
        return solCelular;
    }

    public void setSolCelular(String solCelular) {
        this.solCelular = solCelular;
    }

    public String getSolConvencional() {
        return solConvencional;
    }

    public void setSolConvencional(String solConvencional) {
        this.solConvencional = solConvencional;
    }

    public String getSolCorreo() {
        return solCorreo;
    }

    public void setSolCorreo(String solCorreo) {
        this.solCorreo = solCorreo;
    }

    public String getOtroMotivo() {
        return otroMotivo;
    }

    public void setOtroMotivo(String otroMotivo) {
        this.otroMotivo = otroMotivo;
    }

    public Integer getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Integer numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CtlgItem getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(CtlgItem tipoBien) {
        this.tipoBien = tipoBien;
    }

    public CtlgItem getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(CtlgItem tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Boolean tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getSolEstadoCivil() {
        return solEstadoCivil;
    }

    public void setSolEstadoCivil(String solEstadoCivil) {
        this.solEstadoCivil = solEstadoCivil;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public Long getIdSolicitudVentanilla() {
        return idSolicitudVentanilla;
    }

    public void setIdSolicitudVentanilla(Long idSolicitudVentanilla) {
        this.idSolicitudVentanilla = idSolicitudVentanilla;
    }

    public String getBancoDesde() {
        return bancoDesde;
    }

    public void setBancoDesde(String bancoDesde) {
        this.bancoDesde = bancoDesde;
    }

    public String getPropietarioCuenta() {
        return propietarioCuenta;
    }

    public void setPropietarioCuenta(String propietarioCuenta) {
        this.propietarioCuenta = propietarioCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(String fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    @Override
    public String toString() {
        return "PubSolicitud{" + "id=" + id + ", tramite=" + tramite + ", solApellidos=" + solApellidos
                + ", solNombres=" + solNombres + ", solCedula=" + solCedula + ", solDireccion=" + solDireccion
                + ", sector=" + sector + ", solParroquia=" + solParroquia + ", manzana=" + manzana
                + ", solCelular=" + solCelular + ", solConvencional=" + solConvencional + ", solCorreo="
                + solCorreo + ", otroMotivo=" + otroMotivo + ", numeroFicha="
                + numeroFicha + ", observacion=" + observacion + ", tipoBien=" + tipoBien + ", tipoCertificado=" + tipoCertificado
                + ", estado=" + estado + ", total=" + total + ", tipoPago=" + tipoPago + ", solEstadoCivil=" + solEstadoCivil
                + ", codigoVerificacion=" + codigoVerificacion + ", banco=" + banco + ", codigoComprobante=" + codigoComprobante
                + ", idSolicitudVentanilla=" + idSolicitudVentanilla + ", bancoDesde=" + bancoDesde + ", propietarioCuenta=" + propietarioCuenta
                + ", numeroCuenta=" + numeroCuenta + ", fechaTransferencia=" + fechaTransferencia + '}';
    }

}
