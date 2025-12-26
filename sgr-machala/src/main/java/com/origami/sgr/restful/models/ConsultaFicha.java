/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.restful.models;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsultaFicha implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tramite;
    private Long ficha;
    private Long ciudadela;
    private String mz;
    private String villa;
    private String identificacion;

    public ConsultaFicha() {
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Long getFicha() {
        return ficha;
    }

    public void setFicha(Long ficha) {
        this.ficha = ficha;
    }

    public Long getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(Long ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getVilla() {
        return villa;
    }

    public void setVilla(String villa) {
        this.villa = villa;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConsultaFicha{");
        sb.append("ficha=").append(ficha);
        sb.append(", ciudadela=").append(ciudadela);
        sb.append(", mz=").append(mz);
        sb.append(", villa=").append(villa);
        sb.append(", identificacion=").append(identificacion);
        sb.append('}');
        return sb.toString();
    }

}
