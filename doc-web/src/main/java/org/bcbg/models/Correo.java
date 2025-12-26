package org.bcbg.models;

import java.io.File;
import java.util.List;
import org.bcbg.documental.models.UsuarioDocs;

public class Correo {

    private String destinatario;
    private String numeroTramite;
    private String nombresDetinatario;
    private String asunto;
    private String mensaje;
    private List<CorreoArchivo> archivos;
    private List<File> adjuntos;
    private UsuarioDocs remitente;
    private UsuarioDocs destino;

    public Correo() {
    }

    public Correo(String destinatario, String asunto, String mensaje, List<CorreoArchivo> archivos, String numeroTramite, String nombresDetinatario, UsuarioDocs remitente, UsuarioDocs destino) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.archivos = archivos;
        this.numeroTramite = numeroTramite;
        this.nombresDetinatario = nombresDetinatario;
        this.remitente = remitente;
        this.destino = destino;
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

    public List<CorreoArchivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<CorreoArchivo> archivos) {
        this.archivos = archivos;
    }

    public List<File> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<File> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public String getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getNombresDetinatario() {
        return nombresDetinatario;
    }

    public void setNombresDetinatario(String nombresDetinatario) {
        this.nombresDetinatario = nombresDetinatario;
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

}
