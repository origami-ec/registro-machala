/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.restful.models;

import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class DetalleContratos implements Serializable {

    private String contrato;
    private String libro;
    private Integer registro;
    private String fecha;

    public DetalleContratos() {
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DetalleContratos{");
        sb.append("contrato=").append(contrato);
        sb.append(", libro=").append(libro);
        sb.append(", registro=").append(registro);
        sb.append(", fecha=").append(fecha);
        sb.append('}');
        return sb.toString();
    }

}
