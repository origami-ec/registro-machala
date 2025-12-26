package com.origami.sgr.restful.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Anyelo
 */
public class DatosProforma implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long numerotramite;
    private Integer repertorio;
    private String doc_solicitante;
    private String nombre_solicitante;
    private String correo_solicitante;
    private String doc_beneficiario;
    private String nombre_beneficiario;
    private String correo_beneficiario;
    private String numerofactura;
    private String claveacceso;
    private String estadotramite;
    private Long fechaingreso;
    private Long fechaentrega;
    private BigDecimal totalPagar;
    private String detalleSolicitud;
    private String tareaActual;
    private String tipo_tramite;
    private Long numero_ficha;
    private String tipo_predio;
    private String codigo_catastral;
    private String estado_gravamen;
    private String tipo_certificado;
    private Integer codigo_gravamen;

    private List<String> propietarios;
    private List<DetalleContratos> contratos;

    public DatosProforma() {
    }

    public Long getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(Long numerotramite) {
        this.numerotramite = numerotramite;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public String getDoc_solicitante() {
        return doc_solicitante;
    }

    public void setDoc_solicitante(String doc_solicitante) {
        this.doc_solicitante = doc_solicitante;
    }

    public String getNombre_solicitante() {
        return nombre_solicitante;
    }

    public void setNombre_solicitante(String nombre_solicitante) {
        this.nombre_solicitante = nombre_solicitante;
    }

    public String getCorreo_solicitante() {
        return correo_solicitante;
    }

    public void setCorreo_solicitante(String correo_solicitante) {
        this.correo_solicitante = correo_solicitante;
    }

    public String getDoc_beneficiario() {
        return doc_beneficiario;
    }

    public void setDoc_beneficiario(String doc_beneficiario) {
        this.doc_beneficiario = doc_beneficiario;
    }

    public String getNombre_beneficiario() {
        return nombre_beneficiario;
    }

    public void setNombre_beneficiario(String nombre_beneficiario) {
        this.nombre_beneficiario = nombre_beneficiario;
    }

    public String getCorreo_beneficiario() {
        return correo_beneficiario;
    }

    public void setCorreo_beneficiario(String correo_beneficiario) {
        this.correo_beneficiario = correo_beneficiario;
    }

    public String getNumerofactura() {
        return numerofactura;
    }

    public void setNumerofactura(String numerofactura) {
        this.numerofactura = numerofactura;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getEstadotramite() {
        return estadotramite;
    }

    public void setEstadotramite(String estadotramite) {
        this.estadotramite = estadotramite;
    }

    public Long getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Long fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Long getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Long fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getDetalleSolicitud() {
        return detalleSolicitud;
    }

    public void setDetalleSolicitud(String detalleSolicitud) {
        this.detalleSolicitud = detalleSolicitud;
    }

    public String getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(String tareaActual) {
        this.tareaActual = tareaActual;
    }

    public String getTipo_tramite() {
        return tipo_tramite;
    }

    public void setTipo_tramite(String tipo_tramite) {
        this.tipo_tramite = tipo_tramite;
    }

    public Long getNumero_ficha() {
        return numero_ficha;
    }

    public void setNumero_ficha(Long numero_ficha) {
        this.numero_ficha = numero_ficha;
    }

    public String getTipo_predio() {
        return tipo_predio;
    }

    public void setTipo_predio(String tipo_predio) {
        this.tipo_predio = tipo_predio;
    }

    public String getCodigo_catastral() {
        return codigo_catastral;
    }

    public void setCodigo_catastral(String codigo_catastral) {
        this.codigo_catastral = codigo_catastral;
    }

    public String getEstado_gravamen() {
        return estado_gravamen;
    }

    public void setEstado_gravamen(String estado_gravamen) {
        this.estado_gravamen = estado_gravamen;
    }

    public List<String> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<String> propietarios) {
        this.propietarios = propietarios;
    }

    public String getTipo_certificado() {
        return tipo_certificado;
    }

    public void setTipo_certificado(String tipo_certificado) {
        this.tipo_certificado = tipo_certificado;
    }

    public Integer getCodigo_gravamen() {
        return codigo_gravamen;
    }

    public void setCodigo_gravamen(Integer codigo_gravamen) {
        this.codigo_gravamen = codigo_gravamen;
    }

    public List<DetalleContratos> getContratos() {
        return contratos;
    }

    public void setContratos(List<DetalleContratos> contratos) {
        this.contratos = contratos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DatosProforma{");
        sb.append("numerotramite=").append(numerotramite);
        sb.append(", repertorio=").append(repertorio);
        sb.append(", doc_solicitante=").append(doc_solicitante);
        sb.append(", nombre_solicitante=").append(nombre_solicitante);
        sb.append(", correo_solicitante=").append(correo_solicitante);
        sb.append(", doc_beneficiario=").append(doc_beneficiario);
        sb.append(", nombre_beneficiario=").append(nombre_beneficiario);
        sb.append(", correo_beneficiario=").append(correo_beneficiario);
        sb.append(", numerofactura=").append(numerofactura);
        sb.append(", claveacceso=").append(claveacceso);
        sb.append(", estadotramite=").append(estadotramite);
        sb.append(", fechaingreso=").append(fechaingreso);
        sb.append(", fechaentrega=").append(fechaentrega);
        sb.append(", totalPagar=").append(totalPagar);
        sb.append(", detalleSolicitud=").append(detalleSolicitud);
        sb.append(", tareaActual=").append(tareaActual);
        sb.append(", tipo_tramite=").append(tipo_tramite);
        sb.append(", numero_ficha=").append(numero_ficha);
        sb.append(", tipo_predio=").append(tipo_predio);
        sb.append(", codigo_catastral=").append(codigo_catastral);
        sb.append(", estado_gravamen=").append(estado_gravamen);
        sb.append(", tipo_certificado=").append(tipo_certificado);
        sb.append(", codigo_gravamen=").append(codigo_gravamen);
        sb.append(", propietarios=").append(propietarios);
        sb.append(", contratos=").append(contratos);
        sb.append('}');
        return sb.toString();
    }

}
