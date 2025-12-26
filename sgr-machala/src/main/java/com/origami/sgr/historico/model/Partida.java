/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Partida implements Serializable {
    
    private String deudor;
    private String acreedor;
    private String fecha;
    private Integer anio;
    private Integer partida;
    private String observacion;

    public Partida() {
    }

    public String getDeudor() {
        return deudor;
    }

    public void setDeudor(String deudor) {
        this.deudor = deudor;
    }

    public String getAcreedor() {
        return acreedor;
    }

    public void setAcreedor(String acreedor) {
        this.acreedor = acreedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getPartida() {
        return partida;
    }

    public void setPartida(Integer partida) {
        this.partida = partida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Partida{deudor=").append(deudor);
        sb.append(", acreedor=").append(acreedor);
        sb.append(", fecha=").append(fecha);
        sb.append(", anio=").append(anio);
        sb.append(", partida=").append(partida);
        sb.append(", observacion=").append(observacion);
        sb.append('}');
        return sb.toString();
    }
    
}
