package org.origami.ws.entities.rrhh;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RH_CARGO", schema = "recurso_humano")
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CRG_ID")
    private Long id;
    @JoinColumn(name = "CRG_CATEGORIA", referencedColumnName = "CPE_ID")
    @ManyToOne
    private Categoria categoria;
    @Column(name = "CRG_NOMBRE")
    private String nombre;
    @Column(name = "CRG_CODIGO")
    private String codigo;
    @Column(name = "CRG_ELIMINADO")
    private Boolean eliminado;

    public Cargo() {
    }

    public Cargo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", categoria=" + categoria +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", eliminado=" + eliminado +
                '}';
    }
}
