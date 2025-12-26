package org.origami.ws.entities.databcbg;

import javax.persistence.*;

@Entity
@Table(schema = "dbo", name = "AF_DEPARTAMENTO")
public class DepartamentoBCBG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEP_ID")
    private Long id;
    @Column(name = "DEP_NOMBRE")
    private String nombre;
    @Column(name = "DEP_CODIGO")
    private String codigo;
    @Column(name = "DEP_ELIMINADO")
    private Boolean estado;

    public DepartamentoBCBG() {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
