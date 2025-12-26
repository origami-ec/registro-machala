package org.bcbg.models;

public class CorreoArchivo {

    private String nombreArchivo;
    private String tipoArchivo;
    private String archivoBase64;

    public CorreoArchivo() {
    }

    public CorreoArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public CorreoArchivo(String nombreArchivo, String tipoArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.tipoArchivo = tipoArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }
}
