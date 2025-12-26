/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import java.util.Date;
import org.bcbg.documental.models.UsuarioDocs;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class CorreoModel {

    private String id;
    private String destinatario;
    private String numeroTramite;
    private String asunto;
    private String mensaje;
    private Boolean enviado;
    private Date fechaEnvio;
    private UsuarioDocs remitente;
    private UsuarioDocs destino;

    public CorreoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public UsuarioDocs getRemitente() {
        return remitente;
    }

    public void setRemitente(UsuarioDocs remitente) {
        this.remitente = remitente;
    }

    public UsuarioDocs getDestino() {
        return destino;
    }

    public void setDestino(UsuarioDocs destino) {
        this.destino = destino;
    }

    public String getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    @Override
    public String toString() {
        return "CorreoModel{" + "destinatario="
                + destinatario + ", asunto="
                + asunto + ", mensaje="
                + mensaje + ", enviado="
                + enviado + ", FechaEnvio="
                + fechaEnvio
                + '}';
    }

}
