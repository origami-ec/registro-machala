package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "usuario_responsable")
public class UsuarioResponsable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usuario;
    private String observacion;
    private Boolean responsable;
    private Boolean director;
    private Boolean jefe;
    private Boolean estado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento departamento;

    public UsuarioResponsable() {
    }

    public UsuarioResponsable(Long id, Long usuario, String observacion, Boolean responsable, Boolean director, Boolean jefe, Boolean estado, Departamento departamento) {
        this.id = id;
        this.usuario = usuario;
        this.observacion = observacion;
        this.responsable = responsable;
        this.director = director;
        this.jefe = jefe;
        this.estado = estado;
        this.departamento = departamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(Boolean responsable) {
        this.responsable = responsable;
    }

    public Boolean getDirector() {
        return director;
    }

    public void setDirector(Boolean director) {
        this.director = director;
    }

    public Boolean getJefe() {
        return jefe;
    }

    public void setJefe(Boolean jefe) {
        this.jefe = jefe;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "UsuarioResponsable{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", observacion='" + observacion + '\'' +
                ", responsable=" + responsable +
                ", director=" + director +
                ", jefe=" + jefe +
                ", estado=" + estado +
                '}';
    }
}
