package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "usuario_responsable_servicio")
public class UsuarioResponsableServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean estado;
    @JoinColumn(name = "usuario_responsable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UsuarioResponsable usuarioResponsable;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiciosDepartamento servicio;

    public UsuarioResponsableServicio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioResponsable getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(UsuarioResponsable usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public ServiciosDepartamento getServicio() {
        return servicio;
    }

    public void setServicio(ServiciosDepartamento servicio) {
        this.servicio = servicio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
