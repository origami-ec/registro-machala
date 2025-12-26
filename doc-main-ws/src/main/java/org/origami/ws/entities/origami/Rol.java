package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Boolean estado;
    @Column(name = "is_director")
    private Boolean isDirector;
    @Column(name = "es_sub_director")
    private Boolean esSubDirector;
    @JoinColumn(name = "departamento_id")
    @ManyToOne
    private Departamento departamento;

    public Rol() {
    }

    public Rol(Long id) {
        this.id = id;
    }

    public Rol(Departamento departamento) {
        this.departamento = departamento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getDirector() {
        return isDirector;
    }

    public void setDirector(Boolean director) {
        isDirector = director;
    }

    public Boolean getEsSubDirector() {
        return esSubDirector;
    }

    public void setEsSubDirector(Boolean esSubDirector) {
        this.esSubDirector = esSubDirector;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", isDirector=" + isDirector +
                ", esSubDirector=" + esSubDirector +
                ", departamento=" + departamento + '}';
    }
}
