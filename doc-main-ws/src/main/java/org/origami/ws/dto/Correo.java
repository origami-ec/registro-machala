package org.origami.ws.dto;

import java.io.File;
import java.util.List;

public class Correo {

    private String destinatario;
    private String asunto;
    private String mensaje;
    private List<CorreoArchivo> archivos;
    private List<File> adjuntos;

    public Correo() {
    }

    public Correo(String destinatario, String asunto, String mensaje, List<CorreoArchivo> archivos) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.archivos = archivos;
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

    @Override
    public String toString() {
        return "Correo{" +
                "destinatario='" + destinatario + '\'' +
                ", asunto='" + asunto + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", archivos=" + archivos +
                ", adjuntos=" + adjuntos +
                '}';
    }
}
