package org.origami.docs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class AppProps {
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${origamidocs.archivos}")
    private String rutaArchivo;
    @Value("${origamidocs.imagenes}")
    private String rutaImagen;
    @Value("${origamidocs.apiImagenes}")
    private String apiImagenes;
    @Value("${origamidocs.apiContext}")
    private String apiContext;
    @Value("${origamidocs.archivosFD}")
    private String rutaArchivosFD;
    @Value("${origamidocs.archivosNT}")
    private String rutaArchivosNT;
    @Value("${origamidocs.rutaReportes}")
    private String rutaReportes;
    @Value("${origamidocs.rutaImagenes}")
    private String rutaImagenes;
    @Value("${origamidocs.keyFiles}")
    private String keyFiles;

    public AppProps() {
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getApiImagenes() {
        return apiImagenes;
    }

    public void setApiImagenes(String apiImagenes) {
        this.apiImagenes = apiImagenes;
    }

    public String getApiContext() {
        return apiContext;
    }

    public void setApiContext(String apiContext) {
        this.apiContext = apiContext;
    }

    public String getRutaArchivosFD() {
        return rutaArchivosFD;
    }

    public void setRutaArchivosFD(String rutaArchivosFD) {
        this.rutaArchivosFD = rutaArchivosFD;
    }

    public String getRutaArchivosNT() {
        return rutaArchivosNT;
    }

    public void setRutaArchivosNT(String rutaArchivosNT) {
        this.rutaArchivosNT = rutaArchivosNT;
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    public String getRutaReportes() {
        return rutaReportes;
    }

    public void setRutaReportes(String rutaReportes) {
        this.rutaReportes = rutaReportes;
    }

    public String getRutaImagenes() {
        return rutaImagenes;
    }

    public void setRutaImagenes(String rutaImagenes) {
        this.rutaImagenes = rutaImagenes;
    }

    public String getKeyFiles() {
        return keyFiles;
    }

    public void setKeyFiles(String keyFiles) {
        this.keyFiles = keyFiles;
    }
}


