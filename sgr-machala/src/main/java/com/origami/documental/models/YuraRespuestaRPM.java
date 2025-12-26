/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author eduar
 */
public class YuraRespuestaRPM implements Serializable {

    private String mensaje;
    private List<YuraModel> listaItemRespuestaRPMDTO;

    public YuraRespuestaRPM() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<YuraModel> getListaItemRespuestaRPMDTO() {
        return listaItemRespuestaRPMDTO;
    }

    public void setListaItemRespuestaRPMDTO(List<YuraModel> listaItemRespuestaRPMDTO) {
        this.listaItemRespuestaRPMDTO = listaItemRespuestaRPMDTO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("YuraRespuestaRPM{");
        sb.append("mensaje=").append(mensaje);
        sb.append(", listaItemRespuestaRPMDTO=").append(listaItemRespuestaRPMDTO);
        sb.append('}');
        return sb.toString();
    }

}
