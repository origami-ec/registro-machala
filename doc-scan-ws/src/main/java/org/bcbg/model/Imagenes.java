package org.bcbg.model;

public class Imagenes {
    private String descripcion;
    private String url;
    private String fileName;

    public Imagenes() {
    }

    public Imagenes(String descripcion, String url) {
        this.descripcion = descripcion;
        this.url = url;
    }

    public Imagenes(String descripcion, String url, String fileName) {
        this.descripcion = descripcion;
        this.url = url;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
