/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.restful.models;

import java.io.Serializable;

/**
 *
 * @author Anyelo
 */
public class DatosSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    private String solicitanteApellidos;
    private String solicitanteNombres;
    private String solicitanteCedula;
    private String solicitanteDireccion;
    private String solicitanteCorreo;
    private String solicitanteCelular;

    private String beneficiarioApellidos;
    private String beneficiarioNombres;
    private String beneficiarioCedula;
    private String beneficiarioDireccion;
    private String beneficiarioCorreo;
    private String beneficiarioCelular;
    private Integer numeroReferencia;
    private String numeroComprobante;
    private String informacionAdicional;

    private String fechaSolicitud;
    private Integer tipoCertificado;
    private Integer numeroFicha;
    private Long usoDocumento;
    private Long cantidad;
    private Long anioInscripcion;
    private Long numeroInscripcion;
    private Long ciudadela;
    private String manzana;
    private String solar;
    private String observacion;

    private Long numTramite;
    private Long idTarea;
    private String nombreCompletoSolicitante;
    private String nombreCompletoPropietario;

    public DatosSolicitud() {
    }

    public String getSolicitanteApellidos() {
        return solicitanteApellidos;
    }

    public void setSolicitanteApellidos(String solicitanteApellidos) {
        this.solicitanteApellidos = solicitanteApellidos;
    }

    public String getSolicitanteNombres() {
        return solicitanteNombres;
    }

    public void setSolicitanteNombres(String solicitanteNombres) {
        this.solicitanteNombres = solicitanteNombres;
    }

    public String getSolicitanteCedula() {
        return solicitanteCedula;
    }

    public void setSolicitanteCedula(String solicitanteCedula) {
        this.solicitanteCedula = solicitanteCedula;
    }

    public String getSolicitanteDireccion() {
        return solicitanteDireccion;
    }

    public void setSolicitanteDireccion(String solicitanteDireccion) {
        this.solicitanteDireccion = solicitanteDireccion;
    }

    public String getSolicitanteCorreo() {
        return solicitanteCorreo;
    }

    public void setSolicitanteCorreo(String solicitanteCorreo) {
        this.solicitanteCorreo = solicitanteCorreo;
    }

    public String getSolicitanteCelular() {
        return solicitanteCelular;
    }

    public void setSolicitanteCelular(String solicitanteCelular) {
        this.solicitanteCelular = solicitanteCelular;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Integer getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Integer tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public Integer getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Integer numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public Long getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(Long usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getAnioInscripcion() {
        return anioInscripcion;
    }

    public void setAnioInscripcion(Long anioInscripcion) {
        this.anioInscripcion = anioInscripcion;
    }

    public Long getNumeroInscripcion() {
        return numeroInscripcion;
    }

    public void setNumeroInscripcion(Long numeroInscripcion) {
        this.numeroInscripcion = numeroInscripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getBeneficiarioApellidos() {
        return beneficiarioApellidos;
    }

    public void setBeneficiarioApellidos(String beneficiarioApellidos) {
        this.beneficiarioApellidos = beneficiarioApellidos;
    }

    public String getBeneficiarioNombres() {
        return beneficiarioNombres;
    }

    public void setBeneficiarioNombres(String beneficiarioNombres) {
        this.beneficiarioNombres = beneficiarioNombres;
    }

    public String getBeneficiarioCedula() {
        return beneficiarioCedula;
    }

    public void setBeneficiarioCedula(String beneficiarioCedula) {
        this.beneficiarioCedula = beneficiarioCedula;
    }

    public String getBeneficiarioDireccion() {
        return beneficiarioDireccion;
    }

    public void setBeneficiarioDireccion(String beneficiarioDireccion) {
        this.beneficiarioDireccion = beneficiarioDireccion;
    }

    public String getBeneficiarioCorreo() {
        return beneficiarioCorreo;
    }

    public void setBeneficiarioCorreo(String beneficiarioCorreo) {
        this.beneficiarioCorreo = beneficiarioCorreo;
    }

    public String getBeneficiarioCelular() {
        return beneficiarioCelular;
    }

    public void setBeneficiarioCelular(String beneficiarioCelular) {
        this.beneficiarioCelular = beneficiarioCelular;
    }

    public Integer getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(Integer numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public Long getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(Long ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getSolar() {
        return solar;
    }

    public void setSolar(String solar) {
        this.solar = solar;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreCompletoSolicitante() {
        return nombreCompletoSolicitante;
    }

    public void setNombreCompletoSolicitante(String nombreCompletoSolicitante) {
        this.nombreCompletoSolicitante = nombreCompletoSolicitante;
    }

    public String getNombreCompletoPropietario() {
        return nombreCompletoPropietario;
    }

    public void setNombreCompletoPropietario(String nombreCompletoPropietario) {
        this.nombreCompletoPropietario = nombreCompletoPropietario;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DatosSolicitud{");
        sb.append("solicitanteApellidos=").append(solicitanteApellidos);
        sb.append(", solicitanteNombres=").append(solicitanteNombres);
        sb.append(", solicitanteCedula=").append(solicitanteCedula);
        sb.append(", solicitanteDireccion=").append(solicitanteDireccion);
        sb.append(", solicitanteCorreo=").append(solicitanteCorreo);
        sb.append(", solicitanteCelular=").append(solicitanteCelular);
        sb.append(", fechaSolicitud=").append(fechaSolicitud);
        sb.append(", tipoCertificado=").append(tipoCertificado);
        sb.append(", numeroFicha=").append(numeroFicha);
        sb.append(", beneficiarioApellidos=").append(beneficiarioApellidos);
        sb.append(", beneficiarioNombres=").append(beneficiarioNombres);
        sb.append(", beneficiarioCedula=").append(beneficiarioCedula);
        sb.append(", beneficiarioDireccion=").append(beneficiarioDireccion);
        sb.append(", beneficiarioCorreo=").append(beneficiarioCorreo);
        sb.append(", beneficiarioCelular=").append(beneficiarioCelular);
        sb.append(", numeroReferencia=").append(numeroReferencia);
        sb.append(", numeroComprobante=").append(numeroComprobante);
        sb.append(", informacionAdicional=").append(informacionAdicional);
        sb.append(", ciudadela=").append(ciudadela);
        sb.append(", manzana=").append(manzana);
        sb.append(", solar=").append(solar);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", idTarea=").append(idTarea);
        sb.append(", nombreCompletoSolicitante=").append(nombreCompletoSolicitante);
        sb.append(", nombreCompletoPropietario=").append(nombreCompletoPropietario);
        sb.append('}');
        return sb.toString();
    }

}
