package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(schema = "public", name = "secuencia_tramite")
public class SecuenciaTramites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numeroTramite;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "SecuenciaTramites{" +
                "id=" + id +
                ", numeroTramite=" + numeroTramite +
                ", fecha=" + fecha +
                '}';
    }
}
