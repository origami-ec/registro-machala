package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "departamento")
public class Departamento {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private Long tipo;
    private Long idBcbg;
    private String nombre;
    private Boolean estado;
    private String codigo;



    public Departamento() {
    }

    public Departamento(String nombre) {
        this.nombre = nombre;
    }

    public Departamento(Long id, String nombre,  Boolean estado, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.codigo = codigo;
    }

    public Departamento(Long id) {
        this.id = id;
    }

    public Departamento(Long id, Boolean estado) {
        this.id = id;
        this.estado = estado;
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

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public Long getIdBcbg() {
        return idBcbg;
    }

    public void setIdBcbg(Long idBcbg) {
        this.idBcbg = idBcbg;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado + '}';
    }
}
