package org.origami.ws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ApplicationProperties {

    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${ventanilla.rutaArchivos}")
    private String rutaArchivos;
    @Value("${ventanilla.rutaFirmaElectronica}")
    private String rutaFirmaElectronica;
    @Value("${ventanilla.urlZull}")
    private String urlZull;
    @Value("${ventanilla.urlWeb}")
    private String urlWeb;
    @Value("${ventanilla.urlDinardap}")
    private String urlDinardap;
    @Value("${ventanilla.usuarioDinardap}")
    private String usuarioDinardap;
    @Value("${ventanilla.claveDinardap}")
    private String claveDinardap;

    @Value("${ventanilla.rutaReportes}")
    private String rutaReportes;
    @Value("${ventanilla.rutaImagenes}")
    private String rutaImagenes;
    @Value("${ventanilla.keyFiles}")
    private String keyFiles;
    public String getUrlDinardap() {
        return urlDinardap;
    }

    public void setUrlDinardap(String urlDinardap) {
        this.urlDinardap = urlDinardap;
    }

    public String getUsuarioDinardap() {
        return usuarioDinardap;
    }

    public void setUsuarioDinardap(String usuarioDinardap) {
        this.usuarioDinardap = usuarioDinardap;
    }

    public String getClaveDinardap() {
        return claveDinardap;
    }

    public void setClaveDinardap(String claveDinardap) {
        this.claveDinardap = claveDinardap;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public String getRutaFirmaElectronica() {
        return rutaFirmaElectronica;
    }

    public void setRutaFirmaElectronica(String rutaFirmaElectronica) {
        this.rutaFirmaElectronica = rutaFirmaElectronica;
    }

    public String getUrlZull() {
        return urlZull;
    }

    public void setUrlZull(String urlZull) {
        this.urlZull = urlZull;
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
