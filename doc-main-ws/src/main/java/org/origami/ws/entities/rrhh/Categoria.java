package org.origami.ws.entities.rrhh;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RH_CATEGORIA", schema = "recurso_humano")
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CPE_ID")
    private Long id;
    @Column(name = "CPE_CODIGO")
    private String codigo;
    @Column(name = "CPE_NOMBRE")
    private String nombre;
    @Column(name = "CPE_DESCRIPCION")
    private String descripcion;
    @Column(name = "CPE_SECUENCIA")
    private Integer secuencia;

    public Categoria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", secuencia=" + secuencia +
                '}';
    }
}
