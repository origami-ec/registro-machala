package org.origami.archivos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProps {

    @Value("${app.urlResource}")
    private String urlResource;
    @Value("${app.imagenes}")
    private String imagenes;
    @Value("${app.archivos}")
    private String archivos;
    @Value("${app.archivoFirmados}")
    private String archivoFirmados;
    @Value("${app.manuales}")
    private String manuales;
    @Value("${app.documentos}")
    private String documentos;
    @Value("${app.notas}")
    private String notas;
    @Value("${app.keyFiles}")
    private String keyFiles;

    public AppProps() {
    }

    public String getArchivos() {
        return archivos;
    }

    public void setArchivos(String archivos) {
        this.archivos = archivos;
    }

    public String getUrlResource() {
        return urlResource;
    }

    public void setUrlResource(String urlResource) {
        this.urlResource = urlResource;
    }

    public String getImagenes() {
        return imagenes;
    }

    public void setImagenes(String imagenes) {
        this.imagenes = imagenes;
    }

    public String getArchivoFirmados() {
        return archivoFirmados;
    }

    public void setArchivoFirmados(String archivoFirmados) {
        this.archivoFirmados = archivoFirmados;
    }

    public String getDocumentos() {
        return documentos;
    }

    public void setDocumentos(String documentos) {
        this.documentos = documentos;
    }

    public String getManuales() {
        return manuales;
    }

    public void setManuales(String manuales) {
        this.manuales = manuales;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getKeyFiles() {
        return keyFiles;
    }

    public void setKeyFiles(String keyFiles) {
        this.keyFiles = keyFiles;
    }
}
