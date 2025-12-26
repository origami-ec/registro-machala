package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "registro_usuario")
public class RegistroUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String motivo;
    private Long userIngreso;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    public RegistroUsuario() {
    }

    public RegistroUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(Long userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public String toString() {
        return "AclRegistroUser{" +
                "id=" + id +
                ", motivo='" + motivo + '\'' +
                ", userIngreso=" + userIngreso +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
