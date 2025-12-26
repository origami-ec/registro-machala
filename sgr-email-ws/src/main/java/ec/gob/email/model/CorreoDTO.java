package ec.gob.email.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class CorreoDTO implements Serializable{

    private String destinatario;
    private String asunto;
    private String mensaje;
    private List<CorreoArchivoDTO> archivos;
    private List<File> adjuntos;
    private boolean tramite;
    private Long numeroTramite;
    private String error;
    private String usuario;
    private ConfiguracionDTO configuracion;

    public CorreoDTO() {
    }

    public CorreoDTO(String destinatario, String asunto, String mensaje, List<CorreoArchivoDTO> archivos) {
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

    public List<CorreoArchivoDTO> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<CorreoArchivoDTO> archivos) {
        this.archivos = archivos;
    }

    public List<File> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<File> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public boolean isTramite() {
        return tramite;
    }

    public void setTramite(boolean tramite) {
        this.tramite = tramite;
    }

    public ConfiguracionDTO getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(ConfiguracionDTO configuracion) {
        this.configuracion = configuracion;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "CorreoDTO{" +
                "destinatario='" + destinatario + '\'' +
                ", asunto='" + asunto + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", archivos=" + archivos +
                ", adjuntos=" + adjuntos +
                '}';
    }
}
