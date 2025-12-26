package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "dominio")
public class Dominio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Short tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }
}
