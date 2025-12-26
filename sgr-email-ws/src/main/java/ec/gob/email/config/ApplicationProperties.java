package ec.gob.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Value("${email.correoHost}")
    private String correoHost;
    @Value("${email.correoPort}")
    private String correoPort;
    @Value("${email.correo}")
    private String correo;
    @Value("${email.razonSocial}")
    private String razonSocial;
    @Value("${email.ssl}")
    private String ssl;
    @Value("${email.correoClave}")
    private String correoClave;
    @Value("${asgard.rutaArchivos}")
    private String rutaArchivos;

    public ApplicationProperties() {
    }

    public String getCorreoHost() {
        return correoHost;
    }

    public void setCorreoHost(String correoHost) {
        this.correoHost = correoHost;
    }

    public String getCorreoPort() {
        return correoPort;
    }

    public void setCorreoPort(String correoPort) {
        this.correoPort = correoPort;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCorreoClave() {
        return correoClave;
    }

    public void setCorreoClave(String correoClave) {
        this.correoClave = correoClave;
    }

    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    @Override
    public String toString() {
        return "ApplicationProperties{" +
                "correoHost='" + correoHost + '\'' +
                ", correoPort='" + correoPort + '\'' +
                ", correo='" + correo + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", correoClave='" + correoClave + '\'' +
                '}';
    }
}
