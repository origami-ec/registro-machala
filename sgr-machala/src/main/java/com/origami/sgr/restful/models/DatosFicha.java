package com.origami.sgr.restful.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asilva
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatosFicha implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger numficha;
    private BigInteger codigopredio;
    private String ciudadela;
    private String fechaapertura;
    private String descripcion;
    private String tipopredio;
    private String codigopredial;
    private Boolean automatico;
    private Long numFicha;
    private String parroquia;
    private String fechaApertura;
    private String linderos;
    private String tipoPredio;
    private String codigoPredial;
    protected List<DatosPropietariosFicha> propietarios = new ArrayList<>();
    protected List<DatosMovimientosFicha> movimientos = new ArrayList<>();

    public DatosFicha() {

    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getLinderos() {
        return linderos;
    }

    public void setLinderos(String linderos) {
        this.linderos = linderos;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getCodigoPredial() {
        return codigoPredial;
    }

    public void setCodigoPredial(String codigoPredial) {
        this.codigoPredial = codigoPredial;
    }

    public List<DatosMovimientosFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<DatosMovimientosFicha> movimientos) {
        this.movimientos = movimientos;
    }

    public List<DatosPropietariosFicha> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<DatosPropietariosFicha> propietarios) {
        this.propietarios = propietarios;
    }

    public BigInteger getNumficha() {
        return numficha;
    }

    public void setNumficha(BigInteger numficha) {
        this.numficha = numficha;
    }

    public BigInteger getCodigopredio() {
        return codigopredio;
    }

    public void setCodigopredio(BigInteger codigopredio) {
        this.codigopredio = codigopredio;
    }

    public String getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(String ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getFechaapertura() {
        return fechaapertura;
    }

    public void setFechaapertura(String fechaapertura) {
        this.fechaapertura = fechaapertura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipopredio() {
        return tipopredio;
    }

    public void setTipopredio(String tipopredio) {
        this.tipopredio = tipopredio;
    }

    public String getCodigopredial() {
        return codigopredial;
    }

    public void setCodigopredial(String codigopredial) {
        this.codigopredial = codigopredial;
    }

    public Boolean getAutomatico() {
        return automatico;
    }

    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }

}
