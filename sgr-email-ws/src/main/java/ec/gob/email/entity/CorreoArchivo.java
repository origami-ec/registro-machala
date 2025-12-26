package ec.gob.email.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "correo_archivo", schema = "mail")
public class CorreoArchivo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String nombreArchivo;
    private String tipoArchivo;
    private String rutaArchivo;
    @JoinColumn(name = "correo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Correo correo;

    public CorreoArchivo() {
    }

    public CorreoArchivo(Long id) {
        this.id = id;
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

    public Correo getCorreo() {
        return correo;
    }

    public void setCorreo(Correo correo) {
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}
