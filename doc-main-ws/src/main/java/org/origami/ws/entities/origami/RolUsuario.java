package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "rol_usuario")
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "usuario_id")
    private Long usuario;
    @JoinColumn(name = "rol_id")
    @ManyToOne
    private Rol rol;

    public RolUsuario() {
    }

    public RolUsuario(Long usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    public RolUsuario(Rol rol) {
        this.rol = rol;
    }


    public RolUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "RolUsuario{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", rol=" + rol +
                '}';
    }
}
