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
public class RespuestaWs implements Serializable {

    private String codigoError;
    private String mensaje;
    private String sugerencia;
    private Object data;

    public RespuestaWs() {
    }

    public RespuestaWs(Long id) {
        this.data = id;
    }

    public RespuestaWs(String codigoError, String mensaje) {
        this.codigoError = codigoError;
        this.mensaje = mensaje;
    }
    
    public RespuestaWs(String codigoError, String mensaje, String sugerencia) {
        this.codigoError = codigoError;
        this.mensaje = mensaje;
        this.sugerencia = sugerencia;
    }

    public RespuestaWs(Object data) {
        this.data = data;
    }

    public RespuestaWs(String codigoError, String mensaje, Object data) {
        this.codigoError = codigoError;
        this.mensaje = mensaje;
        this.data = data;
    }
    
    public RespuestaWs(String codigoError, String mensaje, String sugerencia, Object data) {
        this.codigoError = codigoError;
        this.mensaje = mensaje;
        this.sugerencia = sugerencia;
        this.data = data;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RespuestaWsBddimi{"
                + "codigoError='" + codigoError + '\''
                + ", mensaje='" + mensaje + '\''
                + ", data=" + data
                + '}';
    }

}
