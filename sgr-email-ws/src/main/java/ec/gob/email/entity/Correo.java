package ec.gob.email.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "correo", schema = "mail")
public class Correo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private Boolean enviado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date FechaEnvio;

    private Long tramite;
    private String excepcion;
    private String origen;
    private String usuario;

    public Correo() {

    }

    public Correo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return FechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        FechaEnvio = fechaEnvio;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Correo{"
                + "id=" + id
                + ", destinatario='" + destinatario + '\''
                + ", asunto='" + asunto + '\''
                + ", mensaje='" + mensaje + '\''
                + ", enviado=" + enviado
                + ", FechaEnvio=" + FechaEnvio
                + '}';
    }
}
